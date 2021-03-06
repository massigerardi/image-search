/**
 * 
 */
package com.dart.archive.image.search.color;

import ij.process.ColorProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.utils.ImageHelper;
import com.dart.archive.image.utils.ImageUtils;
import com.google.common.base.Objects;


/**
 * @author massi
 * 
 */
@Slf4j
public class NaiveColorImageSearcher implements ImageSearcher {

	private static final String COLOR = "COLOR";
	ImageHelper imageHelper = new ImageHelper();
	
	private String sources;
	private int zones;
	
	List<ImageDescriptor> imagesDescriptors = new ArrayList<ImageDescriptor>();
	
	public NaiveColorImageSearcher(String imageHome) {
		this(imageHome, 5, 60);
	}

	public NaiveColorImageSearcher(String imageHome, int zones, int zoneSize) {
		this.sources = new File(imageHome).getAbsolutePath();
		this.zones = zones;
		this.zoneSize = zoneSize;
		init();
	}

	@Override
	public String toString() {
		return 	Objects.toStringHelper(this.getClass())
				.add("sources", sources)
				.add("images", imagesDescriptors.size()).toString();
	}

	protected void init() {
		log.debug("init "+this.toString());
		baseSize = zoneSize * zones;
		fraction = zones * 2;
		baseDistance = Math.pow(zones, 2) * Math.sqrt( 3*(Math.pow(255,2)));
		imagesDescriptors.clear();
		List<File> files = imageHelper.getImages(new File(sources));
		for (File file : files) {
			BufferedImage current = ImageUtils.resize(300, 300, file.getAbsolutePath());
			Color[][] signature = calcSignature(current);
			ImageDescriptor image = new ImageDescriptor(signature, file);
			imagesDescriptors.add(image);
		}
		log.debug("init done"+this.toString());
	}

	// The reference image "signature" (25 representative pixels, each in
	// R,G,B).
	// We use instances of Color to make things simpler.
	private double baseDistance;
	private float fraction;
	private int zoneSize;
	// The base size of the images.
	private int baseSize;

	/*
	 * This method calculates and returns signature vectors for the input image.
	 */
	Color[][] calcSignature(BufferedImage i) {
		// Get memory for the signature.
		Color[][] sig = new Color[zones][zones];
		// For each of the 25 signature values average the pixels around it.
		// Note that the coordinate of the central pixel is in proportions.
		float[] prop = new float[zones];
		for (int j = 0; j < prop.length; j++) {
			prop[j] = ( 2*j+1f )/ fraction;
		}		
		for (int x = 0; x < zones; x++)
			for (int y = 0; y < zones; y++)
				sig[x][y] = averageAround(i, prop[x], prop[y]);
		return sig;
	}

	/*
	 * This method averages the pixel values around a central point and return
	 * the average as an instance of Color. The point coordinates are
	 * proportional to the image.
	 */
	private Color averageAround(BufferedImage i, double px, double py) {
		ColorProcessor processor = new ColorProcessor(i);
		
		// Get memory for a pixel and for the accumulator.
		double[] accum = new double[3];
		// The size of the sampling area.
		int sampleSize = zoneSize/4;
		int numPixels = 0;
		// Sample the pixels.
		for (double x = px * baseSize - sampleSize; x < px * baseSize
				+ sampleSize; x++) {
			for (double y = py * baseSize - sampleSize; y < py * baseSize
					+ sampleSize; y++) {
				int [] values = processor.getPixel((int)x, (int)y, null);
				accum[0] += values[0];
				accum[1] += values[1];
				accum[2] += values[2];
				numPixels++;
			}
		}
		// Average the accumulated values.
		accum[0] /= numPixels;
		accum[1] /= numPixels;
		accum[2] /= numPixels;
		return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
	}

	/*
	 * This method calculates the distance between the signatures of an image
	 * and the reference one. The signatures for the image passed as the
	 * parameter are calculated inside the method.
	 */
	double calcDistance(Color[][] signature, Color[][] candidate) {
		// Calculate the signature for that image.
		// There are several ways to calculate distances between two vectors,
		// we will calculate the sum of the distances between the RGB values of
		// pixels in the same positions.
		double dist = 0;
		for (int x = 0; x < zones; x++)
			for (int y = 0; y < zones; y++) {
				int r1 = signature[x][y].getRed();
				int g1 = signature[x][y].getGreen();
				int b1 = signature[x][y].getBlue();
				int r2 = candidate[x][y].getRed();
				int g2 = candidate[x][y].getGreen();
				int b2 = candidate[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
						* (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		return 1-(dist/baseDistance);
	}

	@Override
	public Collection<Candidate> search(File file) {
		Collection<Candidate> candidates = new TreeSet<Candidate>();
		try {
			// Put the reference, scaled, in the left part of the UI.
			BufferedImage ref = ImageUtils.resize(300, 300, file.getAbsolutePath());
			// Calculate the signature vector for the reference.
			Color[][] signature = calcSignature(ref);
			// Now we need a component to store X images in a stack, where X is the
			// number of images in the same directory as the original one.
			// For each image, calculate its signature and its distance from the
			// reference signature.
			for (ImageDescriptor imageDescriptor : imagesDescriptors) {
				double distance = calcDistance(signature, imageDescriptor.getSignature());
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				double result = (double)Math.round(distance * 100) / 100;
				candidates.add(new Candidate(imageDescriptor.getImage(), Double.valueOf(twoDForm.format(result)), COLOR));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return candidates;
	}
	
	@Override
	public void reload() {
		init();
	}


}
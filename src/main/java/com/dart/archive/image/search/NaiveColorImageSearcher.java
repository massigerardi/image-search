/**
 * 
 */
package com.dart.archive.image.search;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;


/**
 * @author massi
 * 
 */
public class NaiveColorImageSearcher extends AImageSearcher implements ImageSearcher {

	private String reference;
	private int zones;
	
	List<ImageDescriptor> images;
	public NaiveColorImageSearcher(String reference, int zones, int zoneSize) {
		this.reference = reference;
		this.zones = zones;
		this.zoneSize = zoneSize;
		init();
	}

	private void init() {
		baseSize = zoneSize * zones;
		fraction = zones * 2;
		baseDistance = Math.pow(zones, 2) * Math.sqrt( 3*(Math.pow(255,2)));
		images = new ArrayList<ImageDescriptor>();
		File[] files = getOtherImageFiles(new File(reference));
		try {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				RenderedImage current = rescale(ImageIO.read(file));
				Color[][] signature = calcSignature(current);
				ImageDescriptor image = new ImageDescriptor(signature, file);
				images.add(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * This method rescales an image to 300,300 pixels using the JAI scale
	 * operator.
	 */
	RenderedImage rescale(RenderedImage i) {
		float scaleW = ((float) baseSize) / i.getWidth();
		float scaleH = ((float) baseSize) / i.getHeight();
		// Scales the original image
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(i);
		pb.add(scaleW);
		pb.add(scaleH);
		pb.add(0.0F);
		pb.add(0.0F);
		pb.add(new InterpolationNearest());
		// Creates a new, scaled image and uses it on the DisplayJAI component
		return JAI.create("scale", pb);
	}

	/*
	 * This method calculates and returns signature vectors for the input image.
	 */
	Color[][] calcSignature(RenderedImage i) {
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
	private Color averageAround(RenderedImage i, double px, double py) {
		// Get an iterator for the image.
		RandomIter iterator = RandomIterFactory.create(i, null);
		// Get memory for a pixel and for the accumulator.
		double[] pixel = new double[3];
		double[] accum = new double[3];
		// The size of the sampling area.
		int sampleSize = zoneSize/4;
		int numPixels = 0;
		// Sample the pixels.
		for (double x = px * baseSize - sampleSize; x < px * baseSize
				+ sampleSize; x++) {
			for (double y = py * baseSize - sampleSize; y < py * baseSize
					+ sampleSize; y++) {
				iterator.getPixel((int) x, (int) y, pixel);
				accum[0] += pixel[0];
				accum[1] += pixel[1];
				accum[2] += pixel[2];
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

	/*
	 * This method get all image files in the same directory as the reference.
	 * Just for kicks include also the reference image.
	 */
	private File[] getOtherImageFiles(File reference) {
		// List all the image files in that directory.
		File[] others = reference.listFiles(new FileFilter() {

			public boolean accept(File f) {
				if (f.getName().toLowerCase().endsWith(".jpeg"))
					return true;
				if (f.getName().toLowerCase().endsWith(".jpg"))
					return true;
				return false;
			}
		});
		return others;
	}

	@Override
	void populateCandidate(Collection<Candidate> candidates, File file) {
		try {
			RenderedImage image = ImageIO.read(file);
			// Put the reference, scaled, in the left part of the UI.
			RenderedImage ref = rescale(image);
			// Calculate the signature vector for the reference.
			Color[][] signature = calcSignature(ref);
			// Now we need a component to store X images in a stack, where X is the
			// number of images in the same directory as the original one.
			// For each image, calculate its signature and its distance from the
			// reference signature.
			for (ImageDescriptor imageDescriptor : images) {
				double distance = calcDistance(signature, imageDescriptor.getSignature());
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				double result = (double)Math.round(distance * 100) / 100;
				Candidate candidate = new NaiveCandidate(Double.valueOf(twoDForm.format(result)), imageDescriptor.getImage());
				candidates.add(candidate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
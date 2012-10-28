package com.dart.archive.image.search.surf.ip;

import java.awt.Color;

/** Parameter for SURF Detector, SURF Descriptor and for displaying the results.  */
public class Settings {

	public final static String programVersion = "ImageJ SURF v2009-12-01";

	private Settings() { 	
		
	}

	private static Settings instance;
	public static Settings getSettings() {
		if (instance==null) {
			instance = new Settings();
		}
		return instance;
	}
	
	
	/////////////////////////////////////////////////////////////////////////7
	// Detector params

	/**
	 * Defines octaves and layers (filter sizes). The actual implementation
	 * assumes the equal number of layers in each octave. Default is 4
	 * octaves with 4 layers per octave and values as proposed in the SURF
	 * paper (2008):<br>
	 * <code>{{9,15,21,27},{15,27,39,51},{27,51,75,99},{51,99,147,195}}</code>.
	 */
	private int[][] filterSizes = { { 9, 15, 21, 27 }, { 15, 27, 39, 51 }, { 27, 51, 75, 99 }, { 51, 99, 147, 195 } };
	private int[] maxFilterSizes = {27, 51, 99, 195};
	//TODO: add alternate filter size (to apply after image scaling)

	/** Number of analysed octaves. Default is 4. */
	private int octaves = 4; //  3 .. 4 (filterSizes.length)      OpenSURF (C++): 3     orig.SURF: 4
	private int layers = 4; // 3 .. 4 (filterSizes[0].length)
	public int getOctaves() {return octaves;}
	public int getLayers() {return layers;}

	public int getFilterSize(int octave, int layer) {
		return filterSizes[octave][layer];
	}

	/** Returns the biggest filter size in the octave. */
	public int getMaxFilterSize(int octave) {
		return maxFilterSizes[octave];
	}

	// Set this flag "true" to double the image size
	boolean doubleImageSize = false; 
	// TODO: rename to "upscale image first"? + implementation (siehe ImageJ_SIFT e.g. as parameter to intImg constr.) + update constructor/getter/setter


	/**
	 * The responses are thresholded such that all values below the
	 * <code>threshold</code> are removed. Increasing the threshold lowers the
	 * number of detected interest points, and vice versa. Must be >= 0 and <= 1.
	 */
	//	private float threshold = 600; 
	// NB: in C++ version 0.0004f (sometimes 0.0006f); in C# version 0.002f
	// orig.SURF: double thres = 4.0; "Blob response treshold";  cvsurf: "hessianThreshold"
	// private float threshold = 0.0002f; 
	private float threshold = 0.0000001f; 
	public float getThreshold() {return threshold;}
	public void setThreshold(float f) {threshold = f;}

	/** The initial sampling step (1..6). Default is 2. <br>
	 * Will be doubled for each next octave (see stepIncFactor). */
	private int initStep = 1; // orig.SURF: 2 (2 gives less IPs than 1 but much faster)
	public int getInitStep() {return initStep;}

	private int stepIncFactor = 2;
	public int getStepIncFactor() {return stepIncFactor;}

	//	private int interp_steps = 5; // from C# version of OpenSURF (not exists in C++ version)  TODO: purpose?


	/////////////////////////////////////////////////////////////////////////7
	// Descriptor params

	/** Extract upright (i.e. not rotation invariant) descriptors. Default is <code>false</code>. */
	private boolean upright;
	public boolean isUpright() {return upright;}

	private int descSize = 64;
	public int getDescSize() {return descSize;}


	/////////////////////////////////////////////////////////////////////////7
	// Display params

	private boolean displayOrientationVectors = true;
	public boolean isDisplayOrientationVectors() {return displayOrientationVectors;}

	private boolean displayDescriptorWindows = false;
	public boolean isDisplayDescriptorWindows() {return displayDescriptorWindows;}

	private int lineWidth = 1; // 1..5
	public int getLineWidth() {return lineWidth;}

	boolean displayStatistics = false;
	public boolean isDisplayStatistics() {return displayStatistics;}

	public Color getDescriptorWindowColor() { return Color.PINK; }
	public Color getOrientationVectorColor() { return Color.YELLOW; }

	/** Drawing color for dark blobs on light background */   
	public Color getDarkPointColor() { return Color.BLUE; }

	/** Drawing color for light blobs on dark background */   
	public Color getLightPointColor() { return Color.RED; }

}


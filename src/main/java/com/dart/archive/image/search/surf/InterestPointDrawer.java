package com.dart.archive.image.search.surf;

import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import ij.process.ImageProcessor;

import java.util.Arrays;
import java.util.List;

import com.dart.archive.image.search.surf.ip.Descriptor;
import com.dart.archive.image.search.surf.ip.Detector;
import com.dart.archive.image.search.surf.ip.IntegralImage;
import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Settings;


// TODO: license info in all files?

/** SURF library for ImageJ. Based on SURF paper (2008) and OpenSURF C++ implementation. 
 * See SURF_Test.java for example of usage.
 * @author Eugen Labun
 */
public class InterestPointDrawer {
	
	private InterestPointDrawer() {}
	
	/** Cached last result. */ //TODO: sve multiple results as a map "img <-> I.P. list" ?
	private static List<InterestPoint> lastResult = null;
	synchronized public static void setLastResult(List<InterestPoint> ipts) {lastResult = ipts;}
	synchronized public static List<InterestPoint> getLastResult() {return lastResult;}
	
	//TODO: cache also the last parameter set!

	/** Draws interest points onto suplied <code>ImageProcessor</code>. */
	public static void drawInterestPoints(ImageProcessor img, List<InterestPoint> ipts, Settings settings) {
		// TODO: 3 loops: 1) rectangles only, 2) orientation vectors only, 3) interest points only
		// ^^ to be shure all interest points are visible!

		for(InterestPoint ipt : ipts) 
			drawSingleInterestPoint(img, settings, ipt);
	}
	
	
	public static void drawSingleInterestPoint(ImageProcessor img, Settings p, InterestPoint ipt) {
		int x = round(ipt.x);
		int y = round(ipt.y);
		float w = ipt.scale * 10; // for descriptor window
		float ori = ipt.orientation;
		float co = (float) cos(ori);
		float si = (float) sin(ori);
		float s = ipt.strength * 10000; // for orientation vector

		// The order of drawing is important: 
		// 1) descriptor windows
		// 2) orientation vectors
		// 3) points
		// Otherwise some points could be overdrawed by descriptor- or vector-lines. 
		
		// Draw descriptor window around the interest point
		if (p.isDisplayDescriptorWindows()) {
			img.setLineWidth(p.getLineWidth());
			img.setColor(p.getDescriptorWindowColor());

			float x0 = w * ( si + co) + ipt.x;   float y0 = w * (-co + si) + ipt.y;
			float x1 = w * ( si - co) + ipt.x;   float y1 = w * (-co - si) + ipt.y;
			float x2 = w * (-si - co) + ipt.x;   float y2 = w * ( co - si) + ipt.y;
			float x3 = w * (-si + co) + ipt.x;   float y3 = w * ( co + si) + ipt.y;

            // normal window
//			img.moveTo(round(x0), round(y0));
//			img.lineTo(round(x1), round(y1));
//			img.lineTo(round(x2), round(y2));
//			img.lineTo(round(x3), round(y3));
//			img.lineTo(round(x0), round(y0));
			
			// 'envelope'-window
			img.moveTo(x, y);
			img.lineTo(round(x0), round(y0));
			img.lineTo(round(x1), round(y1));
			img.lineTo(round(x2), round(y2));
			img.lineTo(round(x3), round(y3));
			img.lineTo(x, y);
			
			
		}

		
		// Draw orientation vector
//		if (ori != 0) {
		if (p.isDisplayOrientationVectors()) {
			img.setLineWidth(p.getLineWidth());
			img.setColor(p.getOrientationVectorColor());
			img.drawLine(x, y, round(s*co + x), round(s*si + y));
		}

		
		// Draw interest point
		img.setLineWidth(p.getLineWidth()*4);
		if (ipt.sign)
			img.setColor(p.getDarkPointColor());
		else
			img.setColor(p.getLightPointColor());
		img.drawDot(x, y);
		
	}

}

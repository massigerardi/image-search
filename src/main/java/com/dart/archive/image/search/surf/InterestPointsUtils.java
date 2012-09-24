/**
 * 
 */
package com.dart.archive.image.search.surf;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Settings;

/**
 * @author massi
 *
 */
public class InterestPointsUtils {

	private static Opener opener = new Opener();
	
	private InterestPointsUtils() {
	}
	
	public static void displayInterestingPoints(Map<InterestPoint, InterestPoint> matchedPoints, File image, List<InterestPoint> points, File candidate, List<InterestPoint> list) {
		// Draw matched interest points:
		ImagePlus image1 = opener.openImage(image.getAbsolutePath());
		ImagePlus image2 = opener.openImage(candidate.getAbsolutePath());
		
		// 1) prepare a copy of image1
		ImageProcessor image1ProcessorCopy = image1.getProcessor().duplicate().convertToRGB();
		String image1NewTitle = String.format("%s: %d of %d Interest Points", 
				image1.getTitle().split(":")[0], matchedPoints.size(), points.size());
		ImagePlus image1Copy = new ImagePlus(image1NewTitle, image1ProcessorCopy);
		
		// 1) prepare a copy of image1
		ImageProcessor image3ProcessorCopy = image1.getProcessor().duplicate().convertToRGB();
		String image3NewTitle = String.format("%s: %d of %d Interest Points", 
				image1.getTitle().split(":")[0], points.size(), points.size());
		ImagePlus image3Copy = new ImagePlus(image3NewTitle, image1ProcessorCopy);
		
		// 2) prepare a copy of image2
		ImageProcessor imageProcessor2Copy = image2.getProcessor().duplicate().convertToRGB();
		String image2NewTitle = String.format("%s: %d of %d Interest Points", 
				image2.getTitle().split(":")[0], matchedPoints.size(), list.size());
		ImagePlus image2Copy = new ImagePlus(image2NewTitle, imageProcessor2Copy);
		
		// 2) prepare a copy of image2
		ImageProcessor imageProcessor4Copy = image2.getProcessor().duplicate().convertToRGB();
		String image4NewTitle = String.format("%s: %d of %d Interest Points", 
				image2.getTitle().split(":")[0], list.size(), list.size());
		ImagePlus image4Copy = new ImagePlus(image4NewTitle, imageProcessor2Copy);
		
		for (Entry<InterestPoint, InterestPoint> pair : matchedPoints.entrySet()) {
			InterestPointDrawer.drawSingleInterestPoint(image1ProcessorCopy, new Settings(), pair.getKey());
			InterestPointDrawer.drawSingleInterestPoint(imageProcessor2Copy, new Settings(), pair.getValue());
			InterestPointDrawer.drawSingleInterestPoint(image1ProcessorCopy, new Settings(), pair.getValue());
			InterestPointDrawer.drawSingleInterestPoint(imageProcessor2Copy, new Settings(), pair.getKey());
		}

		for (InterestPoint interestPoint : points) {
			InterestPointDrawer.drawSingleInterestPoint(image3ProcessorCopy, new Settings(), interestPoint);
		}
		
		for (InterestPoint interestPoint : list) {
			InterestPointDrawer.drawSingleInterestPoint(imageProcessor4Copy, new Settings(), interestPoint);
		}
		
		image1Copy.show();
		image2Copy.show();
		image3Copy.show();
		image4Copy.show();
		read("any key to continue");
		image1Copy.hide();
		image2Copy.hide();
		image3Copy.hide();
		image4Copy.hide();
	}

	private static String read(String string) {
		System.out.println(string);
		String command = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (command==null) {
			try {
				command = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}
		}
		return command;
	}

	
}

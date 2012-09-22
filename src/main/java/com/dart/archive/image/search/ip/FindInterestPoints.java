package com.dart.archive.image.search.ip;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.util.List;

import com.dart.archive.image.search.ip.surf.InterestPoint;
import com.dart.archive.image.search.ip.surf.Params;

public class FindInterestPoints {
	
	public static void main(String[] args) {
		findInterestPoint("/Users/massi/Pictures/test-images/dynys/dynys.pdf_16_146-A.jpg");
		findInterestPoint("/Users/massi/Pictures/test-images/dynys/dynys.pdf_16_146-B.jpg");
		findInterestPoint("/Users/massi/Pictures/test-images/dynys/dynys.pdf_16_146-C.jpg");
		findInterestPoint("/Users/massi/Pictures/test-images/dynys/dynys.pdf_16_146-D.jpg");
		findInterestPoint("/Users/massi/Pictures/test-images/dynys/dynys.pdf_16_146.jpg");
		
	}
	
	public static void findInterestPoint(String imagePath) {
		long start = System.currentTimeMillis();
		Opener opener = new Opener();
		ImagePlus activeImage = opener.openImage(imagePath);
		List<InterestPoint> ipts = new InterestingPointsFinder().findInterestingPoints(activeImage.getProcessor());
		long end = System.currentTimeMillis();
		System.out.println("found "+ipts.size()+" IPs in "+(end-start)+"ms");
		if (ipts.size() == 0) {
			IJ.showMessage("No Interest Points found.");
			return;
		}
		// Draw interest points on a copy of the active image.
		ImageProcessor ip2 = activeImage.getProcessor().duplicate().convertToRGB();
		IJFacade.drawInterestPoints(ip2, ipts, new Params());
		String title = String.format("%s: %d Interest Points", 
				activeImage.getTitle().split(":")[0], ipts.size());
		ImagePlus imp2 = new ImagePlus(title, ip2);
		imp2.show();
	}
	
}

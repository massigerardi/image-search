/**
 * 
 */
package com.dart.archive.image.search.ip;

import ij.process.ImageProcessor;

import java.util.Arrays;
import java.util.List;

import com.dart.archive.image.search.ip.surf.Descriptor;
import com.dart.archive.image.search.ip.surf.Detector;
import com.dart.archive.image.search.ip.surf.IntegralImage;
import com.dart.archive.image.search.ip.surf.InterestPoint;
import com.dart.archive.image.search.ip.surf.Params;

/**
 * @author massi
 *
 */
public class InterestingPointsFinder {

	public List<InterestPoint> findInterestingPoints(ImageProcessor processor) {
		
		IntegralImage image = new IntegralImage(processor, true);
		
		// Detect interest points with Fast-Hessian
		List<InterestPoint> ipts = Detector.fastHessian(image, new Params());
		
		float[] strengthOfIPs = new float[ipts.size()];
		for (int i = 0; i < ipts.size(); i++) {
			strengthOfIPs[i] = ipts.get(i).strength;
		}
		Arrays.sort(strengthOfIPs);
		
		// Describe interest points with SURF-descriptor
//		for (InterestPoint ipt: ipts)
//			Descriptor.computeAndSetOrientation(ipt, image);
		for (InterestPoint ipt: ipts)
			Descriptor.computeAndSetDescriptor(ipt, image, new Params());

		return ipts;

	}
	
	
}

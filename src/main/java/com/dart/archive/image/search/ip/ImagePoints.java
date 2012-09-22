/**
 * 
 */
package com.dart.archive.image.search.ip;

import java.io.File;
import java.util.List;

import com.dart.archive.image.search.ip.surf.InterestPoint;

/**
 * @author massi
 *
 */
public class ImagePoints {

	File image;
	
	List<InterestPoint> points;

	public ImagePoints(File image, List<InterestPoint> points) {
		super();
		this.image = image;
		this.points = points;
	}

	/**
	 * @return the image
	 */
	public File getImage() {
		return image;
	}

	/**
	 * @return the points
	 */
	public List<InterestPoint> getPoints() {
		return points;
	}
	
	
	
}

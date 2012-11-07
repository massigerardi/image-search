/**
 * 
 */
package com.dart.archive.image.search.surf;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.dart.archive.image.search.surf.ip.InterestPoint;

/**
 * @author massi
 *
 */
public class ImageInterestPoints implements Serializable {

	File image;
	
	List<InterestPoint> points;

	public ImageInterestPoints(File image, List<InterestPoint> points) {
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

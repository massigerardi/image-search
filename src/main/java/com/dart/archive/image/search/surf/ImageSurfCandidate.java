/**
 * 
 */
package com.dart.archive.image.search.surf;

import java.io.File;

import com.dart.archive.image.search.ACandidate;

/**
 * @author massi
 *
 */
public class ImageSurfCandidate extends ACandidate {

	File image;

	double distance;
	
	int interestingPoints;
	
	public ImageSurfCandidate(File image, double distance, int interestingPoints) {
		super();
		this.image = image;
		this.distance = distance;
		this.interestingPoints = interestingPoints;
	}

	/**
	 * @return the interestingPoints
	 */
	public int getInterestingPoints() {
		return interestingPoints;
	}

	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.Candidate#getDistance()
	 */
	public Double getScore() {
		return distance;
	}

	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.Candidate#getImage()
	 */
	public File getImage() {
		return image;
	}

	@Override
	public String toString() {
		return image.getName()+"["+distance+"]["+interestingPoints+"]";
	}
	

}

/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

/**
 * @author massi
 *
 */
public class NaiveCandidate implements Candidate {

	public NaiveCandidate(Double distance, File image) {
		this.distance = distance;
		this.image = image;
	}

	public Double getDistance() {
		return distance;
	}

	public File getImage() {
		return image;
	}

	Double distance;
	
	File image;

	public int compareTo(Candidate other) {
		return other.getDistance().compareTo(this.distance);
	}

	@Override
	public String toString() {
		return image.getName()+"["+distance+"]";
	}
	
	
	
}

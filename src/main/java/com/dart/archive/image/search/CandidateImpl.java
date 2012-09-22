/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

/**
 * @author massi
 *
 */
public class CandidateImpl extends ACandidate {

	public CandidateImpl(Double distance, File image) {
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

	@Override
	public String toString() {
		return image.getName()+"["+distance+"]";
	}
	
	
	
}

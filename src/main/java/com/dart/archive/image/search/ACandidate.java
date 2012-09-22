/**
 * 
 */
package com.dart.archive.image.search;


/**
 * @author massi
 *
 */
public abstract class ACandidate implements Candidate {

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Candidate other) {
		return other.getDistance().compareTo(getDistance());
	}


}

/**
 * 
 */
package com.dart.archive.image.search;

import com.google.common.collect.ComparisonChain;


/**
 * @author massi
 *
 */
public abstract class ACandidate implements Candidate {

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Candidate other) {
		return ComparisonChain.start().compare(other.getScore(), this.getScore()).result();
	}


}

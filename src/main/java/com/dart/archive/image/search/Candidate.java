/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

import com.google.common.collect.ComparisonChain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author massi
 *
 */
@Getter
@AllArgsConstructor
@ToString
public class Candidate implements Comparable<Candidate> {

	File image;
	
	Double score;
	
	String type;
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Candidate other) {
		return ComparisonChain.start().compare(other.getScore(), this.getScore()).result();
	}

}

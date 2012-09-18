/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

/**
 * @author massi
 *
 */
public interface Candidate extends Comparable<Candidate> {

	Double getDistance();
	
	File getImage();
}

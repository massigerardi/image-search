/**
 * 
 */
package com.dart.archive.image.search.service;

import java.io.File;
import java.util.Collection;

import com.dart.archive.image.search.Candidate;

/**
 * @author massi
 *
 */
public interface ImageSearchService {

	public static final String SEQUENCE = "SEQUENCE";
	 
	public static final String PRE_FILTERING = "PRE_FILTERING";
	
	/**
	 * searches for an image applying a given strategy
	 * 
	 * @param image
	 * @param strategy
	 * @return
	 */
	Collection<Candidate> search(File image, String strategy);
	
	/**
	 * reload the images into the searcher
	 */
	void reload();
}

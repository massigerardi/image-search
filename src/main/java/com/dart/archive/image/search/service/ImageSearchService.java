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

	/**
	 * searches for a image
	 * @param image the image file to search
	 * @return a list of similar images
	 */
	Collection<Candidate> search(File image);
	
	
	void reload();
}

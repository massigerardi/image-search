/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;
import java.util.Collection;

/**
 * @author massi
 *
 */
public interface ImageSearcher {

	Collection<Candidate> search(File image);
	
	public final static String DART_IMAGES_HOME = "dart.images.home";
}

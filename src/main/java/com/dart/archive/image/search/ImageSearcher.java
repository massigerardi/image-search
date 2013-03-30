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
	
	void reload();
}

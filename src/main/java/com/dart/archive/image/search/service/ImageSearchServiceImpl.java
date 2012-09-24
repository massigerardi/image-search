/**
 * 
 */
package com.dart.archive.image.search.service;

import java.io.File;
import java.util.Collection;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;

/**
 * @author massi
 *
 */
public class ImageSearchServiceImpl implements ImageSearchService {

	ImageSearcher searcher;
	
	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.service.ImageSearchService#search(java.io.File)
	 */
	public Collection<Candidate> search(File image) {
		return searcher.search(image);
	}

	/**
	 * @param searcher the searcher to set
	 */
	public void setSearcher(ImageSearcher searcher) {
		this.searcher = searcher;
	}
	
}

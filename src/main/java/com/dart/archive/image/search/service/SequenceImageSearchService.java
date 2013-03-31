/**
 * 
 */
package com.dart.archive.image.search.service;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.surf.InterestPointsSearcher;

/**
 * @author massimiliano.gerardi
 *
 */
public class SequenceImageSearchService implements ImageSearchService {

	private final Logger logger = Logger.getLogger(SequenceImageSearchService.class);

	String searchDir;
	
	InterestPointsSearcher pointsSearcher;
	
	NaiveColorImageSearcher colorSearcher;

	/**
	 * @param searchDir
	 */
	public SequenceImageSearchService(String searchDir) {
		this.searchDir = searchDir;
		this.colorSearcher = new NaiveColorImageSearcher(searchDir);
		this.pointsSearcher = new InterestPointsSearcher(searchDir);
	}

	/**
	 * @param pointsSearcher
	 * @param colorSearcher
	 */
	public SequenceImageSearchService(
			InterestPointsSearcher pointsSearcher,
			NaiveColorImageSearcher colorSearcher) {
		this.pointsSearcher = pointsSearcher;
		this.colorSearcher = colorSearcher;
	}

	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.service.ImageSearchService#search(java.io.File)
	 */
	@Override
	public Collection<Candidate> search(File image) {
		long start = System.currentTimeMillis();
		try {
			Collection<Candidate> candidates = pointsSearcher.search(image);
			if (candidates.isEmpty()) {
				candidates = colorSearcher.search(image);
			}
			return candidates;
		} finally {
			debug("search for "+image.getName()+" took "+(System.currentTimeMillis()-start)+"ms");
		}
	}

	private void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

}

/**
 * 
 */
package com.dart.archive.image.search.service;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.surf.ImageInterestPoints;
import com.dart.archive.image.search.surf.InterestPointsSearcher;

/**
 * @author massi
 *
 */
public class PreFilteringImageSearchServiceImpl extends AImageSearchService implements ImageSearchService {

	private final Logger logger = Logger.getLogger(PreFilteringImageSearchServiceImpl.class);

	/**
	 * @param searchDir
	 */
	public PreFilteringImageSearchServiceImpl(String searchDir) {
		super(searchDir);
	}

	/**
	 * @param pointsSearcher
	 * @param colorSearcher
	 */
	public PreFilteringImageSearchServiceImpl(
			InterestPointsSearcher pointsSearcher,
			NaiveColorImageSearcher colorSearcher) {
		super(pointsSearcher, colorSearcher);
	}

	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.service.ImageSearchService#search(java.io.File)
	 */
	public Collection<Candidate> search(File image) {
		long start = System.currentTimeMillis();
		try {
			Collection<Candidate> candidates = colorSearcher.search(image);
			if (candidates.isEmpty()) {
				return candidates;
			}
			debug(image.getName()+":"+candidates);
			Collection<Candidate> newCandidates = pointsSearcher.search(image, getSubset(candidates));
			if (newCandidates.isEmpty()) {
				return candidates;
			}
			debug(image.getName()+":"+newCandidates);
			return newCandidates;
		} finally {
			debug("search for "+image.getName()+" took "+(System.currentTimeMillis()-start)+"ms");
		}
	}

	private Collection<ImageInterestPoints> getSubset(
			Collection<Candidate> candidates) {
		Set<ImageInterestPoints> imageInterestPoints = new TreeSet<ImageInterestPoints>();
		for (Candidate candidate : candidates) {
			String path = candidate.getImage().getAbsolutePath();
			if (pointsSearcher.getImagePoints().containsKey(path)) {
				imageInterestPoints.add(pointsSearcher.getImagePoints().get(path));
			}
		}
		return imageInterestPoints;
	}

	private void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

}

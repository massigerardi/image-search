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
public class PreFilteringImageSearchServiceImpl implements ImageSearchService {

	private final Logger logger = Logger.getLogger(PreFilteringImageSearchServiceImpl.class);

	String searchDir;
	
	InterestPointsSearcher pointsSearcher;
	
	NaiveColorImageSearcher colorSearcher;
	
	/**
	 * @param searchDir
	 */
	public PreFilteringImageSearchServiceImpl(String searchDir) {
		this.searchDir = searchDir;
		this.colorSearcher = new NaiveColorImageSearcher(searchDir);
		this.pointsSearcher = new InterestPointsSearcher(searchDir);
	}

	/**
	 * @param pointsSearcher
	 * @param colorSearcher
	 */
	public PreFilteringImageSearchServiceImpl(
			InterestPointsSearcher pointsSearcher,
			NaiveColorImageSearcher colorSearcher) {
		this.pointsSearcher = pointsSearcher;
		this.colorSearcher = colorSearcher;
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
			System.out.println(image.getName()+":"+candidates);
			Set<ImageInterestPoints> imageInterestPoints = new TreeSet<ImageInterestPoints>();
			for (Candidate candidate : candidates) {
				String path = candidate.getImage().getAbsolutePath();
				if (pointsSearcher.getImagePoints().containsKey(path)) {
					imageInterestPoints.add(pointsSearcher.getImagePoints().get(path));
				}
			}
			System.out.println(image.getName()+":"+imageInterestPoints);
			Collection<Candidate> newCandidates = pointsSearcher.search(image, imageInterestPoints);
			if (newCandidates.isEmpty()) {
				return candidates;
			}
			System.out.println(image.getName()+":"+newCandidates);
			return newCandidates;
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

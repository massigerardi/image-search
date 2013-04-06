/**
 * 
 */
package com.dart.archive.image.search.service;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.surf.ImageInterestPoints;
import com.dart.archive.image.search.surf.InterestPointsSearcher;
import com.google.common.base.Objects;

/**
 * @author massi
 *
 */
public class ImageSearchServiceImpl implements ImageSearchService {

	private final Logger logger = Logger.getLogger(ImageSearchServiceImpl.class);

	InterestPointsSearcher pointsSearcher;
	
	NaiveColorImageSearcher colorSearcher;
	
	/**
	 * @param searchDir
	 */
	public ImageSearchServiceImpl(String searchDir) {
		this.colorSearcher = new NaiveColorImageSearcher(searchDir);
		this.pointsSearcher = new InterestPointsSearcher(searchDir);

	}

	/**
	 * @param pointsSearcher
	 * @param colorSearcher
	 */
	public ImageSearchServiceImpl(InterestPointsSearcher pointsSearcher,
			NaiveColorImageSearcher colorSearcher) {
		this.pointsSearcher = pointsSearcher;
		this.colorSearcher = colorSearcher;
	}

	private Executor executor = Executors.newFixedThreadPool(5);

	class ReloadJob implements Runnable {
		
		ImageSearcher searcher;
		public ReloadJob(ImageSearcher searcher) {
			this.searcher = searcher;
		}
		public void run() {
			logger.debug("reloading "+searcher);
			searcher.reload();
		}
		
	}

	@Override
	public void reload() {
		executor.execute(new ReloadJob(colorSearcher));
		executor.execute(new ReloadJob(pointsSearcher));
	}

	private void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
				.add("\nColor Searcher", colorSearcher)
				.add("\nInterest Points Searcher", pointsSearcher)
				.toString();
	}
	
	public Collection<Candidate> sequenceSearch(File image) {
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

	/* (non-Javadoc)
	 * @see com.dart.archive.image.search.service.ImageSearchService#search(java.io.File)
	 */
	public Collection<Candidate> preFilteringSearch(File image) {
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

	@Override
	public Collection<Candidate> search(File image, String strategy) {
		if (strategy.equals(PRE_FILTERING)) {
			return preFilteringSearch(image);
		} else if (strategy.equals(SEQUENCE)) {
			return sequenceSearch(image);
		}
		throw new IllegalArgumentException(strategy);
	}

	
}

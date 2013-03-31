/**
 * 
 */
package com.dart.archive.image.search.service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.surf.InterestPointsSearcher;
import com.google.common.base.Objects;

/**
 * @author massimiliano.gerardi
 *
 */
public abstract class AImageSearchService implements ImageSearchService {

	private final Logger logger = Logger.getLogger(AImageSearchService.class);

	InterestPointsSearcher pointsSearcher;
	
	NaiveColorImageSearcher colorSearcher;
	
	/**
	 * @param searchDir
	 */
	public AImageSearchService(String searchDir) {
		this.colorSearcher = new NaiveColorImageSearcher(searchDir);
		this.pointsSearcher = new InterestPointsSearcher(searchDir);

	}

	/**
	 * @param pointsSearcher
	 * @param colorSearcher
	 */
	public AImageSearchService(InterestPointsSearcher pointsSearcher,
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

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
				.add("\nColor Searcher", colorSearcher)
				.add("\nInterest Points Searcher", pointsSearcher)
				.toString();
	}
	
	

}

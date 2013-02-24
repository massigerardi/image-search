/**
 * 
 */
package com.dart.archive.image.search.surf;

import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.dart.archive.image.search.AImageSearcher;
import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.CandidateImpl;
import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Matcher;

/**
 * @author massi
 *
 */
public class InterestPointsSearcher extends AImageSearcher {

	Logger logger = Logger.getLogger(InterestPointsSearcher.class);
	
	DecimalFormat twoDForm = new DecimalFormat("#.##");
	
	List<ImageInterestPoints> imagePointsList = new ArrayList<ImageInterestPoints>();
	
	private Cache interestPoints = CacheManager.getInstance().getCache("interestPoints");
	
	/**
	 * @return the imagePointsList
	 */
	public List<ImageInterestPoints> getImagePointsList() {
		return imagePointsList;
	}

	InterestPointsFinder finder = new InterestPointsFinder();
	
	Opener opener = new Opener();
	
	protected void search(Collection<Candidate> candidates, File file) {
		logger.debug("searching..." + file.getName());
		long start = System.currentTimeMillis();
		List<InterestPoint> points = findInterestPoints(file);
		try {
			for (ImageInterestPoints imagePoints : imagePointsList) {
				double distance = calculateDistance(points, imagePoints.getPoints());
				if (distance>0.1d) {
					if(logger.isDebugEnabled()) {
						logger.debug("candidate: " + imagePoints.getImage().getName() + " result: " + distance);					
					}
					candidates.add(new CandidateImpl(distance, imagePoints.getImage()));
					if (distance>0.60d) {
						break;
					}
				}
			}
		} finally {
			long end = System.currentTimeMillis();
			logger.debug("search in "+imagePointsList.size()+" images took "+(end -  start)+" ms");
		}
		
	}

	private double calculateDistance(List<InterestPoint> points, List<InterestPoint> currentPoints) {
		Map<InterestPoint, InterestPoint> matchedPoints = Matcher.findMathes(points, currentPoints, false);
		double distance = ((double)matchedPoints.size()/(double)points.size());
		return (double)Math.round(distance * 100) / 100;
	}

	double threshold = 0.25d;
	public double getThreshold() {return threshold;}
	public void setThreshold(double threshold) {this.threshold = threshold;}

	String sources;
	
	public InterestPointsSearcher(String sources) {
		this.sources = new File(sources).getAbsolutePath();
		init();
	}

	@Override
	public String toString() {
		return "InterestPointsSearcher{ sources:"+sources+" }";
	}

	private void init() {
		System.setProperty("net.sf.ehcache.enableShutdownHook","true");
		try {
			Collection<File> files = FileUtils.listFiles(new File(sources), new String[] {"jpg", "jpeg"}, true);
			for (File file : files) {
				addImageInterestPoints(file);
			}
		} finally {
			CacheManager.getInstance().shutdown();			
		}
	}
	private void addImageInterestPoints(File file) {
		
		ImageInterestPoints imageInterestPoints = null;
		Element element = interestPoints.get(file.getName());
		if(element != null) {
			imageInterestPoints = (ImageInterestPoints)element.getObjectValue();
		} else {
			List<InterestPoint> points = findInterestPoints(file);
			imageInterestPoints = new ImageInterestPoints(file, points);			
			interestPoints.put(new Element(file.getName(), imageInterestPoints));
			interestPoints.flush();
		}
		imagePointsList.add(imageInterestPoints);
	}
	

	private List<InterestPoint> findInterestPoints(File file) {
		logger.debug("findInterestPoints start ...");
		long start = System.currentTimeMillis();
		try {
			ImagePlus image = opener.openImage(file.getAbsolutePath());
			return finder.findInterestingPoints(image.getProcessor());
		} finally {
			long end = System.currentTimeMillis();
			logger.debug("findInterestPoints took "+(end -  start)+" ms");
		}
	}
	
}

/**
 * 
 */
package com.dart.archive.image.search.surf;

import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Matcher;
import com.dart.archive.image.utils.ImageHelper;
import com.dart.archive.image.utils.ImageUtils;
import com.google.common.base.Objects;

/**
 * @author massi
 *
 */
@Getter
@Slf4j
public class InterestPointsSearcher implements ImageSearcher {

	private static final String SURF = "SURF";

	private ImageHelper imageHelper = new ImageHelper();
	
	private final Map<String, ImageInterestPoints> imagePoints = new TreeMap<String, ImageInterestPoints>();
	
	private Cache interestPoints;

	private final InterestPointsFinder finder = new InterestPointsFinder();
	
	private final Opener opener = new Opener();
	
	private static boolean useCache = true;

	public Collection<Candidate> search(File file) {
		return search(file, imagePoints.values());
	}
	
	public Collection<Candidate> search(File file, Collection<ImageInterestPoints> imagePointsList) {
		Collection<Candidate> candidates = new TreeSet<Candidate>();
		log.debug("searching..." + file.getName());
		final long start = System.currentTimeMillis();
		List<InterestPoint> points = resizeAndFindInterestPoints(file);
		try {
			for (ImageInterestPoints imagePoints : imagePointsList) {
				final double distance = calculateDistance(points, imagePoints.getPoints());
				if (distance>0.1d) {
					log.debug("candidate: " + imagePoints.getImage().getName() + " result: " + distance);					
					candidates.add(new Candidate(imagePoints.getImage(), distance, SURF));
					if (distance>0.60d) {
						break;
					}
				}
			}
		} finally {
			long end = System.currentTimeMillis();
			log.debug("search in "+imagePointsList.size()+" images took "+(end -  start)+" ms");
		}
		return candidates;
	}

	private double calculateDistance(final List<InterestPoint> points, final List<InterestPoint> currentPoints) {
		final Map<InterestPoint, InterestPoint> matchedPoints = Matcher.findMathes(points, currentPoints, false);
		final double distance = ((double)matchedPoints.size()/(double)points.size());
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
	
	protected void init() {
		log.debug("init "+this.toString());
		List<File> files = imageHelper.getImages(new File(sources));
		if (useCache) {
			System.setProperty("net.sf.ehcache.enableShutdownHook","true");
			interestPoints = CacheManager.getInstance().getCache("interestPoints");
			try {
				for (File file : files) {
					loadImageInterestPointsFromCache(file);
				}
			} finally {
				CacheManager.getInstance().shutdown();			
			}
		} else {
			for (File file : files) {
				List<InterestPoint> points = resizeAndFindInterestPoints(file);
				imagePoints.put(file.getAbsolutePath(), new ImageInterestPoints(file, points));
			}
		}
		log.debug("init done "+this.toString());
	}
	
	private void loadImageInterestPointsFromCache(File file) {
		ImageInterestPoints imageInterestPoints = null;
		Element element = interestPoints.get(file.getAbsolutePath());
		if(element != null) {
			log.debug("file {} was found in cache", file.getAbsolutePath());
			imageInterestPoints = (ImageInterestPoints)element.getObjectValue();
		} else {
			log.debug("file {} was NOT found in cache", file.getAbsolutePath());
			List<InterestPoint> points = resizeAndFindInterestPoints(file);
			imageInterestPoints = new ImageInterestPoints(file, points);			
			interestPoints.put(new Element(file.getAbsolutePath(), imageInterestPoints));
			interestPoints.flush();
		}
		imagePoints.put(file.getAbsolutePath(), imageInterestPoints);
	}
	
	private List<InterestPoint> resizeAndFindInterestPoints(File file) {
		File dest = new File(file.getParentFile(), UUID.randomUUID()+"."+FilenameUtils.getExtension(file.getName()));
		try {
			ImageUtils.resizeAndSave(600, file.getAbsolutePath(), dest.getAbsolutePath());
		} catch (IOException e) {
			log.error("resizing", e);
			return findInterestPoints(file);
		}
		try {
			return findInterestPoints(dest);
		} finally {
			try {
				FileUtils.forceDelete(dest);
			} catch (IOException e) {
				log.error("deleting "+dest, e);
			}
		}
	}

	private List<InterestPoint> findInterestPoints(File file) {
		log.debug("findInterestPoints start ... "+file.getName());
		long start = System.currentTimeMillis();
		try {
			ImagePlus image = opener.openImage(file.getAbsolutePath());
			return finder.findInterestingPoints(image.getProcessor());
		} finally {
			long end = System.currentTimeMillis();
			log.debug("findInterestPoints "+file.length()+" took "+(end -  start)+" ms");
		}
	}

	public static void setUseCache(boolean b) {
		useCache = b;
	}

	@Override
	public String toString() {
		return 	Objects.toStringHelper(this.getClass())
				.add("sources", sources)
				.add("images", imagePoints.size()).toString();
	}

	@Override
	public void reload() {
		init();
	}
}

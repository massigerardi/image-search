/**
 * 
 */
package com.dart.archive.image.search.surf;

import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import lombok.ToString;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import com.dart.archive.image.search.AImageSearcher;
import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.CandidateImpl;
import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Matcher;
import com.dart.archive.image.utils.ImageHelper;
import com.dart.archive.image.utils.ImageUtils;
import com.google.common.base.Objects;

/**
 * @author massi
 *
 */
public class InterestPointsSearcher extends AImageSearcher {

	private final Logger logger = Logger.getLogger(InterestPointsSearcher.class);
	
	private final DecimalFormat twoDForm = new DecimalFormat("#.##");

	private ImageHelper imageHelper = new ImageHelper();
	
	private final Set<ImageInterestPoints> imagePointsList = new TreeSet<ImageInterestPoints>();
	
	private Cache interestPoints;

	private final InterestPointsFinder finder = new InterestPointsFinder();
	
	private final Opener opener = new Opener();
	
	private boolean useCache = true;
	
	protected void search(Collection<Candidate> candidates, File file) {
		debug("searching..." + file.getName());
		final long start = System.currentTimeMillis();
		List<InterestPoint> points = resizeAndFindInterestPoints(file);
		try {
			for (ImageInterestPoints imagePoints : imagePointsList) {
				final double distance = calculateDistance(points, imagePoints.getPoints());
				if (distance>0.1d) {
					debug("candidate: " + imagePoints.getImage().getName() + " result: " + distance);					
					candidates.add(new CandidateImpl(distance, imagePoints.getImage()));
					if (distance>0.60d) {
						break;
					}
				}
			}
		} finally {
			long end = System.currentTimeMillis();
			debug("search in "+imagePointsList.size()+" images took "+(end -  start)+" ms");
		}
		
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
	
	private void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	protected void init() {
		debug("init "+this.toString());
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
				imagePointsList.add(new ImageInterestPoints(file, points));
			}
		}
		debug("init done "+this.toString());
	}
	
	private void loadImageInterestPointsFromCache(File file) {
		ImageInterestPoints imageInterestPoints = null;
		String key = getKey(file);
		Element element = interestPoints.get(key);
		if(element != null) {
			debug("element "+ key +" was found in cache");
			imageInterestPoints = (ImageInterestPoints)element.getObjectValue();
		} else {
			debug("element "+ key +" was NOT found in cache");
			List<InterestPoint> points = resizeAndFindInterestPoints(file);
			imageInterestPoints = new ImageInterestPoints(file, points);			
			interestPoints.put(new Element(key, imageInterestPoints));
			interestPoints.flush();
		}
		imagePointsList.add(imageInterestPoints);
	}
	
	private String getKey(File file) {
		return String.valueOf(new HashCodeBuilder()
			.append(file.getParent())
			.append(file.getName())
			.append(file.length())
			.toHashCode());
	}

	private List<InterestPoint> resizeAndFindInterestPoints(File file) {
		File dest = new File(file.getParentFile(), UUID.randomUUID()+"."+FilenameUtils.getExtension(file.getName()));
		try {
			ImageUtils.resizeAndSave(600, file.getAbsolutePath(), dest.getAbsolutePath());
		} catch (IOException e) {
			logger.error("resizing", e);
			return findInterestPoints(file);
		}
		try {
			return findInterestPoints(dest);
		} finally {
			try {
				FileUtils.forceDelete(dest);
			} catch (IOException e) {
				logger.error("deleting "+dest, e);
			}
		}
	}

	private List<InterestPoint> findInterestPoints(File file) {
		debug("findInterestPoints start ... "+file.getName());
		long start = System.currentTimeMillis();
		try {
			ImagePlus image = opener.openImage(file.getAbsolutePath());
			return finder.findInterestingPoints(image.getProcessor());
		} finally {
			long end = System.currentTimeMillis();
			debug("findInterestPoints "+file.length()+" took "+(end -  start)+" ms");
		}
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	@Override
	public String toString() {
		return 	Objects.toStringHelper(this.getClass())
				.add("sources", sources)
				.add("images", imagePointsList.size()).toString();
	}
}

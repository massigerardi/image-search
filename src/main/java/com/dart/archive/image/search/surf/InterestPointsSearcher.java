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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dart.archive.image.search.AImageSearcher;
import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.dart.archive.image.search.surf.ip.Matcher;

/**
 * @author massi
 *
 */
public class InterestPointsSearcher extends AImageSearcher {

	DecimalFormat twoDForm = new DecimalFormat("#.##");
	
	List<ImageInterestPoints> imagePointsList = new ArrayList<ImageInterestPoints>();
	
	private static final Logger logger = LoggerFactory.getLogger(InterestPointsSearcher.class);


	/**
	 * @return the imagePointsList
	 */
	public List<ImageInterestPoints> getImagePointsList() {
		return imagePointsList;
	}

	InterestPointsFinder finder = new InterestPointsFinder();
	
	Opener opener = new Opener();
	
	protected void search(Collection<Candidate> candidates, File file) {
		
		List<InterestPoint> points = findInterestPoints(file);
		for (ImageInterestPoints imagePoints : imagePointsList) {
			List<InterestPoint> currentPoints = imagePoints.getPoints();
			Map<InterestPoint, InterestPoint> matchedPointsDirect = Matcher.findMathes(points, currentPoints);
			Map<InterestPoint, InterestPoint> matchedPointsReverse = Matcher.findMathes(currentPoints, points);
			Map<InterestPoint, InterestPoint> matchedPoints = intersection(matchedPointsDirect, matchedPointsReverse);
			
			if (matchedPoints.size() > 5) {
//				InterestingPointsUtils.displayInterestingPoints(matchedPoints, file, points, imagePoints.getImage(), imagePoints.getPoints());
				double distance = ((double)matchedPoints.size()/(double)points.size());
				double result = (double)Math.round(distance * 100) / 100;
				candidates.add(new ImageSurfCandidate(imagePoints.getImage(), result, matchedPoints.size()));
			}
		}
	}

	
	String sources;

	public InterestPointsSearcher(String imageHome, String sources) {
		this.sources = imageHome +  sources;
		init();
	}

	private void init() {
		long now = System.currentTimeMillis();
		Collection<File> files = FileUtils.listFiles(new File(sources), new String[] {"jpg", "jpeg"}, true);
		long listfiles = (System.currentTimeMillis() - now) / 1000;
		logger.info("list files has taken: " + listfiles  + " secs");
		for (File file : files) {
			List<InterestPoint> points = findInterestPoints(file);
			imagePointsList.add(new ImageInterestPoints(file, points));
		}
		logger.info("Create ImageInterestPoints has taken: " + (System.currentTimeMillis() - listfiles) / 1000  + " secs");		
	}

	private List<InterestPoint> findInterestPoints(File file) {
		ImagePlus image = opener.openImage(file.getAbsolutePath());
		return finder.findInterestingPoints(image.getProcessor());
	}

	private Map<InterestPoint, InterestPoint> intersection(Map<InterestPoint, InterestPoint> map1, Map<InterestPoint, InterestPoint> map2) {
		// take only those points that matched in the reverse comparison too
		Map<InterestPoint, InterestPoint> result = new HashMap<InterestPoint, InterestPoint>();
		for (InterestPoint ipt1 : map1.keySet()) {
			InterestPoint ipt2 = map1.get(ipt1);
			if (ipt1 == map2.get(ipt2))
				result.put(ipt1, ipt2);
		}
		return result;
	}
	
}

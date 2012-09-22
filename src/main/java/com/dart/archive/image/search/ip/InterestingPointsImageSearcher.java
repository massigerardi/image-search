/**
 * 
 */
package com.dart.archive.image.search.ip;

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

import com.dart.archive.image.search.AImageSearcher;
import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.CandidateImpl;
import com.dart.archive.image.search.ip.surf.InterestPoint;
import com.dart.archive.image.search.ip.surf.Matcher;

/**
 * @author massi
 *
 */
public class InterestingPointsImageSearcher extends AImageSearcher {

	DecimalFormat twoDForm = new DecimalFormat("#.##");
	
	List<ImagePoints> imagePointsList = new ArrayList<ImagePoints>();
	
	/**
	 * @return the imagePointsList
	 */
	public List<ImagePoints> getImagePointsList() {
		return imagePointsList;
	}

	InterestingPointsFinder finder = new InterestingPointsFinder();
	
	Opener opener = new Opener();
	
	@Override
	protected void populateCandidate(Collection<Candidate> candidates, File file) {
		
		List<InterestPoint> points = findInterestPoints(file);
		System.out.print(points.size()+" ");
//		System.out.println("found "+points.size()+" IPs for "+file.getAbsolutePath());
		for (ImagePoints imagePoints : imagePointsList) {
			List<InterestPoint> currentPoints = imagePoints.getPoints();
			Map<InterestPoint, InterestPoint> matchedPointsDirect = Matcher.findMathes(points, currentPoints);
			Map<InterestPoint, InterestPoint> matchedPointsReverse = Matcher.findMathes(currentPoints, points);
			Map<InterestPoint, InterestPoint> matchedPoints = intersection(matchedPointsDirect, matchedPointsReverse);
			
			if (matchedPoints.size() > 5) {
//				InterestingPointsUtils.displayInterestingPoints(matchedPoints, file, points, imagePoints.getImage(), imagePoints.getPoints());
				double distance = ((double)matchedPoints.size()/(double)points.size());
				double result = (double)Math.round(distance * 100) / 100;
				candidates.add(new IPCandidate(imagePoints.getImage(), result, matchedPoints.size()));
			}
		}
	}


	String sources;

	public InterestingPointsImageSearcher(String sources) {
		this.sources = sources;
		init();
	}

	private void init() {
		Collection<File> files = FileUtils.listFiles(new File(sources), new String[] {"jpg", "jpeg"}, true);
		for (File file : files) {
			List<InterestPoint> points = findInterestPoints(file);
			System.out.println("for "+file.getAbsolutePath()+" found "+points.size()+" IPs");
			imagePointsList.add(new ImagePoints(file, points));
		}
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

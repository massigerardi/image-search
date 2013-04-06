/**
 * 
 */
package com.dart.archive.image.search.service;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcherTest;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.surf.InterestPointsSearcher;

/**
 * @author massi
 *
 */
public class ImageSearchServiceImplTest extends ImageSearcherTest {

	private final Logger logger = Logger.getLogger(ImageSearchServiceImplTest.class);

	ImageSearchServiceImpl searchService;
	InterestPointsSearcher pointsSearcher;
	NaiveColorImageSearcher colorImageSearcher;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		pointsSearcher = new InterestPointsSearcher(imagesFolder);
		colorImageSearcher = new NaiveColorImageSearcher(imagesFolder);
		searchService = new ImageSearchServiceImpl(pointsSearcher, colorImageSearcher);
	}
	
	
	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testFilteringSearch() {
		testSearch(ImageSearchService.PRE_FILTERING);
	}
	
	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testSequenceSearch() {
		testSearch(ImageSearchService.SEQUENCE);
	}

	private void testSearch(String strategy) {
		logger.debug("test search("+strategy+")");
		File image = new File(testFolder, "DG4X0099.jpg");
		testSearch(image, strategy);
		image = new File(testFolder, "elena-1.jpg");
		testSearch(image, strategy);
		image = new File(testFolder, "lim-001-000.ppm");
		testSearch(image, strategy);
	}

	private void testSearch(File image,	String strategy) {
		Collection<Candidate> candidates = searchService.search(image, strategy);
		assertFalse("image "+image.getName(), candidates.isEmpty());
	}
	
}

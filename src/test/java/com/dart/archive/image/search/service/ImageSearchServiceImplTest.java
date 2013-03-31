/**
 * 
 */
package com.dart.archive.image.search.service;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Collection;

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

	PreFilteringImageSearchServiceImpl filteringSearchService;
	SequenceImageSearchService sequenceSearchService;
	InterestPointsSearcher pointsSearcher;
	NaiveColorImageSearcher colorImageSearcher;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		pointsSearcher = new InterestPointsSearcher(imagesFolder);
		colorImageSearcher = new NaiveColorImageSearcher(imagesFolder);
		filteringSearchService = new PreFilteringImageSearchServiceImpl(pointsSearcher, colorImageSearcher);
		sequenceSearchService = new SequenceImageSearchService(pointsSearcher, colorImageSearcher);
	}
	
	
	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testFilteringSearch() {
		testSearch(filteringSearchService);
	}
	
	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testSequenceSearch() {
		testSearch(sequenceSearchService);
	}

	private void testSearch(ImageSearchService searchService) {
		File image = new File(testFolder, "DG4X0099.jpg");
		testSearch(image, searchService);
		image = new File(testFolder, "elena-1.jpg");
		testSearch(image, searchService);
		image = new File(testFolder, "lim-001-000.ppm");
		testSearch(image, searchService);
	}

	private void testSearch(File image,
			ImageSearchService searchService) {
		Collection<Candidate> candidates = searchService.search(image);
		assertFalse("image "+image.getName(), candidates.isEmpty());
	}
	
}

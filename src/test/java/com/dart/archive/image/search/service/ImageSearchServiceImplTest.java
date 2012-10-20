/**
 * 
 */
package com.dart.archive.image.search.service;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.search.ImageSearcherTest;
import com.dart.archive.image.search.color.NaiveColorImageSearcher;
import com.dart.archive.image.search.hash.PHashImageSearcher;
import com.dart.archive.image.search.surf.InterestPointsSearcher;

/**
 * @author massi
 *
 */
public class ImageSearchServiceImplTest extends ImageSearcherTest {


	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testSearchByColor() {
		ImageSearchService service = new ImageSearchServiceImpl();
		ImageSearcher searcher = new NaiveColorImageSearcher("", imagesFolder);
		service.setSearcher(searcher);
		doTest(service);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testSearchByHash() {
		ImageSearchService service = new ImageSearchServiceImpl();
		ImageSearcher searcher = new PHashImageSearcher(imagesFolder);
		service.setSearcher(searcher);
		doTest(service);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.service.ImageSearchServiceImpl#search(java.io.File)}.
	 */
	@Test
	public void testSearchByInterestingPoints() {
		ImageSearchService service = new ImageSearchServiceImpl();
		ImageSearcher searcher = new InterestPointsSearcher("", imagesFolder);
		service.setSearcher(searcher);
		doTest(service);
	}

	private void doTest(ImageSearchService service) {
		File testImages = new File(testFolder);
		Collection<File> images = FileUtils.listFiles(testImages, new String[] {"jpg"}, true);
		for (File image : images) {
			Collection<Candidate> candidates = service.search(image);
			assertFalse(candidates.isEmpty());
		}
	}
	
}

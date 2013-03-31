/**
 * 
 */
package com.dart.archive.image.search.ip;

import java.io.IOException;

import org.junit.Test;

import com.dart.archive.image.search.ImageSearcherTest;
import com.dart.archive.image.search.surf.InterestPointsSearcher;

/**
 * @author massi
 *
 */
public class InterestPointsSearcherTest extends ImageSearcherTest{
	
	@Test
	public void testCompare() throws IOException {
		InterestPointsSearcher.setUseCache(false);
		InterestPointsSearcher searcher = new InterestPointsSearcher(imagesFolder);
		setSearcher(searcher);
		compare(0.1d);
	}
	

}

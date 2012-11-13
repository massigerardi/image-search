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
		setSearcher(new InterestPointsSearcher(imagesFolder));
		compare();
	}
	

}

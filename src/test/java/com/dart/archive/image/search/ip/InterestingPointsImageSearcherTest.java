/**
 * 
 */
package com.dart.archive.image.search.ip;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.dart.archive.image.search.ImageSearcherTest;

/**
 * @author massi
 *
 */
public class InterestingPointsImageSearcherTest extends ImageSearcherTest{

	@Test
	public void testCompare() throws IOException {
		setSearcher(new InterestingPointsImageSearcher(imagesFolder));
		compare();
	}
	

}

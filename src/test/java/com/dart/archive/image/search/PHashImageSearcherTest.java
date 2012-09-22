/**
 * 
 */
package com.dart.archive.image.search;

import java.io.IOException;

import org.junit.Test;

import com.dart.archive.image.search.hash.PHashImageSearcher;

/**
 * @author massi
 *
 */
public class PHashImageSearcherTest extends ImageSearcherTest {

	/**
	 * Test method for {@link com.dart.archive.image.search.NaiveColorImageSearcher#compare(java.awt.image.RenderedImage)}.
	 * @throws IOException 
	 */
	@Test
	public void testCompare() throws IOException {
		long start = System.currentTimeMillis();
		setSearcher(new PHashImageSearcher(imagesFolder));
		System.out.println("loaded images in "+(System.currentTimeMillis()-start)+"ms");
		compare();
	}

}

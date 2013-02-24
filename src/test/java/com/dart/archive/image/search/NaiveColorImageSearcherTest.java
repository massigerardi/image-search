/**
 * 
 */
package com.dart.archive.image.search;

import java.io.IOException;

import org.junit.Test;

import com.dart.archive.image.search.color.NaiveColorImageSearcher;

/**
 * @author massi
 *
 */
public class NaiveColorImageSearcherTest extends ImageSearcherTest {

	static {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.color.NaiveColorImageSearcher#search(java.awt.image.RenderedImage)}.
	 * @throws IOException 
	 */
	@Test
	public void testCompare() throws IOException {
		setSearcher(new NaiveColorImageSearcher(imagesFolder, 5, 60));
		compare(0);
	}

}

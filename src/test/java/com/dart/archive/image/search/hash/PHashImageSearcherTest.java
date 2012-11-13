/**
 * 
 */
package com.dart.archive.image.search.hash;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.dart.archive.image.search.ImageSearcherTest;
import com.dart.archive.image.search.hash.PHashImageSearcher;

/**
 * @author massi
 *
 */
public class PHashImageSearcherTest extends ImageSearcherTest {

	/**
	 * Test method for {@link com.dart.archive.image.search.color.NaiveColorImageSearcher#search(java.awt.image.RenderedImage)}.
	 * @throws IOException 
	 */
	@Test
	public void testCompare() throws IOException {
		setSearcher(new PHashImageSearcher(new File(imagesFolder).getAbsolutePath()));
		compare();
	}

}

/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author massi
 *
 */
public class NaiveColorImageSearcherTest extends ImageSearcherTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.NaiveColorImageSearcher#compare(java.awt.image.RenderedImage)}.
	 * @throws IOException 
	 */
	@Test
	public void testCompare() throws IOException {
		long start = System.currentTimeMillis();
		setSearcher(new NaiveColorImageSearcher(imagesFolder, 5, 60));
		System.out.println("loaded images in "+(System.currentTimeMillis()-start)+"ms");
		compare();
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.NaiveColorImageSearcher#compare(java.awt.image.RenderedImage)}.
	 */
	@Test
	public void testCompare1050() throws IOException {
		long start = System.currentTimeMillis();
		setSearcher(new NaiveColorImageSearcher(imagesFolder, 10, 50));
		System.out.println("loaded images in "+(System.currentTimeMillis()-start)+"ms");
		compare();
	}

}

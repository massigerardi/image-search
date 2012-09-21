/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

/**
 * @author massi
 *
 */
public class NaiveColorImageSearcherTest extends ImageSearcherTest {

	static {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
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
		Collection<File> files = FileUtils.listFiles(new File(imagesFolder), new String[] {"jpg", "jpeg"}, true);
		long start = System.currentTimeMillis();
		setSearcher(new NaiveColorImageSearcher(imagesFolder, 5, 60));
		System.out.println("loaded "+files.size()+"images in "+(System.currentTimeMillis()-start)+"ms");
		compare();
	}

}

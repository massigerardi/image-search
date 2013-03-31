/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;

import com.dart.archive.image.utils.ImageHelper;

/**
 * @author massi
 *
 */
public abstract class ImageSearcherTest {

	protected String imagesFolder;
	
	protected String testFolder;
	
	ImageSearcher searcher;

	public void setSearcher(ImageSearcher searcher) {
		this.searcher = searcher;
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream("src/test/resources/test.properties"));
		imagesFolder = System.getProperty("images");
		if (StringUtils.isEmpty(imagesFolder)) {
			imagesFolder = properties.getProperty("images");
		}
		testFolder = System.getProperty("search-images");
		if (StringUtils.isEmpty(testFolder)) {
			testFolder = properties.getProperty("search");
		}
	}

	protected void compare(double treshold) throws IOException {
		System.out.println("comparing with "+searcher.toString());
		File testImages = new File(testFolder);
		List<File> images = new ImageHelper().getImages(testImages);
		for (File image : images) {
			compare(image, treshold);
		}
		System.out.println();
	}
	
	protected void compare(File file, double treshold) throws IOException {
		long start = System.currentTimeMillis();
		Collection<Candidate> candidates = searcher.search(file);
		float end = (System.currentTimeMillis()-start);
		int counter = 1;
		String time = "["+end+"ms]";
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.rightPad(time, 11));
		buffer.append(StringUtils.rightPad(file.getName(), 40));
		buffer.append(":");
		double score = 1.0d;
		for (Candidate candidate : candidates) {
			assertTrue(candidate.getScore()+">"+score, candidate.getScore()<=score);
			assertFalse("", candidate.getScore()<treshold);
			if (counter<6) {
				buffer.append(candidate);
				buffer.append(", ");
			}
			counter++;
			score = candidate.getScore();
		}
		buffer.append("... ");
		System.out.println(buffer);
	}

}

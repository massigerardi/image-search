/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;

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
		Collection<File> images = FileUtils.listFiles(testImages, new String[] {"jpg", "JPG"}, true);
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
		for (Candidate candidate : candidates) {
			assertFalse("", candidate.getScore()<treshold);
			if (counter<6) {
				buffer.append(candidate);
				buffer.append(", ");
			}
			counter++;
		}
		buffer.append("... ");
		System.out.println(buffer);
	}


	private boolean checkName(String image, String candidate) {
		String imageName = FilenameUtils.getBaseName(image);
		String candidateName = FilenameUtils.getBaseName(candidate);
		return imageName.startsWith(candidateName);
	}

}

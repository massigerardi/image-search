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
import org.apache.commons.lang.StringUtils;
import org.junit.Before;

/**
 * @author massi
 *
 */
public abstract class ImageSearcherTest {

	protected String imagesRootFolder;
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
		imagesRootFolder = properties.getProperty("imagesRoot");
		imagesFolder = properties.getProperty("images");
		testFolder   = properties.getProperty("search");
	}

	protected void compare() throws IOException {
		File testImages = new File(testFolder);
		Collection<File> images = FileUtils.listFiles(testImages, new String[] {"jpg", "JPG"}, true);
		for (File image : images) {
			compare(image);
		}
		System.out.println();
	}
	
	protected void compare(File file) throws IOException {
		long start = System.currentTimeMillis();
		Collection<Candidate> candidates = searcher.search(file);
		float end = (System.currentTimeMillis()-start);
		int counter = 1;
		int index = -1;
		boolean hasHits = false;
		String time = "["+end+"ms]";
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.rightPad(time, 11));
		buffer.append(StringUtils.rightPad(file.getName(), 40));
		buffer.append(":");
		for (Candidate candidate : candidates) {
			if (!hasHits) {
				hasHits = checkName(file.getName(), candidate.getImage().getName());
				if (hasHits) {
					index = counter;
				}
				hasHits = hasHits && index < 6;
			}
			if (counter<6) {
				buffer.append(candidate);
				buffer.append(", ");
			}
			counter++;
		}
		buffer.append("... ");
		buffer.insert(0, StringUtils.rightPad(String.valueOf(index), 5));
		System.out.println(buffer);
		assertFalse(candidates.isEmpty());
	}


	private boolean checkName(String image, String candidate) {
		String imageName = StringUtils.remove(StringUtils.remove(image.toLowerCase(), ".jpg"),".jpeg");
		String candifateName = StringUtils.remove(StringUtils.remove(candidate.toLowerCase(), ".jpg"),".jpeg");
		return imageName.startsWith(candifateName);
	}

}

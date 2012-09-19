/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.*;

import ij.ImagePlus;
import ij.io.Opener;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

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
		imagesFolder = properties.getProperty("images");
		testFolder   = properties.getProperty("search");
		
	}

	protected void compare() throws IOException {
		File testImages = new File(testFolder);
		Collection<File> images = FileUtils.listFiles(testImages, new String[] {"jpg"}, true);
		for (File image : images) {
			compare(image);
		}
		System.out.println();
	}
	
	protected void compare(File file) throws IOException {
		long start = System.currentTimeMillis();
		Collection<Candidate> candidates = searcher.compare(file);
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
		String imageName = StringUtils.remove(image, ".jpg");
		String candifateName = StringUtils.remove(candidate, ".jpg");
		return imageName.startsWith(candifateName);
	}

}

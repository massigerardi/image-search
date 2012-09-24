/**
 * 
 */
package com.dart.archive.image.search.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.dart.archive.image.search.hash.ImageHashCalculator;

/**
 * @author massi
 *
 */
public class ImageHashCalculatorTest {

	String imageA;
	String imageB;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("src/test/resources/test.properties"));
			imageA = properties.getProperty("imageA");
			imageB   = properties.getProperty("imageB");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-----------------------------------------");
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.hash.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistanceSameImage() throws FileNotFoundException, Exception {
		ImageHashCalculator imageHashCalculator = new ImageHashCalculator();
		String hashA = imageHashCalculator.getHash(new FileInputStream(new File(imageA)));
		String hashB = imageHashCalculator.getHash(new FileInputStream(new File(imageA)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imageHashCalculator.distance(hashA, hashB);
		System.out.println(distance);
		assertEquals(1.0d, distance, 0);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.hash.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistance() throws FileNotFoundException, Exception {
		ImageHashCalculator imageHashCalculator = new ImageHashCalculator();
		String hashA = imageHashCalculator.getHash(new FileInputStream(new File(imageA)));
		String hashB = imageHashCalculator.getHash(new FileInputStream(new File(imageB)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imageHashCalculator.distance(hashA, hashB);
		System.out.println(distance);
		assertTrue(distance>0.5d);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.hash.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistance6416() throws FileNotFoundException, Exception {
		ImageHashCalculator imageHashCalculator = new ImageHashCalculator(64, 8);
		String hashA = imageHashCalculator.getHash(new FileInputStream(new File(imageA)));
		String hashB = imageHashCalculator.getHash(new FileInputStream(new File(imageB)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imageHashCalculator.distance(hashA, hashB);
		assertTrue(distance>0.5d);
	}
	
}

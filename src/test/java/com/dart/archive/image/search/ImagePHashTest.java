/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author massi
 *
 */
public class ImagePHashTest {

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
	 * Test method for {@link com.dart.archive.image.search.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistanceSameImage() throws FileNotFoundException, Exception {
		ImagePHash imagePHash = new ImagePHash();
		String hashA = imagePHash.getHash(new FileInputStream(new File(imageA)));
		String hashB = imagePHash.getHash(new FileInputStream(new File(imageA)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imagePHash.distance(hashA, hashB);
		System.out.println(distance);
		assertEquals(1.0d, distance, 0);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistance() throws FileNotFoundException, Exception {
		ImagePHash imagePHash = new ImagePHash();
		String hashA = imagePHash.getHash(new FileInputStream(new File(imageA)));
		String hashB = imagePHash.getHash(new FileInputStream(new File(imageB)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imagePHash.distance(hashA, hashB);
		System.out.println(distance);
		assertEquals(0.94d, distance, 0.00d);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.ImagePHash#distance(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDistance6416() throws FileNotFoundException, Exception {
		ImagePHash imagePHash = new ImagePHash(64, 8);
		String hashA = imagePHash.getHash(new FileInputStream(new File(imageA)));
		String hashB = imagePHash.getHash(new FileInputStream(new File(imageB)));
		System.out.println("'"+hashA+"'");
		System.out.println("'"+hashB+"'");
		double distance = imagePHash.distance(hashA, hashB);
		assertEquals(0.94d, distance, 0.00d);
	}

	/**
	 * Test method for {@link com.dart.archive.image.search.ImagePHash#getHash(java.io.InputStream)}.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testGetHash() throws FileNotFoundException, Exception {
		ImagePHash imagePHash = new ImagePHash();
		String hash = imagePHash.getHash(new FileInputStream(new File(imageA)));
		System.out.println(hash);
		assertEquals("0000000011000110000000000100000000000100100000100", hash);
	}

}

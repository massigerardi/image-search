/**
 * 
 */
package com.dart.archive.image.search;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author massi
 *
 */
public class ImagePHashTest {

	String imageA = "/Users/massi/Pictures/test-images/dynys.pdf_16_146test.jpg";
	String imageB = "/Users/massi/Pictures/dart/dynys.pdf_16_146.jpg";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("-----------------------------------------");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
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
		System.out.println(distance);
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
	}

}

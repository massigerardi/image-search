/**
 * 
 */
package com.dart.archive.image.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author massimiliano.gerardi
 *
 */
public class ImageHelper {

	private String[] exts = new String[] {"jpg", "jpeg", "ppm", "PPM"};

	public List<File> getImages(File dir) {
		return new ArrayList<File>(FileUtils.listFiles(dir, exts , true));
	}
	
}

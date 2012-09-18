/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

/**
 * @author massi
 *
 */
public class ImageHash {

	String hash;
	
	File image;

	public ImageHash(String hash, File image) {
		this.hash = hash;
		this.image = image;
	}

	public String getHash() {
		return hash;
	}

	public File getImage() {
		return image;
	}
	
	
	
}

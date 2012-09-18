/**
 * 
 */
package com.dart.archive.image.search;

import java.awt.Color;
import java.io.File;

/**
 * @author massi
 *
 */
public class ImageDescriptor {
	
	Color[][] signature;
	
	File image;

	
	
	public ImageDescriptor(Color[][] signature, File image) {
		this.signature = signature;
		this.image = image;
	}

	public Color[][] getSignature() {
		return signature;
	}

	public File getImage() {
		return image;
	}
	
	

}

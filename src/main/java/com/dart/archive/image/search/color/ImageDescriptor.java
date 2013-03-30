/**
 * 
 */
package com.dart.archive.image.search.color;

import java.awt.Color;
import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author massi
 *
 */
@Getter
@AllArgsConstructor
public class ImageDescriptor {
	
	Color[][] signature;
	
	File image;
}

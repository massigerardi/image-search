/**
 * 
 */
package com.dart.archive.image.utils;

import ij.ImagePlus;
import ij.io.Opener;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author massi
 *
 */
public class ImageUtils {

	private static Opener opener = new Opener();
	
	public static void main(String[] args) throws IOException {
		if (args.length<2) {
			return;
		}
		String size = args[0];
		String[] sizes = StringUtils.split(size, "x");
		int w = Integer.valueOf(sizes[0]);
		int h = -1;
		if (sizes.length>1) {
			h = Integer.valueOf(sizes[1]);
		}
		System.out.println("size: "+w+(h<0?"":"x"+h));
		String path = args[1];
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				if (h<0) {
					resizeFolder(w, file);
				} else {
					resizeFolder(w, h, file);
				}
			} else {
				if (h<0) {
					resizeAndSave(w, path, null);
				} else {
					resizeAndSave(w, h, path, null);
				}
			}
		}
		
	}
	
	public static void resizeFolder(int width, int height, File folder) throws IOException {
		System.out.println("resizing to "+width+"x"+height+" in folder "+folder.getAbsolutePath());
		Collection<File> files = FileUtils.listFiles(folder, new String[] {"jpg", "JPG", "jpeg"}, false);
		for (File file : files) {
			resizeAndSave(width, height, file.getAbsolutePath(), createDest(file.getAbsolutePath()));
		}
	}
	
	public static void resizeFolder(int size, File folder) throws IOException {
		System.out.println("resizing to "+size+" in folder "+folder.getAbsolutePath());
		Collection<File> files = FileUtils.listFiles(folder, new String[] {"jpg", "JPG", "jpeg"}, false);
		for (File file : files) {
			resizeAndSave(size, file.getAbsolutePath(), createDest(file.getAbsolutePath()));
		}
	}
	
	private static String createDest(String absolutePath) {
		return createDest(absolutePath, null);
	}

	private static String createDest(String fileName, String dest) {
		if (dest!=null) {
			return dest;
		}
		String name = FilenameUtils.getBaseName(fileName);
		String ext = FilenameUtils.getExtension(fileName);
		String path = FilenameUtils.getFullPath(fileName);
		File newFolder = new File(path, "thumbs");
		newFolder.mkdirs();
		File newFile = new File(newFolder, name+"."+ext.toLowerCase());
		return newFile.getAbsolutePath();
	}

	private  ImageUtils() {
	}
	
	
	public static BufferedImage resize(int width, int height, String file) {
		ImagePlus image = opener.openImage(file);
		return image.getProcessor().resize(width, height).getBufferedImage();
	}
	
	public static BufferedImage resizeAndSave(int width, int height, String src, String dest) throws IOException {
		dest = createDest(src, dest);
		System.out.println("resizing to "+width+"x"+height+" file "+src+" to "+dest);
		BufferedImage image = resize(width, height, src);
		ImageIO.write(image, "jpg", new File(dest));
		return image;
	}
	
	public static BufferedImage resize(int size, String file) {
		ImagePlus image = opener.openImage(file);
		RenderedImage renderedImage = image.getBufferedImage();
		int width = size;
		int height = size;
		int w = renderedImage.getWidth();
		int h = renderedImage.getHeight();
		double ratio = (double)w/(double)h;
		if (w > h) {
			height = (int) (width * 1/ratio);
		} else {
			width = (int) (height * ratio);
		}
		System.out.println("resizing to "+width+"x"+height);
		return image.getProcessor().resize(width, height).getBufferedImage();
	}
	
	public static BufferedImage resizeAndSave(int size, String src, String dest) throws IOException {
		dest = createDest(src, dest);
		System.out.println("resizing to "+size+" file "+src+" to "+dest);
		BufferedImage image = resize(size, src);
		ImageIO.write(image, "jpg", new File(dest));
		return image;
	}
	
	
}

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
import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public class ImageUtils {

	static Logger logger = Logger.getLogger(ImageUtils.class);
	
	private static void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	private static Opener opener = new Opener();
	/**
	 * a main method to resize single image or content of folders
	 * it accept the following arguments
	 * java ImageUtils W[xH] file|folder
	 * 
	 *  where
	 *  W is the Width
	 *  H is the Height
	 *  file is the file to resize
	 *  folder is the folder to resize
	 * 
	 * if H is not used, then the image will be resized according to orientation
	 * 
	 * @param args
	 * @throws IOException
	 */
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
		debug("size: "+w+(h<0?"":"x"+h));
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

	
	/**
	 * resize the images in a folder with the exact width and height provided
	 * the proportion are not kept
	 * the new images will have the original name with the suffix -th
	 * @param width
	 * @param height
	 * @param folder the folder 
	 * @throws IOException
	 */
	public static void resizeFolder(int width, int height, File folder) throws IOException {
		debug("resizing to "+width+"x"+height+" in folder "+folder.getAbsolutePath());
		Collection<File> files = FileUtils.listFiles(folder, new String[] {"jpg", "JPG", "jpeg"}, false);
		for (File file : files) {
			resizeAndSave(width, height, file.getAbsolutePath(), createDest(file.getAbsolutePath()));
		}
	}
	
	/**
	 * resize the images in a folder with a maximum size
	 * the image will be resized according to their orientation
	 * the new size will be applied to the maximum dimension
	 * the proportion are kept
	 * @param size
	 * @param folder
	 * @throws IOException
	 */
	public static void resizeFolder(int size, File folder) throws IOException {
		debug("resizing to "+size+" in folder "+folder.getAbsolutePath());
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
	
	/**
	 * returns an image with the exact dimension provided
	 * the proportion are not kept
	 * @param width
	 * @param height
	 * @param file the file path for the image to be resized
	 * @return
	 */
	public static BufferedImage resize(int width, int height, String file) {
		ImagePlus image = opener.openImage(file);
		return image.getProcessor().resize(width, height).getBufferedImage();
	}
	
	/**
	 * returns an image with the exact dimension provided
	 * the proportion are not kept
	 * @param width
	 * @param height
	 * @param image the image to be resized
	 * @return
	 */
	public static BufferedImage resize(int width, int height, BufferedImage image) {
		ImagePlus imagep = new ImagePlus("", image );
		return imagep.getProcessor().resize(width, height).getBufferedImage();
	}
	
	/**
	 * returns an image with the exact dimension provided
	 * the proportion are not kept
	 * the image will be saved in the destination file
	 * if destination file is null, the original name with suffix -th will be used
	 * @param width
	 * @param height
	 * @param src the file path for the image to be resized
	 * @param dest the destination file
	 * @return
	 */
	public static BufferedImage resizeAndSave(int width, int height, String src, String dest) throws IOException {
		dest = createDest(src, dest);
		debug("resizing to "+width+"x"+height+" file "+src+" to "+dest);
		BufferedImage image = resize(width, height, src);
		ImageIO.write(image, "jpg", new File(dest));
		return image;
	}
	
	/**
	 * returns an image with the maximum dimension provided
	 * the image will be resized according to their orientation
	 * the new size will be applied to the maximum dimension
	 * the proportion are kept
	 * @param width
	 * @param height
	 * @param file the file path for the image to be resized
	 * @return
	 */
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
		debug("resizing to "+width+"x"+height);
		return image.getProcessor().resize(width, height).getBufferedImage();
	}
	
	/**
	 * returns an image with the maximum dimension provided
	 * the image will be resized according to their orientation
	 * the new size will be applied to the maximum dimension
	 * the proportion are kept
	 * the image will be saved in the destination file
	 * if destination file is null, the original name with suffix -th will be used
	 * @param width
	 * @param height
	 * @param src the file path for the image to be resized
	 * @param dest the destination file
	 * @return
	 */
	public static BufferedImage resizeAndSave(int size, String src, String dest) throws IOException {
		dest = createDest(src, dest);
		debug("resizing to "+size+" file "+src+" to "+dest);
		BufferedImage image = resize(size, src);
		ImageIO.write(image, "jpg", new File(dest));
		return image;
	}
	
	
}

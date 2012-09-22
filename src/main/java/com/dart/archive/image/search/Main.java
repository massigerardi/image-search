/**
 * 
 */
package com.dart.archive.image.search;

import ij.ImagePlus;
import ij.io.Opener;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.dart.archive.image.search.hash.PHashImageSearcher;

/**
 * @author massi 
 * 
 */
public class Main {

	private static String imagesFolder;

	private static ImageSearcher searcher;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length==0) {
			System.out.println("please, specify images folder path");
			System.out.println("java Main {folder} ");
			return;
		}
		imagesFolder = args[0];
		while(true){
			hideImages();
			int command = readInt("what kind of search? [1=hash, 2=color, 0=exit]", 0);
			if (command==0) {
				System.exit(0);
			}
			run(command);
		}

	}

	private static void run(int command) {
		long start = System.currentTimeMillis();
		switch (command) {
		case 1:
			searcher = new PHashImageSearcher(imagesFolder);
			break;
		case 2:
			int zones = readInt("enter the number of zones (5 as default)", 5);
			int size = readInt("enter the size of the zone (60 as default)", 60);
			System.out.println("using "+zones+" zones of "+size+"px");
			start = System.currentTimeMillis();
			searcher = new NaiveColorImageSearcher(imagesFolder, zones, size);
			break;
		default:
			System.out.println("wrong value; please retry");
			return;
		}
		long end = System.currentTimeMillis();
		System.out.println("loaded images in "+(end-start)+"ms");
		while (true) {
			String image = read("image to search? [nothing to start again]");
			hideImages();
			if (StringUtils.isBlank(image)) {
				return;
			}
			try {
				compare(new File(image));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static int readInt(String string, int defaultValue) {
		
		int result = -1;
		while (result<0) {
			String reading = read(string);
			if (StringUtils.isBlank(reading)) {
				return defaultValue;
			}
			try {
				result = Integer.valueOf(reading);
			} catch (NumberFormatException e) {
				System.err.println("Please enter a integer");
				result = -1;
			}
		}
		return result;
	}

	private static String read(String string) {
		System.out.println(string);
		String command = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (command==null) {
			try {
				command = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}
		}
		return command;
	}

	private static void compare(String folder) throws IOException {
		File testImages = new File(folder);
		Collection<File> images = FileUtils.listFiles(testImages,
				new String[] { "jpg" }, true);
		for (File image : images) {
			compare(image);
			String command = read("next?");
			hideImages();
			if (command.equalsIgnoreCase("n")) {
				break;
			}
		}
		System.out.println();
	}

	private static List<ImagePlus> windows = new ArrayList<ImagePlus>(); 
	
	private static void compare(File file) throws IOException {
		long start = System.currentTimeMillis();
		Collection<Candidate> candidates = searcher.compare(file);
		float end = (System.currentTimeMillis() - start);
		int counter = 1;
		int index = -1;
		boolean hasHits = false;
		String time = "[" + end + "ms]";
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.rightPad(time, 11));
		buffer.append(StringUtils.rightPad(file.getName(), 40));
		buffer.append(":");
		showImage(file, "test image");
		for (Candidate candidate : candidates) {
			if (!hasHits) {
				hasHits = checkName(file.getName(), candidate.getImage()
						.getName());
				if (hasHits) {
					index = counter;
				}
				hasHits = hasHits && index < 6;
			}
			if (counter < 6) {
				buffer.append(candidate);
				buffer.append(", ");
				showImage(candidate.getImage(),counter+" - "+candidate.getDistance());
			}
			counter++;
		}
		buffer.append("... ");
		buffer.insert(0, StringUtils.rightPad(String.valueOf(index), 5));
		System.out.println(buffer);
	}

	private static void showImage(File file, String status) {
		Opener opener = new Opener();
		ImagePlus image = opener.openImage(file.getAbsolutePath());
		windows.add(image);
		image.show(status);
	}
	
	private static void hideImages() {
		for (ImagePlus window : windows) {
			window.hide();
			window.close();
		}
		windows.clear();
	}
	
	private static boolean checkName(String image, String candidate) {
		String imageName = StringUtils.remove(image, ".jpg");
		String candifateName = StringUtils.remove(candidate, ".jpg");
		return imageName.startsWith(candifateName);
	}

}

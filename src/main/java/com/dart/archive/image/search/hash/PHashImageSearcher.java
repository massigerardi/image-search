/**
 * 
 */
package com.dart.archive.image.search.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.dart.archive.image.search.AImageSearcher;
import com.dart.archive.image.search.Candidate;
import com.dart.archive.image.search.ImageSearcher;
import com.dart.archive.image.search.CandidateImpl;

/**
 * @author massi
 *
 */
public class PHashImageSearcher extends AImageSearcher implements ImageSearcher {
	
	ImagePHash imagePHash = new ImagePHash();
	String folderName;
	List<ImageHash> images;
	public PHashImageSearcher(String folderName) {
		this.folderName = folderName;
		init();
	}

	private void init() {
		images = new  ArrayList<ImageHash>();
		File folder = new File(folderName);
		Collection<File> images = FileUtils.listFiles(folder, new String[] {"jpg"}, true);
		for (File image : images) {
			try {
				String hash = imagePHash.getHash(new FileInputStream(image));
				ImageHash imageHash = new ImageHash(hash, image);
				this.images.add(imageHash);
			} catch (FileNotFoundException e) {
				System.err.println("File "+image+" not found");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("error getting hash for file "+image);
				e.printStackTrace();
			}
			
		}
	}

	@Override
	protected void populateCandidate(Collection<Candidate> candidates, File file) {
		try {
			String hash = imagePHash.getHash(new FileInputStream(file));
			for (ImageHash imageHash : images) {
				Double distance = imagePHash.distance(hash, imageHash.getHash());
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				double result = (double)Math.round(distance * 100) / 100;
				Candidate candidate = new CandidateImpl(Double.valueOf(twoDForm.format(result)), imageHash.getImage());
				candidates.add(candidate);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

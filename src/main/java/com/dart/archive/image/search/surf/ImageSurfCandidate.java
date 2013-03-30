/**
 * 
 */
package com.dart.archive.image.search.surf;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.dart.archive.image.search.ACandidate;

/**
 * @author massi
 *
 */
@Getter
@AllArgsConstructor
public class ImageSurfCandidate extends ACandidate {

	File image;

	Double score;
	
	int interestingPoints;
	
}

/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author massi
 *
 */
@Getter
@AllArgsConstructor
@ToString
public class CandidateImpl extends ACandidate {

	Double score;
	
	File image;
	
}

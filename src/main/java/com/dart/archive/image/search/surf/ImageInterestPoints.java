/**
 * 
 */
package com.dart.archive.image.search.surf;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.dart.archive.image.search.surf.ip.InterestPoint;
import com.google.common.collect.ComparisonChain;

/**
 * @author massi
 *
 */
@Getter
@AllArgsConstructor
public class ImageInterestPoints implements Serializable, Comparable<ImageInterestPoints> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 835075804493712156L;

	File image;
	
	List<InterestPoint> points;

	@Override
	public int compareTo(ImageInterestPoints that) {
		// TODO Auto-generated method stub
		return ComparisonChain.start()
				.compare(this.getImage().getAbsolutePath(), that.getImage().getAbsolutePath())
				.result();
	}
	
}

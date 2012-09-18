/**
 * 
 */
package com.dart.archive.image.search;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author massi
 *
 */
public interface ImageSearcher {

	Collection<Candidate> compare(File image);
}

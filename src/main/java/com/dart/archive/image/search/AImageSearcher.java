package com.dart.archive.image.search;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

public abstract class AImageSearcher {

	public AImageSearcher() {
	}

	abstract void populateCandidate(Collection<Candidate> candidates, File file);
	
	public Collection<Candidate> compare(File file) {
		Collection<Candidate> result = new TreeSet<Candidate>();
		populateCandidate(result, file);
		return result;
	}

}
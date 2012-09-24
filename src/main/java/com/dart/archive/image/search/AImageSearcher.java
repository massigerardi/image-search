package com.dart.archive.image.search;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

public abstract class AImageSearcher implements ImageSearcher{

	protected abstract void search(Collection<Candidate> candidates, File file);
	
	public Collection<Candidate> search(File file) {
		Collection<Candidate> result = new TreeSet<Candidate>();
		search(result, file);
		return result;
	}

}
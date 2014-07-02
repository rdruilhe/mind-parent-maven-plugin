package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesSearch {
	private List<File> directories = null;
	private List<File> files = null;
	
	protected FilesSearch(String root, String pattern) {
		
		files = new ArrayList<File>();
		
		PathDependencies pathDependencies = new PathDependencies();

		directories = pathDependencies.getPathDependencies(root, pattern);
		
		for(File directory : directories)
		{
			for(File file : directory.listFiles()) {
				if(file.getName().matches(pattern))
					files.add(file);
			}
		}
	}

	protected List<File> getFilesList() {
		return files;
	}
	
	protected List<File> getDirectoriesList() {
		return directories;
	}
}

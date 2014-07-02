package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathBuilder {

	private List<File> paths;
	private static final char PATH_SEPARATOR = ':';

	protected PathBuilder() {
		paths = new ArrayList<File>();
	}

	protected void addToPath(File path) {
		paths.add(path);
	}

	protected List<File> getPaths() {
		return paths;
	}
	
	protected String pathsToString() {
		StringBuilder strBuilder = new StringBuilder();

		for(File path : paths)
			if (strBuilder.toString().isEmpty())
				strBuilder.append(path.getAbsolutePath());
			else
				strBuilder.append(PATH_SEPARATOR + path.getAbsolutePath());

		return strBuilder.toString();
	}
}

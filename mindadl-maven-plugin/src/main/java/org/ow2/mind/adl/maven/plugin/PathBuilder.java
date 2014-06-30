package org.ow2.mind.adl.maven.plugin;

public class PathBuilder {

	private StringBuilder strBuilder;
	private static final char PATH_SEPARATOR = ':';
	
	public PathBuilder() {
		strBuilder = new StringBuilder();
	}
	
	public void addToPath(String path) {
		if (strBuilder.toString().isEmpty())
			strBuilder.append(path);
		else
			strBuilder.append(PATH_SEPARATOR + path);
	}
	
	public String getPaths() {
		return strBuilder.toString();
	}
}

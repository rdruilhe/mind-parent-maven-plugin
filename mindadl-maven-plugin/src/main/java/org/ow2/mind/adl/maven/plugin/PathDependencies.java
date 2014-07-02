package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.util.List;

public class PathDependencies {
	private PathBuilder pathBuilder;
	
	protected PathDependencies() {
		pathBuilder = new PathBuilder();
	}
	
	private void explorePathDependencies(String root, String pattern) {
		File rootNode = new File(root);
		boolean matching = false;
				
		if(rootNode.list().length == 0)
			return;
		
		for(File node : rootNode.listFiles()) {
			if(node.getName().matches(pattern) && !matching) 
				matching = true;
			else if(node.isDirectory())
				explorePathDependencies(node.getAbsolutePath(), pattern);
		}

		if(matching)
			pathBuilder.addToPath(rootNode);
		
		return;
	}
	
	protected List<File> getPathDependencies(String root, String pattern) {
		explorePathDependencies(root, pattern);
		
		return pathBuilder.getPaths();
	}
}

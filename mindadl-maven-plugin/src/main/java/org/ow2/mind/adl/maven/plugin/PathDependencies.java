package org.ow2.mind.adl.maven.plugin;

import java.io.File;

public class PathDependencies {
	private PathBuilder pathBuilder;
	
	protected PathDependencies() {
		pathBuilder = new PathBuilder();
	}
	
	protected void explorePathDependencies(String root, String fileExtension) {
		File rootNode = new File(root);
		boolean matching = false;
				
		if(rootNode.list().length == 0)
			return;
		
		for(File node : rootNode.listFiles()) {
			// Regular expression to check file extension
			if(node.getName().matches("([^\\s]+(\\.(?i)(" + fileExtension + "))$)") && !matching) 
				matching = true;
			else if(node.isDirectory())
				this.explorePathDependencies(node.getAbsolutePath(), fileExtension);
		}

		if(matching)
			pathBuilder.addToPath(rootNode.getAbsolutePath());
		
		return;
	}
	
	protected String getPathDependencies () {
		return pathBuilder.getPaths();
	}
}

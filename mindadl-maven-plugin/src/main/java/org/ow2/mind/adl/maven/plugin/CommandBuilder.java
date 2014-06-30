package org.ow2.mind.adl.maven.plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {

	private List<String> allOrderedArguments = null;
	private PathBuilder srcPathBuilder = null;
	private PathBuilder incPathBuilder = null;
	
	protected CommandBuilder() {
		allOrderedArguments = new ArrayList<String>();
		srcPathBuilder = new PathBuilder();
		incPathBuilder = new PathBuilder();
	}
	
	protected void addArgWithoutPrefix(String argValue) {
		allOrderedArguments.add(argValue);
	}
	
	protected void addArgIfNotPresent(String argName, String argValue) {
		argName = MindAdlLauncherArguments.ARGUMENT_PREFIX + argName;
		
		for (String arg : allOrderedArguments)
			if (arg.startsWith(argName)) return;
		
		allOrderedArguments.add(argName + MindAdlLauncherArguments.ARGUMENT_NAME_VALUE_SEPARATOR + argValue);
	}

	protected void addArg(String argName, String argValue) {
		allOrderedArguments.add(MindAdlLauncherArguments.ARGUMENT_PREFIX + argName
				+ MindAdlLauncherArguments.ARGUMENT_NAME_VALUE_SEPARATOR + argValue);
	}
	
	protected void addToSrcPath(String path) {
		srcPathBuilder.addToPath(path);
	}
	
	protected void addToIncPath(String path) {
		incPathBuilder.addToPath(path);
	}
	
	protected List<String> getArguments() {
		addArg("src-path", srcPathBuilder.getPaths());
		addArg("inc-path", incPathBuilder.getPaths());
		
		return allOrderedArguments;
	}
}

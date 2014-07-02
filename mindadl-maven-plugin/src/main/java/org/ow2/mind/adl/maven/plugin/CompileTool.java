package org.ow2.mind.adl.maven.plugin;

import java.util.HashMap;

public class CompileTool {
	private String command = null;
	private String flags = null;
	
	protected CompileTool(HashMap<?,?> map) {
	
		if(map.get("command") != null)
			if(!map.get("command").toString().isEmpty())
				command = map.get("command").toString();
		
		if(map.get("flags") != null)
			if(!map.get("flags").toString().isEmpty())
				flags = map.get("flags").toString();
	}
	
	protected void addFlag(String flag) {
		flags = flags + " " + flag;
	}
	
	protected String getCommand() {
		return command;
	}
	
	protected String getFlags() {
		return flags;
	}
}

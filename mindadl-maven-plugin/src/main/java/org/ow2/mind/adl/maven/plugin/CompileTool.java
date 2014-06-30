package org.ow2.mind.adl.maven.plugin;

import java.util.HashMap;

public class CompileTool {
	private String command = null;
	private String flags = null;
	
	public CompileTool(HashMap<?,?> map) {
	
		if(map.get("command") != null)
			if(!map.get("command").toString().isEmpty())
				command = map.get("command").toString();
		
		if(map.get("flags") != null)
			if(!map.get("flags").toString().isEmpty())
				flags = map.get("flags").toString();
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getFlags() {
		return flags;
	}
}

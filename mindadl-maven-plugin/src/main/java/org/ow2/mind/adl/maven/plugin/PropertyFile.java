package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {
	private Properties properties = null;

	protected PropertyFile(File file) {
		properties = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(file);
			properties.load(fis);						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected CommandBuilder createCommandBuilder(CommandBuilder commandBuilder) {

		if(properties.getProperty("targetComponent") == null)
			throw new RuntimeException("\"targetComponent\" can not be empty");

		String targetComponent = properties.getProperty("targetComponent");

		if(!properties.getProperty("binaryName").isEmpty())
			targetComponent = targetComponent + ":" + properties.getProperty("binaryName"); 

		commandBuilder.addArgWithoutPrefix(targetComponent);

		if(!properties.getProperty("sourcePath").isEmpty())
			commandBuilder.addToSrcPath(properties.getProperty("sourcePath"));

		if(!properties.getProperty("testSourcePath").isEmpty())
			commandBuilder.addToSrcPath(properties.getProperty("testSourcePath"));

		if(!properties.getProperty("includePath").isEmpty()) 
			commandBuilder.addToIncPath(properties.getProperty("includePath"));

		if(!properties.getProperty("outputDirectory").isEmpty()) {
			commandBuilder.addArg("out-path", properties.getProperty("outputDirectory"));
			
			/* Creates the output directory if necessary */
			Util.createOutDirIfNecessary(properties.getProperty("outputDirectory"));
		}

		if(!properties.getProperty("compilerCommand").isEmpty())
			commandBuilder.addArg("compiler-command", properties.getProperty("compilerCommand"));

		if(!properties.getProperty("cFlags").isEmpty())
			commandBuilder.addArg("c-flags", properties.getProperty("cFlags"));

		if(!properties.getProperty("linkerCommand").isEmpty())
			commandBuilder.addArg("linker-command", properties.getProperty("linkerCommand"));

		if(!properties.getProperty("ldFlags").isEmpty())
			commandBuilder.addArg("ld-flags", properties.getProperty("ldFlags"));

		if(!properties.getProperty("assemblerCommand").isEmpty())
			commandBuilder.addArg("assembler-command", properties.getProperty("assemblerCommand"));

		if(!properties.getProperty("asFlags").isEmpty())
			commandBuilder.addArg("as-flags", properties.getProperty("asFlags"));
		
		if(!properties.getProperty("extraOptions").isEmpty())
			commandBuilder.addArgWithoutPrefix(properties.getProperty("extraOptions"));
		
		if(!properties.getProperty("testExtraOptions").isEmpty())
			commandBuilder.addArgWithoutPrefix(properties.getProperty("testExtraOptions"));

		return commandBuilder;
	}
}

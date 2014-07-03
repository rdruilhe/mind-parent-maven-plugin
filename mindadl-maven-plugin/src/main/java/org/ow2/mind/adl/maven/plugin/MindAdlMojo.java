/**
 Copyright 2007 INRIA
 Copyright 2014 Schneider-Electric

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 USA

 Contact: fractal@objectweb.org, mind@ow2.org
 Author:  Alessio Pace
 Contributor: Stephane Seyvoz 
 Contributor: Remi Druilhe
 */

package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.ow2.mind.Launcher;
import org.ow2.mind.cli.CmdOption;

/**
 * A simple mojo which allows invoking the <strong>Mind ADL Toolchain</strong>
 * passing some configuration. The invocation will happen in the same VM which
 * runs the MOJO.
 * 
 * Migrated to Java 5 annotations, as shown at:
 * http://olamy.blogspot.fr/2012/05/java5-annotations-support-for-maven.html
 * 
 * SSZ note: goal is old-school Maven 2.0 style (add @ before it to enable it)
 * goal compile
 * @author Alessio Pace
 * @contributor Stephane Seyvoz
 */
@Mojo(name = "mindadl", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
//@Execute( goal = "mindadl")
public class MindAdlMojo extends AbstractMojo {

	/**
	 * The Maven project reference.
	 */
	@Parameter(defaultValue = "${project}", required = true)
	protected MavenProject project;

	/**
	 * The fully qualified name of the component ADL definition to compile.
	 */
	@Parameter
	protected String	 adl;

	/**
	 * The list of fully qualified name of the component ADL definitions to
	 * compile.
	 */
	@Parameter
	protected List<?>	 adls;

	/**
	 * The output directory for the files produced by Mind ADL.
	 */
	@Parameter(defaultValue = "${project.build.directory}/build")
	protected String     outputDir;

	/**
	 * The source folders
	 */
	@Parameter(defaultValue = "src/main/mind")
	private String 		includeSrcPath;

	/**
	 * The test folders
	 */
	@Parameter(defaultValue = "src/test/mind")
	private String 		includeTestPath;

	/**
	 * The include folders
	 */
	@Parameter(defaultValue = "src/main/include")
	private String 		includeIncPath;

	/**
	 * Binary name
	 */
	@Parameter
	private String 		binaryName;

	/**
	 * Extra options to use with mindc
	 */
	@Parameter
	private String 		extraOptions;

	/**
	 * Property file
	 */
	@Parameter(defaultValue = "Default.properties")
	private String		propertyFile;

	/**
	 * Compiler command and flags;
	 */
	@Parameter
	private HashMap<?,?>		compiler;

	/**
	 * Linker command and flags;
	 */
	@Parameter
	private HashMap<?,?>		linker;

	/**
	 * Assembler command and flags;
	 */
	@Parameter
	private HashMap<?,?>		assembler;

	/**
	 * Others arguments
	 */
	@Parameter
	protected List<?>	arguments;

	private CompileTool compilerTool = null;
	private CompileTool linkerTool = null;
	private CompileTool assemblerTool = null;

	private CommandBuilder commandBuilder = null;

	public void execute() throws MojoExecutionException, MojoFailureException {

		commandBuilder = new CommandBuilder();

		//		File file = new File(project.getBasedir(), propertyFile);

		if((new File(project.getBasedir(), propertyFile)).exists())
		{
			getLog().warn("Property file exists. Overwritting MindAdl Maven plugin configuration section.");

			PropertyFile propFile = new PropertyFile(new File(project.getBasedir(), propertyFile));

			commandBuilder = propFile.createCommandBuilder(commandBuilder);
		} else {
			getLog().debug("Properties " + arguments);

			/* Handle ADL file */
			if (adl != null) {
				if (adls != null)
					throw new RuntimeException(
							"<adl> and <adls> tags cannot be used in the same time.");

				getLog().debug("Compiling architecture: " + adl);

				if(binaryName != null)
					adl = adl + ":" + binaryName;

				commandBuilder.addArgWithoutPrefix(adl);
			} else {
				if (adls == null)
					throw new RuntimeException(
							"At least one <adl> or <adls> tag must be specified.");

				getLog().debug("Compiling architecture: " + adls);

				/* the first argument is the ADL to be compiled */
				for (Object adlName : adls) {
					if (!(adlName instanceof Adl)) {
						throw new MojoExecutionException(
								"Invalid element in \"adls\" list. Must have the following form:\n"
										+ "  <adls>\n" + "    <adl>\n"
										+ "      <definition>ADL name</definition>\n"
										+ "      <execname>exec file name</execname>\n"
										+ "    </adl>\n" + "    ...\n" + "  </adls>");
					}

					commandBuilder.addArgWithoutPrefix(adlName.toString());
				}
			}

			/* Handle --srcPath argument */
			if(includeSrcPath != null) {				
				commandBuilder.addToSrcPath(includeSrcPath);
			}

			/* Handle --testPath argument */
			if(includeTestPath != null)
				commandBuilder.addToSrcPath(includeTestPath);

			/* Handle --incPath argument */
			if(includeIncPath != null)
				commandBuilder.addToIncPath(includeIncPath);

			/* Handle compilation tools */
			if (compiler != null)
				compilerTool = new CompileTool(compiler);

			if(compilerTool.getCommand() != null)
				commandBuilder.addArg("compiler-command", compilerTool.getCommand());

			if(compilerTool.getFlags() != null)
				commandBuilder.addArg("c-flags", compilerTool.getFlags());

			if (linker != null)
			{
				linkerTool = new CompileTool(linker);

				FilesSearch libFiles = new FilesSearch("target/mind-dependencies", "([^\\s]+(\\.(?i)(a|o))$)");

				for(File directory : libFiles.getDirectoriesList())
					linkerTool.addFlag("-L" + directory.getAbsolutePath());

				for(File file : libFiles.getFilesList())
					if(file.getName().endsWith(".a"))
						linkerTool.addFlag("-l" + file.getName().replace("lib", "").replace(".a", ""));
					else
						linkerTool.addFlag(file.getAbsolutePath());

				if(linker.get("script") != null)
					linkerTool.addFlag(" -T" + ((String) linker.get("script")));
				else {
					FilesSearch startupFiles = new FilesSearch("target/mind-dependencies/startup", "([^\\s]+(\\.(?i)(ld))$)");

					List<File> files = startupFiles.getFilesList();

					if(files.size() > 1) {
						getLog().error("Too much link scripts found:");

						for(File file : files)
							getLog().error(file.getAbsolutePath());
					}
					else if(files.size() == 1)
						linkerTool.addFlag("-T" + files.get(0).getAbsolutePath());
					else
						getLog().info("No link script found");
				}
			}

			if(linkerTool.getCommand() != null)
				commandBuilder.addArg("linker-command", linkerTool.getCommand());

			if(linkerTool.getFlags() != null)
				commandBuilder.addArg("ld-flags", linkerTool.getFlags());

			if (assembler != null)
				assemblerTool = new CompileTool(assembler);

			if(assemblerTool.getCommand() != null)
				commandBuilder.addArg("assembler-command", assemblerTool.getCommand());

			if(assemblerTool.getFlags() != null)
				commandBuilder.addArg("as-flags", assemblerTool.getFlags());

			// Add the "out-path" parameter
			if(outputDir != null)
				commandBuilder.addArg("out-path", outputDir);

			/* Creates the output directory if necessary */
			Util.createOutDirIfNecessary(outputDir);

			/* Handle extra options of Mindc */
			if(extraOptions != null) 
				commandBuilder.addArgWithoutPrefix(extraOptions);

			//			/* Append the other parameters */
			//			if (arguments != null) {
			//				for (Object argument : arguments) {
			//					if (!(argument instanceof MindAdlLauncherArguments)) {
			//						throw new MojoExecutionException(
			//								"Invalid element in \"arguments\" list. Must have one of the following form:\n"
			//										+ "  <parameter>\n" + "      <name>param name</name>\n"
			//										+ "      <value>param value</value>\n" + "  </parameter>\n"
			//										+ "or" + "  <properties>\n"
			//										+ "      <file>property file name</file>\n"
			//										+ "  </properties>\n" + "or" + "  <flag>\n"
			//										+ "      <name>flag name</name>\n" + "  </flag>");
			//					}
			//					
			//					commandBuilder.addAll(((MindAdlLauncherArguments) argument).getArguments(project));
			//				}
			//			}
		}

		//		if (allOrderedArguments.contains("--check-adl")) {
		//			// add by default the project sources directory in the toolchain
		//			// src-path (this way, the project .c and .itf will be found.
		//			addArg(allOrderedArguments, "src-path", project.getBasedir()
		//					.getPath() + "/src/main/mind");
		//		}
		//		else {
		//			// add by default the project build output directory in the toolchain
		//			// src-path (this way, the project .c and .itf will be found.
		//			addArg(allOrderedArguments, "src-path", project.getBuild()
		//					.getOutputDirectory());
		//		}

		/* Automatic informations added to the CommandBuilder (e.g., mind dependencies paths) */

		File dep = new File(project.getBasedir(), "target/mind-dependencies");

		if (dep.isDirectory()) {
			commandBuilder.addToSrcPath(new File("target/mind-dependencies/mind"));

			PathDependencies srcPathDependencies =  new PathDependencies();

			// Looking for all directories containing .c files
			commandBuilder.addToSrcPath(srcPathDependencies.getPathDependencies("target/mind-dependencies", "([^\\s]+(\\.(?i)(c))$)"));

			PathDependencies incPathDependencies =  new PathDependencies();

			// Looking for all directories containing .h files
			commandBuilder.addToIncPath(incPathDependencies.getPathDependencies("target/mind-dependencies", "([^\\s]+(\\.(?i)(h))$)"));
		}

		final String[] args = commandBuilder.getArguments().toArray(new String[0]);

		getLog().info("Invoking Mind ADL Launcher with the following command line parameters: " + Arrays.deepToString(args));

		try {
			new MojoLauncher(args);
		} catch (Exception e) {
			if (e instanceof MojoFailureException)
				throw (MojoFailureException) e;
			else if (e instanceof MojoExecutionException)
				throw (MojoExecutionException) e;
			else
				throw new MojoExecutionException("Unexpected error", e);
		}
	}

	private class MojoLauncher extends Launcher {

		/** @see Launcher#main(String...) */
		public MojoLauncher(String... args) throws Exception {
			super.main(args);
		}

		//    @Override
		//    protected void handleException(ADLException e) throws Exception {
		//      MojoFailureException mojoFailureException = new MojoFailureException(e
		//          .getError().toString());
		//      mojoFailureException.initCause(e);
		//      throw mojoFailureException;
		//    }
		//
		//    @Override
		//    protected void handleException(CompilerInstantiationException e)
		//        throws Exception {
		//      throw new MojoExecutionException(
		//          "An error occurs while instantiating MindADL compiler", e);
		//    }
		//
		//    @Override
		//    protected void handleException(InvalidCommandLineException e) throws MojoFailureException {
		//      ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//      PrintStream ps = new PrintStream(baos);
		//      printHelp(ps);
		//      ps.flush();
		//      MojoFailureException mojoFailureException = new MojoFailureException(
		//          null, e.getMessage(), baos.toString());
		//      mojoFailureException.initCause(e);
		//      throw mojoFailureException;
		//    }

		@Override
		protected void printHelp(PrintStream ps) {
			ps.println("Configuration must have the following form:");
			ps.println();
			ps.println("  <configuration>");
			ps.println("    <adl>");
			ps.println("      ADL TO COMPILE");
			ps.println("    </adl>");
			ps.println("    <arguments>");
			ps.println("      <parameter>");
			ps.println("        <name>PARAM NAME</name>");
			ps.println("        <value>PARAM VALUE</value>");
			ps.println("      </parameter>");
			ps.println("      ...");
			ps.println("      <properties>");
			ps.println("        <file>PROPERTIES FILE NAME</file>");
			ps.println("      </properties>");
			ps.println("      ...");
			ps.println("      <flag>");
			ps.println("        <name>FLAG NAME</file>");
			ps.println("      </flag>");
			ps.println("      ...");
			ps.println("    </arguments>");
			ps.println("  </configuration>");
			ps.println();
			ps.println();
			ps.println("Where available arguments are:");

			int maxCol = 0;
			for (final CmdOption opt : options.getOptions()) {

				// FIXME old simply commented
				//        if (opt == printStackTraceOpt || opt == outDirOpt
				//            || opt == targetDescOpt) continue;
				String name = opt.getLongName();
				if (name == null) name = opt.getShortName();
				int col = 2 + name.length();
				if (col > maxCol) maxCol = col;
			}

			for (final CmdOption opt : options.getOptions()) {

				// FIXME old simply commented
				//        if (opt == printStackTraceOpt || opt == outDirOpt
				//            || opt == targetDescOpt) continue;
				final StringBuffer sb = new StringBuffer("  ");
				String name = opt.getLongName();
				if (name == null) name = opt.getShortName();
				sb.append(name);
				while (sb.length() < maxCol)
					sb.append(' ');
				sb.append(": ").append(opt.getDescription());
				ps.println(sb);
			}
		}
	}
}

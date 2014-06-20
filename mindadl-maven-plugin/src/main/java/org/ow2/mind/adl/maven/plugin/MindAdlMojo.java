/**
 Copyright 2007 INRIA

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

 Contact: fractal@objectweb.org
 Author:  Alessio Pace
 Contributor: Stephane Seyvoz 
 */

package org.ow2.mind.adl.maven.plugin;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
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
	protected String       adl;

	/**
	 * The list of fully qualified name of the component ADL definitions to
	 * compile.
	 */
	@Parameter
	protected List<?>      adls;

	/**
	 * The output directory for the files produced by Mind ADL.
	 */
	@Parameter(defaultValue = "${project.build.directory}/build")
	protected String       outDir;

	/**
	 * The name of the target descriptor to use.
	 */
	@Parameter
	private String       target;

	/**
	 * 
	 */
	@Parameter
	protected List<?>      arguments;

	/**
	 *
	 */
	protected List<String> allOrderedArguments = null;

	protected void addArgIfNotPresent(List<String> args, String argName,
			String argValue) {
		argName = MindAdlLauncherArguments.ARGUMENT_PREFIX + argName;
		for (String arg : args) {
			if (arg.startsWith(argName)) return;
		}
		args.add(argName
				+ MindAdlLauncherArguments.ARGUMENT_NAME_VALUE_SEPARATOR + argValue);
	}

	protected void addArg(List<String> args, String argName, String argValue) {
		args.add(MindAdlLauncherArguments.ARGUMENT_PREFIX + argName
				+ MindAdlLauncherArguments.ARGUMENT_NAME_VALUE_SEPARATOR + argValue);
	}

	public void execute() throws MojoExecutionException, MojoFailureException {

		if (allOrderedArguments == null) {
			allOrderedArguments = new ArrayList<String>();
		}

		if (adl != null) {
			if (adls != null)
				throw new RuntimeException(
						"<adl> and <adls> tags cannot be used in the same time.");
			getLog().debug("Compiling architecture: " + adl);
			allOrderedArguments.add(adl);
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
				allOrderedArguments.add(adlName.toString());
			}
		}

		getLog().debug("Properties " + arguments);

		
		// add by default the project sources directory in the toolchain
		// src-path (this way, the project .c and .itf will be found.
		addArg(allOrderedArguments, "src-path", project.getBasedir()
				.getPath() + "/src/main/mind");
		
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

		File dep = new File(project.getBasedir(), "target/mind-dependencies");
		if (dep.isDirectory()) {
			addArg(allOrderedArguments, "src-path", dep.getPath());
		}

		/* creates the output dir if necessary */
		Util.createOutDirIfNecessary(outDir);

		/* append the other parameters */
		if (arguments != null) {
			for (Object argument : arguments) {
				if (!(argument instanceof MindAdlLauncherArguments)) {
					throw new MojoExecutionException(
							"Invalid element in \"arguments\" list. Must have one of the following form:\n"
									+ "  <parameter>\n" + "      <name>param name</name>\n"
									+ "      <value>param value</value>\n" + "  </parameter>\n"
									+ "or" + "  <properties>\n"
									+ "      <file>property file name</file>\n"
									+ "  </properties>\n" + "or" + "  <flag>\n"
									+ "      <name>flag name</name>\n" + "  </flag>");
				}
				allOrderedArguments.addAll(((MindAdlLauncherArguments) argument)
						.getArguments(project));
			}
		}

		// add the "out-path" parameter
		addArg(allOrderedArguments, "out-path", outDir);

		// add the "target-descriptor" parameter (if any)
		if (target != null)
			addArg(allOrderedArguments, "target-descriptor", target);

		final String[] args = allOrderedArguments.toArray(new String[0]);
		getLog().debug(
				"Invoking Mind ADL Launcher with the following command line parameters: "
						+ Arrays.deepToString(args));

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

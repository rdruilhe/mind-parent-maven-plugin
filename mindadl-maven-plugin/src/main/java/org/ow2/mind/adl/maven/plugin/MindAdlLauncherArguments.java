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

import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * @author Alessio Pace
 * @contributor Stephane Seyvoz
 *
 */
public interface MindAdlLauncherArguments {
	
	public static final String ARGUMENT_PREFIX = "--";
	public static final String ARGUMENT_NAME_VALUE_SEPARATOR = "=";
	
	/**
	 * Return a list command line arguments as the ones expected by the Mind ADL Launcher.
	 * 
	 * @return
	 */
    List<String> getArguments(MavenProject project);
}

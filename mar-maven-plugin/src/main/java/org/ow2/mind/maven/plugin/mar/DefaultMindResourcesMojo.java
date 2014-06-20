/***
 * mind
 * Copyright (C) 2007 STMicroelectronics
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: fractal@objectweb.org
 *
 * Author: Matthieu Leclercq
 */

package org.ow2.mind.maven.plugin.mar;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * Adds the default mind directories as project resources.
 * 
 * @goal default-resources
 * @requiresProject
 */
public class DefaultMindResourcesMojo extends AbstractMojo {

  public static final String[] DEFAULT_INCLUDES = {"**/*.adl", "**/*.itf",
      "**/*.c", "**/*.h", "**/*.s", "**/*.S"    };
  public static final String[] DEFAULT_EXCLUDES = {};

  /**
   * The directory containing the mind sources
   * 
   * @parameter default-value="${basedir}/src/main/mind"
   * @required
   */
  private File                 mindDirectory;

  /**
   * The included resources. (default is *.adl, *.itf, *.c, *.h, *.s, *.S).
   * 
   * @parameter
   */
  private String[]             includes         = DEFAULT_INCLUDES;

  /**
   * the excluded resources. (default is none)
   * 
   * @parameter
   */
  private String[]             excludes         = DEFAULT_EXCLUDES;

  /**
   * The maven project.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject         project;

  /**
   * @component
   */
  private MavenProjectHelper   projectHelper;

  public void execute() throws MojoExecutionException, MojoFailureException {
    projectHelper.addResource(project, mindDirectory.getPath(),
        Arrays.asList(includes), Arrays.asList(excludes));
  }
}

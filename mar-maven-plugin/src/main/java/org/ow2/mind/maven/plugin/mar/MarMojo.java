/***
 * Mind Maven Plugins
 * Copyright (C) 2007 STMicroelectronics
 * Copyright (C) 2014 Schneider-Electric
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
 * Contact: mind@ow2.org
 *
 * Author: Matthieu Leclercq
 * Contributors: Julien Tous, Stephane Seyvoz
 */

package org.ow2.mind.maven.plugin.mar;

import java.io.File;

/**
 * Build a MAR (mind ARchive) from the current project.
 *
 * 
 * @goal mar
 * @phase package
 * @requiresProject
 */
// Didn't manage to make Java 5 annotations work...
//@Mojo(name = "mar", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, requiresProject = true)
//@Execute( goal = "mar", phase = LifecyclePhase.PACKAGE )
public class MarMojo extends AbstractMarMojo {

    /**
     * Directory containing the classes.
     * 
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
	//@Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    private File classesDirectory;

    /**
     * Classifier to add to the artifact generated. If given, the artifact will be an attachment instead.
     * 
     * @parameter
     */
	//@Parameter
    private String classifier;

    protected String getClassifier()
    {
        return classifier;
    }
    
    /**
     * @return type of the generated artifact
     */
    protected String getType()
    {
        return MAR_TYPE;
    }

    /**
     * Return the main classes directory, so it's used as the root of the jar.
     */
    protected File getClassesDirectory()
    {
        return classesDirectory;
    }
}

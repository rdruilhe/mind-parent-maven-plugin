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
 * Build a MAR of the test code for the current project.
 *
 * @goal test-mar
 * @phase package
 * @requiresProject
 */
public class TestMarMojo extends AbstractMarMojo {

    /**
     * Directory containing the test classes.
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private File testClassesDirectory;

    protected String getClassifier()
    {
        return "tests";
    }

    /**
     * @return type of the generated artifact
     */
    protected String getType()
    {
        return "test-mar";
    }

    /**
     * Return the test-classes directory, to serve as the root of the tests jar.
     */
    protected File getClassesDirectory()
    {
        return testClassesDirectory;
    }
}

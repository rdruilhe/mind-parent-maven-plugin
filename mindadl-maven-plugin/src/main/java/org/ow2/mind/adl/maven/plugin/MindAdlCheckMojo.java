/**
 Copyright 2008 INRIA
 
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
 Author:  Lionel Debroux
 Contributor: Stephane Seyvoz 
 */

package org.ow2.mind.adl.maven.plugin;

import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * A simple mojo which allows invoking the <strong>Mind ADL Toolchain</strong>
 * in ADL checking mode. The invocation will happen in the same VM which
 * runs the MOJO.
 * 
 * @goal check
 * @author Lionel Debroux
 * @contributor Stephane Seyvoz
 */
public class MindAdlCheckMojo extends MindAdlMojo {

  public void execute() throws MojoExecutionException, MojoFailureException {
    allOrderedArguments = new ArrayList<String>();
    
    allOrderedArguments.add("--check-adl");
    
    super.execute();
  }
}

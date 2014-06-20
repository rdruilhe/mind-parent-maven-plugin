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
 */

package org.ow2.mind.adl.maven.plugin;

import java.io.File;

/**
 * @author Alessio Pace
 * @contributor Stephane Seyvoz
 */
public class Util {

  public static void createOutDirIfNecessary(String outputDirectoryPath)
      throws RuntimeException {
    File outDir = new File(outputDirectoryPath);

    if (outDir.exists()) {
      if (!outDir.isDirectory()) {
        throw new RuntimeException(
            "There is a file with the same name of the chosen output directory, remove it or specify another output directory name");
      }// else DO NOTHING
    } else {
      boolean created = outDir.mkdir();
      if (!created) {
        throw new RuntimeException("Unable to create folder " + outDir);
      } // else DO NOTHING
    }

  }
}

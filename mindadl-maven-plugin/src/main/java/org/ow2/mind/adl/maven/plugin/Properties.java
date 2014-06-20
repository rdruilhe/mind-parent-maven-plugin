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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.project.MavenProject;

/**
 * @author Alessio Pace
 * @contributor Stephane Seyvoz
 */
public class Properties implements MindAdlLauncherArguments {

  private File file;

  public String toString() {
    return "File: " + file;
  }

  public List<String> getArguments(MavenProject project) {

    java.util.Properties p = new java.util.Properties();
    List<String> arguments = new ArrayList<String>(p.size());

    if (file != null) {
      try {
        final FileInputStream fileInputStream = new FileInputStream(file);
        p.load(fileInputStream);

        for (Map.Entry<Object, Object> entry : p.entrySet()) {
          String name = (String) entry.getKey();
          String value = (String) entry.getValue();

          /*
           * Replace all occurrences of ${*} by the corresponding property value
           */
          Pattern pattern = Pattern.compile("\\$\\{[^\\$]*\\}");
          Matcher matcher = pattern.matcher(value);

          while (matcher.find()) {
            String propertyName = matcher.group();
            String key = propertyName.substring(2, propertyName.length() - 1);

            /*
             * The property is searched first in the project properties, then in
             * the system properties and finally in the environment variables
             */
            String propertyValue = project.getProperties().getProperty(key);
            if (propertyValue == null) {
              propertyValue = System.getProperty(key);
              if (propertyValue == null) {
                propertyValue = System.getenv(key);
              }
            }

            if (propertyValue != null) {
              value = value.replace(propertyName, propertyValue);
              matcher.reset(value);
            }
          }

          arguments.add("-" + name + "=" + value);
        }
        return arguments;
      } catch (FileNotFoundException fnfe) {
        fnfe.printStackTrace();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      throw new RuntimeException("Unable to find or read properties from "
          + file);
    }
    return arguments;
  }

}

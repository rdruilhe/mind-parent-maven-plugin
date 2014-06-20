/***
 * Mind Maven Plugins
 * Copyright (C) 2008 STMicroelectronics
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
 * Contributor: Stephane Seyvoz 
 */

package org.ow2.mind.unit.maven.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * @author leclercm
 */
public class Flag implements MindUnitLauncherArguments {
  private String name;

  public List<String> getArguments(MavenProject project) {
    List<String> result = new ArrayList<String>(1);
    result.add(this.toString());
    return result;
  }

  public String toString() {
    return ARGUMENT_PREFIX + name;
  }

}

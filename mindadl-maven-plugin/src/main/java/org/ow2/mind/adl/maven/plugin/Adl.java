/***
 * Mind ADL Compiler
 * Copyright (C) 2008 STMicroelectronics
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
 * Contributor: Stephane Seyvoz 
 */

package org.ow2.mind.adl.maven.plugin;

/**
 * @author leclercm
 * @contributor seyvozs
 */
public class Adl {
  private String definition;
  private String execname;

  public String toString() {
    // Check the correctness of the definition name.
    if (definition.contains(":")) {
      throw new RuntimeException("A <definition> name cannot contain ':'.");
    }
    if (execname == null) {
      return definition + ':';
    } else {
      return definition + ':' + execname;
    }
  }
}

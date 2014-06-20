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
 * Author: Stephane Seyvoz
 * Contributor: 
 */

package org.ow2.mind.unit.maven.plugin;

/**
 * @author seyvozs
 */
public class TestFolder {
	private String testFolder;

	public String toString() {
		// Check the correctness of the testFolder name.

		// Maybe check existence as well ? The toolchain will do it anyway.
		
		if (testFolder == null)
			throw new RuntimeException("A <testFolder> name cannot be null !");

		if (testFolder.contains(":"))
			throw new RuntimeException("A <testFolder> name cannot contain ':'.");
		
		return testFolder;
	}
}
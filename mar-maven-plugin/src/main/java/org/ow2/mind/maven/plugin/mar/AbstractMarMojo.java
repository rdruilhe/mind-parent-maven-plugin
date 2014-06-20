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

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.jar.JarArchiver;

public abstract class AbstractMarMojo extends AbstractMojo {

  private static final String[]     DEFAULT_EXCLUDES = new String[]{};

  private static final String[]     DEFAULT_INCLUDES = new String[]{"**/**"};

  public static final String        MAR_TYPE         = "mar";

  /**
   * Directory containing the generated MAR.
   * 
   * @parameter expression="${project.build.directory}"
   * @required
   */
  private File                      outputDirectory;

  /**
   * Name of the generated MAR.
   * 
   * @parameter alias="jarName" expression="${project.build.finalName}"
   * @required
   */
  private String                    finalName;

  /**
   * The Jar archiver.
   * 
   * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#mar}"
   * @required
   */
  private JarArchiver               marArchiver;

  /**
   * The maven project.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject              project;

  /**
   * The maven archive configuration to use. See <a
   * href="http://maven.apache.org/ref/2.0.4/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html">the
   * Javadocs for MavenArchiveConfiguration</a>.
   * 
   * @parameter
   */
  private MavenArchiveConfiguration archive          = new MavenArchiveConfiguration();

  /**
   * @component
   */
  private MavenProjectHelper        projectHelper;

  /**
   * @component
   */
  private ArtifactFactory           artifactFactory;

  /**
   * Whether creating the archive should be forced.
   * 
   * @parameter expression="${jar.forceCreation}" default-value="false"
   */
  private boolean                   forceCreation;

  /**
   * Return the specific output directory to serve as the root for the archive.
   */
  protected abstract File getClassesDirectory();

  protected final MavenProject getProject() {
    return project;
  }

  /**
   * Overload this to produce a test-jar, for example.
   */
  protected abstract String getClassifier();

  /**
   * Overload this to produce a test-jar, for example.
   */
  protected abstract String getType();

  protected static File getJarFile(File basedir, String finalName,
      String classifier) {
    if (classifier == null) {
      classifier = "";
    } else if (classifier.trim().length() > 0 && !classifier.startsWith("-")) {
      classifier = "-" + classifier;
    }

    return new File(basedir, finalName + classifier + "." + MAR_TYPE);
  }

  /**
   * Generates the MAR.
   */
  public File createArchive() throws MojoExecutionException {
    File jarFile = getJarFile(outputDirectory, finalName, getClassifier());

    MavenArchiver archiver = new MavenArchiver();

    archiver.setArchiver(marArchiver);

    archiver.setOutputFile(jarFile);

    archive.setForced(forceCreation);

    try {
      File contentDirectory = getClassesDirectory();
      if (!contentDirectory.exists()) {
        getLog().warn(
            "MAR will be empty - no content was marked for inclusion!");
      } else {
        archiver.getArchiver().addDirectory(contentDirectory, DEFAULT_INCLUDES,
            DEFAULT_EXCLUDES);
      }

      archiver.createArchive(project, archive);

      return jarFile;
    } catch (Exception e) {
      // TODO: improve error handling
      throw new MojoExecutionException("Error assembling MAR", e);
    }
  }

  /**
   * Generates the JAR.
   */
  public void execute() throws MojoExecutionException {
    File jarFile = createArchive();

    String classifier = getClassifier();
    if (classifier != null) {
      projectHelper
          .attachArtifact(getProject(), getType(), classifier, jarFile);
    } else {
      // create a new artifact for the project with the appropriate type.
      // since by default the type is mapped to the "packaging"
      Artifact artifact = getProject().getArtifact();
      Artifact newArtifact = artifactFactory.createArtifact(artifact
          .getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
          artifact.getClassifier(), getType());
      newArtifact.setFile(jarFile);
      getProject().setArtifact(newArtifact);
      // projectHelper.attachArtifact( getProject(), getType(), jarFile );
    }
  }
}

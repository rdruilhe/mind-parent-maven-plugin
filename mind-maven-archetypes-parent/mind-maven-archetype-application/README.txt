################################################################################
Guide to creating and using Maven archetypes:
http://maven.apache.org/guides/mini/guide-creating-archetypes.html
################################################################################


################################################################################
#             USAGE
################################################################################
To create a project using this archetype, from the command line type:

$ mvn archetype:create -DarchetypeGroupId=org.objectweb.fractal.mind \
    -DarchetypeArtifactId=maven-archetype-mind-application \
    -DarchetypeVersion=THIS_ARCHETYPE_VERSION \
    -DgroupId=YOUR_PROJECT_GROUPID \
    -DartifactId=YOUR_MIND_APPLICATION_ARTIFACTID \
    -Dversion=YOUR_MIND_APPLICATION_INITIAL_VERSION \
    -DremoteRepositories="http://maven.objectweb.org/maven2/,http://maven.objectweb.org/maven2-snapshot/"

where THIS_ARCHETYPE_VERSION is the version of this archetype.

Usually it is suggested to use as archetype version the version number of the 
latest Mind release.
  

The command will create a folder YOUR_PROJECT_ARTIFACTID with a Maven source
tree structure alread initialized. 

From the folder, you can play with a minimal configuration already provided for
you by the template, specifically a composite component which includes a
primitive component that prints on the screen a message.

You can of course customize the creation by passing different values for 
YOUR_PROJECT_GROUPID and YOUR_PROJECT_ARTIFACTID, as well as the "-Dversion" for
your new project version.

To execute the minimalistic template application you can execute:
  
$ mvn clean compile
  
which will create an executable file in the "target" directory.

################################################################################
#                 BUILD NOTES
################################################################################

You may encounter problems when deploying new SNAPSHOT artifact of this module.
In particular the integration tests may fail. This is due to the fact that
artifacts deployed on the OW2 maven repository, are not immediately available
(you have to push the magic button on the OW2 forge to make them available). In
that case, you can skip the execution of the integration tests by using the
following command:

$ mvn clean deploy -Dintegration-test.skip

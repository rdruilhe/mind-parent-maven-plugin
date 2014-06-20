################################################################################
            OVERVIEW
################################################################################
This archetype provides mean to quick start a mind component library project.

In few words this mean that you will have already a Maven project setup to be 
able to package your component library as a "Mind ARchive" (car) package and
be able to let Mind applications depend on it.

See for example how the Mind HelloWorld example depends on the 
"mind-baselib" module, which is a minimalistic component library.


################################################################################
#             USAGE
################################################################################
To create a mind components library using this archetype, from the command 
line type:

$ mvn archetype:create -DarchetypeGroupId=org.objectweb.fractal.mind \
    -DarchetypeArtifactId=maven-archetype-mind-library \
    -DarchetypeVersion=THIS_ARCHETYPE_VERSION \
    -DgroupId=YOUR_PROJECT_GROUPID \
    -DartifactId=YOUR_MIND_LIBRARY_ARTIFACTID \
    -Dversion=1.0-SNAPSHOT \
    -DremoteRepositories="http://maven.objectweb.org/maven2/,http://maven.objectweb.org/maven2-snapshot/"

where THIS_ARCHETYPE_VERSION is the version of this archetype.

Usually it is suggested to use as archetype version the version number of the
latest Mind release.
  
The command will create a folder YOUR_PROJECT_ARTIFACTID with the project
template. 

You can of course customize the creation by passing different values for 
YOUR_PROJECT_GROUPID and YOUR_PROJECT_ARTIFACTID, as well as the "-Dversion" for
your new project version.

The archetype create a component library with one component providing an 
interface, to show you the organization of files and the execution of the
packaging by means of the execution of: 

$ mvn clean package
  
which will make a .car(Mind ARchive) file in the "target" directory. You can
have more informations about .car files following the documentation about the
maven-car-plugin.


################################################################################
As a reference, if you are interested in Maven archetypes you can look at:
  
  http://maven.apache.org/guides/mini/guide-creating-archetypes.html
################################################################################

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

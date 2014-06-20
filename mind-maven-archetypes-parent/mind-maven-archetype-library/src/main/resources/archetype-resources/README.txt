The "src/main/mind" folder should contains the sources of your application (*.c, *.h, *.itf, *.adl), eventually
organized under different packages.

If you have just generated this project using the "maven-archetype-mind-library", you can then play with the
above files, for example with:
 
 $ mvn clean package
 
which will create a ".mar" (Mind ARchive) file in the "./target" directory.
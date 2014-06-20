The "src/main/mind" folder (and subdirectories) should contains the sources of your application (*.c, *.h, *.idl, *.fractal).

If you have just generated this project using the "maven-archetype-mind-application", you can then play with the
above files, for example with:
 
 $ mvn clean compile
 
which will create an executable file located under "./target/build/obj". See the end of the console messages
produced by the above command to get the full path to the file which you can execute.

# Quick sh script to compile the engine and make a jar out of it

javac $(find ./src -name *.java)
jar cvf Gingerbread3.jar -C src/ .

#!/bin/bash
mkdir -p bin
javac -encoding UTF-8 -d bin -sourcepath src src/Main.java
if [ $? -eq 0 ]; then
    java -Dfile.encoding=UTF-8 -cp bin Main
else
    echo "Compilation failed."
fi

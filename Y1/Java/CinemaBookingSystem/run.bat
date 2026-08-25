@echo off
chcp 65001 > nul
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src/Main.java
if %ERRORLEVEL% equ 0 (
    java -Dfile.encoding=UTF-8 -cp bin Main
) else (
    echo Compilation failed.
    pause
)

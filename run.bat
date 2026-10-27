@echo off
REM Compile and run (Windows). Put the MySQL Connector/J jar in lib\ first.
if exist out rmdir /s /q out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt && java -cp "out;lib\*" com.friction.Main
del sources.txt

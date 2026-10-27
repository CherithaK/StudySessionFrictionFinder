#!/bin/bash
# Compile and run (macOS / Linux). Put the MySQL Connector/J jar in lib/ first.
rm -rf out
javac -d out $(find src -name "*.java") && java -cp "out:lib/*" com.friction.Main

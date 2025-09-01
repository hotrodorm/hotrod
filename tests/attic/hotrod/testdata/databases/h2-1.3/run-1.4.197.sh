#!/bin/bash
# java -jar lib/h2-1.4.196.jar
# java -cp lib/h2-1.4.196.jar org.h2.tools.Server -tcp -ifExists -baseDir .
java -cp jdbc-drivers/h2-1.4.197.jar org.h2.tools.Server -tcp


#!/bin/bash
# java -jar lib/h2-1.3.176.jar
# java -cp lib/h2-1.3.176.jar org.h2.tools.Server -tcp -ifExists -baseDir .
java -cp jdbc-drivers/h2-1.3.176.jar org.h2.tools.Server -tcp


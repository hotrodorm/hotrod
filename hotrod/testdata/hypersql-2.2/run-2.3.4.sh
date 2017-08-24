#!/bin/bash
mkdir -p db2
java -cp jdbc-drivers/hsqldb-2.3.4.jar org.hsqldb.server.Server --database.0 file:db2/db2 --dbname.0 xdb


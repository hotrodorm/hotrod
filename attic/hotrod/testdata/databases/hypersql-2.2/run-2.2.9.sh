#!/bin/bash
mkdir -p db1
java -cp jdbc-drivers/hsqldb-2.2.9.jar org.hsqldb.server.Server --database.0 file:db1/db1 --dbname.0 xdb


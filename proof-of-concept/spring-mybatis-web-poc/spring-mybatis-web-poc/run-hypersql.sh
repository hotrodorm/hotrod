#!/bin/bash

mkdir -p data/database1

java -cp lib/hsqldb-2.3.4.jar org.hsqldb.server.Server --database.0 file:data/database1 --dbname.0 xdb



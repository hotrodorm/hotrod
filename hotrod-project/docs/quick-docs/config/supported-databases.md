# Supported Databases

HotRod supports the following Databases:
- Oracle Database
- Db2 LUW
- PostgreSQL
- SQL Server
- MariaDB
- MySQL
- SAP ASE
- H2
- HyperSQL
- Apache Derby

## Default Data Types

HotRod maps the data type of each database column with a default Java type.

The default Java type can be superseded by the user defined data type, specified by a `<column>` tag in
the `<table>`, `<view>`, or `<select>` query or, alternatively, by a `<type-solver>` rule. 

Explicit types or rules are sometimes needed if the database uses an exotic type that HotRod doesn't 
know how to handle by default.

For more complex cases a `<converter>` can manipulate data with tailored Java logic when reading from 
and writing to the database.

The default data type for each column depends on the specific database. See sections below:

- [Support for Oracle Database](./database-support/oracle.md).
- [Support for DB2 LUW Database](./database-support/db2-luw.md).
- [Support for PostgreSQL Database](./database-support/postgresql.md).
- [Support for SQL Server Database](./database-support/sql-server.md).
- [Support for MariaDB Database](./database-support/mariadb.md).
- [Support for MySQL Database](./database-support/mysql.md).
- [Support for SAP ASE (ex-Sybase) Database](./database-support/sap-ase.md).
- [Support for H2 Database](./database-support/h2.md).
- [Support for HSQLDB Database](./database-support/hsqldb.md).
- [Support for Apache Derby Database](./database-support/apache-derby.md).



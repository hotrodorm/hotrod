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

An explicit type, or a rule is sometimes needed if the database uses an exotic type that HotRod doesn't 
know how to handle by default.

For more complex cases a `<converter>` can manipulate data with tailored Java logic when reading from 
and writing to the database.

The default data type for each column depends on the specific database as shown below:

- [Oracle Database Default Data Types](data-types/oracle.md).
- [DB2 Database Default Data Types](data-types/db2.md).
- [PostgreSQL Database Default Data Types](data-types/postgresql.md).
- [SQL Server Database Default Data Types](data-types/sql-server.md).
- [MariaDB Database Default Data Types](data-types/mariadb.md).
- [MySQL Database Default Data Types](data-types/mysql.md).
- [SAP ASE Database Default Data Types](data-types/sap-ase.md).
- [H2 Database Default Data Types](data-types/h2.md).
- [HyperSQL Database Default Data Types](data-types/hypersql.md).
- [Apache Derby Database Default Data Types](data-types/apache-derby.md).



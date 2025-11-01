# Supported Databases

HotRod supports the following Databases:

- Heap Oriented Databases:
    - [Oracle](./database-support/oracle.md)
    - [Db2 LUW](./database-support/db2-luw.md)
    - [PostgreSQL &amp; Aurora/PostgreSQL](./database-support/postgresql.md)
- Clustered-Index Oriented Databases:
    - [SQL Server](./database-support/sql-server.md)
    - [MariaDB](./database-support/mariadb.md)
    - [MySQL &amp; Aurora/MySQL](./database-support/mysql.md)
    - [SAP ASE (Sybase)](./database-support/sap-ase.md)
- In-Memory Databases:
    - [H2](./database-support/h2.md)
    - [HyperSQL (HSQLDB)](./database-support/hsqldb.md)
    - [Apache Derby](./database-support/apache-derby.md)

## Dialect Features

The databases listed above implement different subsets of the SQL Standard and they also implement
their own additional extensions. When it comes to the generated persistence layer not all the
possible features defined in the layer configuration may be available in each specific database,
edition, or version.

The following list enumerates the most important differences that you may encounter. There are more
subtle differences as well, but these cover the most common ones you may find, for example, when
migrating from one database to another.

### 1. Type Resolution Mechanics

The data type for each column is determined by the [Type Resolution Mechanics](../guides/type-resolution-mechanics.md). These mechanics use the Static Dialect Rules as well as the Runtime Dialect Rules. These rules vary per database. See the section of each database above to see the specific dialect rules.

### 2. LiveSQL Clauses and Expressions

Although LiveSQL tries to encompass the common clauses in the SQL Standard, not all LiveSQL
features are implemented by all databases. Depending on their version some databases
may implement more SQL clauses and SQL expressions than others. If parts of a LiveSQL are not
available in the specific database, LiveSQL will throw an exception indicating the details of the
clause or expression that cannot be translated into the current SQL dialect.

As anexample, the features that may not be implemented in all databases include (but it's not limited to):

- CTEs and Recursive CTEs
- Window Functions
- Full Outer Joins
- Offset and Limit Clauses
- Functions such as GROUP_CONCAT(), TRUNC(), EXTRACT(), etc.
- Row Locking Clauses

### 3. Sequences and Identities

Database sequences as well as identities may be implemented only partially or not at all by some
databases. The persistence layer generator validates the database engine and its version to
determine if a sequence or identity declaration can be implemented; if it cannot, it will
display an error message with the specifics.

### 4. Catalogs &amp; Schemas

Each database organizes database objects using schemas and catalogs. The schemas and catalogs are used
during the persistence layer generation, as a parameter indicating the default schema and in the layer configuration file to indicate non-default schemas for specific database objects. Namely:

- When specifying the default schema during the persistence layer generation
- When using multiple schemas and specifying tables and views in other schemas
- When implementing schema discovery rules

See [JDBC Catalogs &amp; Schemas](./jdbc-catalogs-and-schemas.md) for details on which ones
are supported by each database.

## Example of JDBC Drivers

For a list of example JDBC Drivers for each database see [JDBC Drivers Examples](./jdbc-drivers-examples.md).



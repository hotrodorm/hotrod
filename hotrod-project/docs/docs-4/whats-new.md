# What's New in HotRod 4


## What's New in HotRod 4.5

### 1. LiveSQL Pessimistic Locking

The SELECT ... FOR UPDATE locking feature was implemented in the LiveSQL syntax.

### 2. LiveSQL executeOne()

This convenience method was added to return single rows or VOs when it's known that the queries
will return one row at the most.


## What's New in HotRod 4.4

### 1. Torcs CTP

[Torcs CTP](./torcs-ctp/README.md) generates execution plans for visualization and analysis in the
[Check The Plan](https://checktheplan.com) web site, for the Oracle, DB2 LUW, PostgreSQL, and SQL Server databases.

### 2. LiveSQL Optimistic Locking

The INSERT, UPDATE, and DELETE statements of LiveSQL now return the number of inserted, updated, and deleted rows respectively, to support Optimistic Locking.


## What's New in HotRod 4.3

Version 4.3 adds the Torcs module to observe query executions at runtime and detect and analize slow ones.

### 1. Torcs

[Torcs](./torcs/README.md) inspects SQL query executions and records ranking(s) of query execution, that become available to the application. The ranking provides a sorted list of queries, with statistics, and also offer the ability to retrieve execution plans, should the application request it.

Torcs also generates execution plans for each database in multiple formats available in each database.

### 2. LiveSQL

LiveSQL MySQL and MariaDB rendering has been fixed for non-default schema tables and views.


## What's New in HotRod 4.2

Version 4.2 adds SQL set operators (UNION, INTERSECT, EXCEPT) and literal values to the LiveSQL syntax.

### 1. Set Operators

The [set operators](./livesql/syntax/set-operators.md) UNION [ALL], INTERSECT [ALL], EXCEPT [ALL], and combinations of them are now included in the LiveSQL syntax. This implementation also includes nesting set operators using parenthesis, managing default and explicit precedence. Finally, it also includes set ordering, offsets, and limiting.

### 2. Literal Scalars

[Literal scalar values](./livesql/syntax/literals.md) can now be included in the query using `sql.literal()`. Literals are easier to read in the SQL
clause and can provide information to the database optimizer to do its job
better.


## What's New in HotRod 4.1

HotRod 4.1 add subqueries to LiveSQL and adds a few extra enhancements to the LiveSQL syntax.

### 1. Subqueries

[Subqueries](./livesql/syntax/subqueries.md) include Scalar Subqueries, Table Expressions, Common Table Expressions (plain and recursive CTEs), Lateral Joins, in addition to IN/NOT IN, EXISTS/NOT EXISTS, and asymmetric operators. The extra syntax allows the developer to write complex SQL queries to adress more sophisticated data scenarios, without resorting to Nitro queries. Since writing LiveSQL code is faster than writing Nitro queries, this enhancement can significantly speed up development for queries of mid-level complexity.

### 2. POJO vs Bean VOs

By default, LiveSQL instantiate VOs as Spring beans. Now it's possible to instantiate them as POJOs instead, with the use of a system property. On the one hand, POJOs do not participate in the Spring context (to manage transactions for example), but on the other hand, they are faster than beans.

### 3. Other

LiveSQL changes:

- The `.asc()` method is not needed anymore when writing an ordering term. It's now assumed by default.
- Explicit parentheses added for expressions using `sql.enclose()`.
- All scalar values (including integer numbers and strings) are now parameterized to help database engines with query caching and optimization.


## What's New in HotRod 4.0

HotRod 4.0 includes several major features as well as a many of minor improvements.

## 1. General Functionality

This functionality affects all the modules.

### 1.1 Schema Discovery

Schema discovery can find tables and views in one or more schemas of the database
and generate the persistence layer for them automatically. It's enabled in No Config
mode and scans the current schema. Alternatively, it can be enabled in the configuration
to specify a list of schemas to scan. Rules can be defined with
a Name Solver and Type Solver to tailor the name generation of classes and
the type and names of properties in the persistence layer. See
[Schema Discovery](guides/schema-discovery.md) for details.

### 1.2 No Config Mode

The main configuration file can be omitted for rapid prototyping. In this mode, a minimal
setup of the Maven plugin produces a full persistence layer in no time. Also in this mode, HotRod 
generates the persistence layer by
scanning the current database schema. Sensible defaults are defined for all configuration
parameters for a standard persistence layer. See the [Hello World](guides/hello-world.md)
example to see it in action.

### 1.3 Support for Multiple Datasources

Support for multiple datasources was added. These datasources may belong to the same
database engine or to different ones. Each datasource generates a separated persistence
layer that is used seamlessly by all the HotRod modules, including CRUD, LiveSQL, and Nitro,
as well as all Spring features such as transaction management, AOP, etc. 
See [Using Multiple Datasources](guides/using-multiple-datasources.md)
for details.

### 1.4 Aurora/PostgreSQL and Aurora/MySQL Databases

These two new databases are now supported with all PostgreSQL and MySQL features.
HotRod recognizes them as such, and generates the persistence layer accordingly.

### 1.5 VOs Are Now Spring Beans

All VOs are retrieved as Spring Beans. This means they can use the Spring context
to handle Spring calls, autowiring, initiate/manage transactions, AOP, etc.

### 1.6 Enhanced Converters

Converters were enhanced and to receive the database `Connection`. This allows them
to manage special/exotic database types when sending them to and receiving them from
the database.

### 1.7 Configuration Default Settings

Sensible values were defined for all settings of the HotRod configuration file.
When a configuration setting is not specified a well-defined default behavior is included.
This feature goes hand in hand with the No Config mode.


## 2. LiveSQL

### 2.1 The SQL Wildcard *

The SQL wildcard (`*`) is now supported with the `star()` method. Apart from the traditional
functionality defined in the SQL Standard, this method is enhanced with filtering and aliasing.
Filtering decides which columns to keep using a lambda function.
Aliasing renames columns as needed also using another lambda function. See
[The SQL Wildcard](livesql/syntax/select-list.md#the-sql-wildcard) and, for aliasing in particular,
[Aliasing Wildcard Columns](livesql/syntax/select-list.md#aliasing-wildcard-columns).

### 2.2 INSERT, UPDATE, and DELETE

Core versions of these SQL Statements are now implemented to handle typical cases, to make full 
use the LiveSQL features such as usage of complex predicates when selecting data and complex
expressions when updating data. Simple subqueries are allowed in these expressions as well.

### 2.3 Queries without a FROM Clause

LiveSQL now automatically adds a FROM clause for databases that do not support queries without
it. Depending on the database the `DUAL` or `SYSDUMMY1` tables are used for this purpose behind
the scenes. These tables can also be used explicitly when desired. See
[Selecting Without a FROM Clause](livesql/syntax/selecting-without-a-from-clause.md).

### 2.4 LiveSQL Returns List&lt;Row> and Cursor&lt;Row>

To improve readability, LiveSQL changed the return type of the SELECT clauses and now returns
`List<Row>` and `Cursor<Row>` instead of `List<Map<String, Object>>` and `Cursor<Map<String, Object>>`.
This change has minimal side effects since `Row` subclasses `Map<String, Object>`.

### 2.5 Row Parser Implemented

The Row Parser funnctionality is available in the DAOs to reassemble one or more VOs from
a SELECT data row. Support for prefixes and suffixes is designed to be used in conjunction
with column aliasing to handle multiple VOs resulting from joined tables and views. See
[Aliasing Wildcard Columns](livesql/syntax/select-list.md#aliasing-wildcard-columns).


## 3. CRUD Module

### 3.1 DAO Methods Renamed

Most `select()` method variations are now named `select()`. This also applies to `update` and `delete`.
The variations are differentiated by different kind of parameters such as scalar values (for keys),
VOs (for `byExample`) and predicates for criteria searching.

### 3.2 Classic FK Navigation Enable By Default

Classic Foreign Key Navigation is now enabled by default and the `<classic-fk-navigation>` tag can 
be omitted now. In practice, all DAO methods to select related VOs using foreign keys are now included
by default in the DAOs and can be used out of the box.

### 3.3 DAOs Implement Interfaces

Java DAOs classes can be configured to implement predefined Java interfaces and take advantage
of extra Java functionality with them.

### 3.4 Simpler VOs

The `propertiesChangeLog` is now removed from VOs to facilitate quick Spring prototyping. Alhough
this extra property was useful in all `byExample()` functionality it was interfering with the JSON
renderer and parser. At the same the change log was largely rendered obsolete by the LiveSQL's
predicates that cover all these searched and other much more complex ones.


## 4. Nitro

### 4.1 Entity SELECTs Return Entity VOs

All `<select>` tags defined in entities (`<table>` and `<view>` tags) return VOs of the entity only.
the `vo` attribute is not accepted anymore in entity selects. On the other hand, `<select>` tags
outside entities (in `<dao>` tags) are fully free to return any type of VO.

### 4.2 Result Set Generation

Long overdue, Nitro now defaults to `result-set` generation. The `<select-generation>` tag can be 
omitted by default. The `create-view` Nitro strategy was key at the time when JDBC drivers were poorly
implemented but this is not the case anymore in all supported databases.


## 5. Minor Changes and Bug Fixes

Version 4 includes many minor changes and bug fixes are included. The following ones are worth mentioning:

- Configuration property `generator` removed.
- Reusing JDBC connection when using result-set processor.
- Converters fixed in `<dao>` tags.
- DTD declarations are now removed from config files.
- Converter's `java-intermediate-type` attribute renamed as `java-raw-type`.
- `primitives` subfolder removed in the location of mappers.
- Deprecated tag `<mybatis-configuration-template>` removed.
- LiveSQL's Oracle `.remainder()` function was fixed.
- The `<foreach>` tag bug is fixed and supports standard parameters now.




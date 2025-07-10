# What's New in HotRod 5

This is a major release of the HotRod ORM that includes much needed refactorings and upgrades.

As a side effect, this release breaks compatibility with previous releases. Consider that an upgrade from a previous version will require multiple changes to the source code of your application. Thourough testing of your converted application is recommended.

## What's New in HotRod 5.0

### 1. Brand New Persistence Layer

A new fully independent persistence layer was developed to replace the MyBatis dependency. MyBatis served us well but as of 2025 it was dated.

The new internal persistence technology is fully compatible with all Java versions from 8 to 24, with Spring 4.x, 5.x, and 6.x, and with Spring Boot 2.x and 3.x.

Unlike MyBatis this new persistence technology can resolve data types at runtime in addition to static ones resolved during the generation of the persistence layer. This critical for LiveSQL's to produce predictable column types.

### 2. Simplified HotRod Dependency

The HotRod library dependency is now much simplified, requiring a single library declaration. The Hello World example shows this change.

### 3. LiveSQL Runtime Type Solver

HotRod 5 implements a new Type Solver to read LiveSQL's SELECT query columns. Now a LiveSQL query selecting from a table will produce the same column types as the CRUD methods on the same table, including designated types, converter types, type-solver rules, and dialect default rules.

When a LiveSQL query includes columns from tables and views LiveSQL's Runtime Type Solver honors the static types resolved during the persistence layer generation, whichever rule was used to resolve them.

For non-entity columns LiveSQL's Runtime Type Solver decides their type according to the following order of precedence:

1. Runtime Designated types, using the .type(class/converter) method
2. Runtime Rule-based types (runtime JEXL), defined in the &lt;type-solver> tag
3. Runtime Dialect-based types, according to HotRod's database dialect
4. The default type defined by the JDBC driver

### 4. LiveSQL Predicates Can Use Converted Columns

Any converted column from a table or view can now be used as part of a predicate when selecting or processing data. Since a converted column bridges two data domains, only a limited number of operators are available for them. These include:

- = (equality)
- &lt;> (inequality)
- IN (*&lt;subquery>*), IN (*&lt;list>*)
- NOT IN (*&lt;subquery>*), NOT IN (*&lt;list>*)
- Assymmetric Operators
- IS NULL, IS NOT NULL

### 5. Simplified Configuration File

The configuration file supports new defaults that are simpler to use with minimal or even no configuration. This is particularly noticeable in the &lt;jdbc> tag that configures the details of the persistence layer.

### 6. Better Separation of Duties

The persistence layer is now reorganized into separate packages to clearly visualize the separation of duties. The following classes are now located by default in separate packages:

- DAOs for CRUD tables and view, as well as extra DAOs for non-entity queries
- Layout classes that represent the structure of tables and views in the CRUD layer
- Model classes that add behavior to the CRUD objects

An extra LayerConfiguration stores the runtime rules for LiveSQL's Runtime Type Solver.

### 7. New Rankings in Torcs

Torcs now includes the Highest Impact Queries Ranking that is tailored to ranks queries by combining the accumulated response times. This is probably one of the most important metrics when it comes to optimization and this ranking may become the default one in future versions.

The New Highest Frequency Queries Ranking implemented to detect extremely frequent queries, even if they are fast.

### 8. New Optimistic Locking Strategies

The CRUD module now implements the Timestamp and Full Row Check strategies, in addition to the existing Version Number strategy. These new options can come in handy, especially when the affected tables cannot accept more columns.

### 9. Using java.time By Default

This was long overdue. The java.time classes are now resolved by default for DATE, TIME, TIMESTAMP columns. This is valid for the static and runtime type solvers. All database dialects have been updated to default to java.time instead of java.sql now. LiveSQL implement table columns with these types and can use these types in predicates as well.

Old java.sql classes can still be used by explicitly designating them.

### 10. Enhanced Logging

Logging of the queries used by the CRUD methods and other DAOs is now available at the DEBUG and TRACE levels of the corresponding DAOs. The logging always includes the SQL query to be executed. Based on the logging level it also includes the parameters and the runtime column types.

This logging is enabled by the normal logging framework specified at the DAO class or method level.

### 11. Cursors Revamped

The Cursor functionality was fully rewritten for CRUD, Nitro, and LiveSQL for higher performance, and to function correctly in a variety of scenarios.

### 12. Improved Nitro Queries

DynamicSQL tags in Nitro can now be placed outside the complement tag as well as inside it. This allows the creation of highly dynamic queries that include tables and result set columns conditionally at runtime.

The Nitro injection syntax was changed to `$INJECT{expression}` instead `${expression}` to prevent unintentional SQL Injection.

**Note**: Nitro Graph Queries are yet not implemented in HotRod 5.0.

### 13. Previewing LiveSQL

LiveSQL preview now shows SQL, expanded parameters details, and data types to read each column.

- The SQL code includes the exact resolved query that is executed in the database
- The parameters are fully expanded to show all instances of each parameter
- The data types for the resulting columns provide all details to understand the rule that was applied to determine the data type for a column


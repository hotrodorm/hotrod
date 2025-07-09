# What's New in HotRod 5

This is a major release of the HotRod ORM that includes much needed refactorings and upgrades.

As a side effect this release breaks compatibility with previous releases. Consider that an upgrade from a previous version will require multiple changes to the source code of your application.

## What's New in HotRod 5.0

### 1. Easy Compatibility From Java 8 to Java 24

The MyBatis dependency was removed in favor of an internal persistence technology to run in any modern Java version. The HotRod library dependency is now much simplified, requiring a single library declaration.

The new internal persistence technology is fully compatible with all Java versions from Java 8 to Java 24.

### 2. Simplified Configuration File

The configuration file supports new defaults that are simpler to use with minimal or even no configuration.

Also, many option were simplified and better default values were defined.

### 3. Simplified Persistence Layer

The persistence layer is now reorganized into separate packages to clearly visualize the separation of duties. The following classes are now located by default in separate packages:

- DAOs for CRUD tables and view, as well as extra DAOs for non-entity queries
- Layout classes that represent the structure of tables and views in the CRUD layer.
- Model classes that add behavior to the CRUD objects.

An extra LayerConfiguration stores the runtime rules for the Runtime Type Solver.


### 4. Using java.time By Default

This was long overdue. The java.time classes are now resolved by default for DATE, TIME, TIMESTAMP columns. This is valid for the static and runtime type solvers. All database dialects have been updated to default to java.time instead of java.sql now. LiveSQL implement table columns with these types and can use these types in predicates as well.

Old java.sql classes can still be used by explicitly designating them.

### 5. Simplified Logging

Logging of the queries used by the CRUD methods and other DAOs is now available at the DEBUG and TRACE levels of the corresponding DAOs. The logging shows the full rendered query, optionally with its parameters.

This logging is enabled by the normal logging framework specified at the DAO class or method level.

### 6. New Optimistic Locking Strategies

The CRUD module now implements the Timestamp and Full Row Check strategies, in addition to the existing Version Number strategy. These new options can come in handy, especially when the affected tables cannot accept more columns.

### 7. Cursors Revamped

The Cursor functionality was fully rewritten for CRUD, Nitro, and LiveSQL for higher performance, and to function correctly in a variety of scenarios.

### 8. Improved Nitro Queries

DynamicSQL tags in Nitro can now be placed outside the complement tag as well as inside it. This allows the creation of highly dynamic queries that include tables and result set columns conditionally at runtime.

The Nitro injection syntax was changed to `$INJECT{expression}` instead `${expression}` to prevent unintentional SQL Injection.

**Note**: Nitro Graph Queries are not implemented in HotRod 5.0.

### 9. LiveSQL Runtime Type Solver

HotRod 5 implements a new Type Solver to read LiveSQL's SELECT query columns. Since these columns are defined at runtime the Static Type Solver could not resolve their data types correctly in HotRod 4.

The Runtime Type Solver can use designated types, rule-based types (runtime JEXL), dialect-based types, and static-rule types (static JEXL), in this order of precedence. This makes it possible to read table columns with their correct data type, including defined converters.


### 10. Previewing LiveSQL

LiveSQL preview now shows SQL, expanded parameters details, and data types to read each column.

- The SQL code includes the exact resolved query that is executed in the database.
- The parameters are fully expanded to show all instances of each parameter.
- The data types for the resulting columns provide all details to understand the rule that was applied to determine the data type for a column.

### 11. Enhanced Rankings in Torcs

Torcs now includes the Highest Impact Queries Ranking that is tailored to ranks queries by combining the accumulated response times. This is probably one of the most important metrics when it comes to optimization and this ranking may become the default one in future versions.

The New Highest Frequency Queries Ranking implemented to detect extremely frequent queries, even if they are fast.


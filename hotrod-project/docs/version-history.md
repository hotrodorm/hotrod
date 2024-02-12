# HotRod Version History

Libraries for these versions can be downloaded from any Maven Repository mirror. For example, they can be found in Maven Central Repository at [org.hotrodorm.hotrod](https://search.maven.org/search?q=g:org.hotrodorm.hotrod).

## 4.5.3:

- Nitro:
    - Improving JSON rendering of graphs queries.
    - Fixing search of entities in current schema for graph queries.

## 4.5.2

- LiveSQL:
    - Implemented .executeOne() that returns a single row.
    - Converters handled correctly by the parseRow() methods.

## 4.5.1

- LiveSQL:
    - Added FOR UPDATE to select by criteria.
    - The UPDATE statement now accepts the sql.NULL value in the SET clause.
    - Fixing messaging for forUpdate() in Derby and HyperSQL.

## 4.5.0

- LiveSQL:
    - Pessimistic locking using SELECT ... FOR UPDATE (and variations of it).
    - NULLIF() implemented for completeness.
    - Fixed aliases in the UPDATE statement.
    - Fixed SELECT by criteria ordering and limiting.

## 4.4.1

- LiveSQL:
    - INSERT, UPDATE, and DELETE now return the number of inserted, updated, and deleted rows respectively, to support Optimistic Locking in LiveSQL.

## 4.4.0

- Torcs CTP:
    - Torcs CTP generates plans for [Check The Plan](https://checktheplan.com) visualization and analysis for the Oracle, DB2 LUW, PostgreSQL, and SQL Server databases.

- Torcs:
    - DB2 plan improved.

## 4.3.3

- Torcs:
    - Improving API names.

## 4.3.2

- Torcs:
    - Fixing bug in Torcs aspect.

## 4.3.1

- Torcs:
    - Adding generic execution plan variations for each database.

## 4.3.0

- Torcs:
    - Gathers and consolidates statistical data of queries run by the application, with the aim of detecting slow running queries.
    - Three built-in rankings (highest response time, initial queries, latest queries).
    - Execution plans extraction for queries for all databases (except Derby).
    - Ability to add custom observers for any custom purpose.
    - Ability to log all/some query executions.
    - Multi-data source aware.
    - Auto-start feature.
    - Saves rankings using Excel (XLSX) format.

- LiveSQL:
    - Fixing LiveSQL's rendering of catalog/schema for MySQL and MariaDB.


## 4.2.0

- LiveSQL:
    - Set Operators: All variants of them, including UNION [ALL], INTERSECT [ALL], and EXCEPT [ALL], along with nesting using parenthesis, default and explicit precedence, as well as set ordering, offsets, and limiting.
    - Literal scalars: Using `sql.literal()` plain, non-parameterized values can be included in a SQL query.

## 4.1.1

- Core:
    - Applying bug fixes to Spring beans instantiation.

## 4.1.0

- LiveSQL:
    - Subqueries. LiveSQL now includes Table Expressions, Scalar Subqueries, CTEs (Common Table Expressions), Recursive CTEs, and Lateral Joins. Before this version subqueries for only `IN/NOT IN`, `EXISTS/NOT EXISTS`, and assymmetric operators were implemented.
    - Explicit parenthesis added for expressions using `sql.enclose()`.
    - All literals (including integer numbers and safe strings) are now parameterized to help with query optimization.
    - `LAG()` and `LEAD()` fixed (when used with one and two parameters only).
    - The `.asc()` method is not needed anymore when writing an ordering term. It's now assumed by default.

- Core:
    - VOs are by default instantiated as Spring beans (with full Spring context), but can be configured to be instantiated as plain POJOs (higher performance, no Spring context) using the `hotrod.vo.instantiation` property.

## 4.0.0

- Core:
    - No-config mode implemented. The `hotrod.xml` file can now be omitted for rapid prototyping.
    - Schema discovery implemented. Tables and views are automatically discovered in one or more schemas.
    - Support for multiple datasources.
    - Aurora/PostgreSQL and Aurora/MySQL supported.
    - VOs are instantiated as Spring beans rather than as POJOs.
    - Enhanced Converters.
    - Default values for the `<mappers>` and `<daos>` tags. They can be now omitted in the configuration file.
    - Configuration property `generator` removed.
    - Reusing JDBC connection when using result-set processor.
    - Converters fixed in `<dao>` tags.
    - DTD declarations are now removed from config files.
    - Converter's `java-intermediate-type` attribute renamed as `java-raw-type`.
    - `primitives` subfolder removed in the location of mappers.
    - Deprecated tag `<mybatis-configuration-template>` removed.
- LiveSQL:
    - Wildcard (`*`) implemented with the `star()` method, enhanced with filtering and aliasing.
    - Core versions of INSERT, UPDATE, and DELETE implemented.
    - Row parser implemented with support for joins. Rows can can now be retrieved as tuples of tables/views.
    - Added DUAL and SYSDUMMY1 for queries without tables.
    - LiveSQL now returns `List<Row>` and `Cursor<Row>` instead of `List<Map<String, Object>>` and `Cursor<Map<String, Object>>`.
    - Oracle MOD() function fixed.
- CRUD:
    - Main methods renamed.
    - Classic FK Navigation is now enabled by default. The `<classic-fk-navigation>` tag can be omitted now.
    - Added "implements" for DAO classes.
    - `propertiesChangeLog` is now removed from VOs to facilitate quick Spring prototyping.
- Nitro:
    - Select methods in entity tags (`<table>` and `<view>` tags) are restricted to return the corresponding entity VOs only. They don't allow the 'vo' attribute anymore.
    - Nitro defaults to `result-set` generation now. The `<select-generation>` tag can be omitted by default.
    - Dynamic SQL's `<foreach>` fixed.

## 3.5.2

- Core: Fixing Spring instantiation for VOs.
- Core: Fixing access to Spring properties (using getters instead of members dircetly).

## 3.5.1

- LiveSQL: Backport of filtering and aliasing for the star() method.
- LiveSQL: Backport Oracle MOD() function bugfix.
- LiveSQL: Backport of row parser with support for joins. Tuples can now be retrieved.
- DynamicSQL: Backport of `<foreach>` bugfix.
- Core: Backport of default values for the `<mappers>` tag. It can be now omitted in the configuration file.
- Core: Backport of VOs instantiated as Spring beans.


## 3.5.0

- CRUD: Backport of "implements" and DAO wiring for VOs.
- LiveSQL: Backport of method wildcard `*`; method star().

## 3.4.14

- Core: Adding support for tables with identical names in different schemas.

## 3.4.13

- Core: Fixing treatment of identical Oracle metadata in multiple schemas. In this case the DBA copied a schema as a backup and 
this created confusion in HotRod's metadata. 

## 3.4.12

- CRUD: Removing `propertiesChangeLog` in VOs.
- LiveSQL: Adding row parser.
- CRUD: Fixing `updateByExample()`.

## 3.4.11

- CRUD: Fixing CRUD insert.

## 3.4.10

- CRUD: Removing `abstract` from model VOs.

## 3.4.9

- Full documentation for version 3 and 4 in the GitHub repo/pages.

## 3.4.8

- Adding support for H2 version 2.x.

## 3.4.7

- DAO references to other DAO are now marked as `@Lazy` to deal with circular references (parent-children). Spring Boot prototypes don't like them when eager-loaded.

## 3.4.6

- Migration to GitHub.
- Improved Spring Boot Archetype.

## 3.4.5

- New Spring Boot archetype! It generates a running full-blown project with a single command line: Includes
  Spring Boot, HotRod, REST Controllers, OpenAPI3, Debbie, and Sentinel.
- Fixing `<enum>` tag error.

## 3.4.4

- Log4j fully removed from runtimes (hotrod-jar and hotrod-livesql.jar). Now Log4j is only used in the generator.

## 3.4.3

- Log4J upgraded to 2.17.1.

## 3.4.2

- Fixing fragment processing.
- Adding HotRod Services infrastructure to facilitate future plugins.
- Removed deprecated first-level `<select>` tag.
- Removed old generators.
- Significant refactoring by separating metadata from generator.
- Code clean up.

## 3.4.1

- Improving select tag messaging for SQL errors.
- Fixing default catalog/schema for nitro module.
- Adding generation output and summary line.

## 3.4.0

- Classic FK navigation can now be enabled on a per-table basis.
- Cursors implemented for LiveSQL, Nitro Selects, selectByExample(), and selectByCriteria().
- New `<select>` tag processor "result-set" offers many benefits compared to traditional "create view" processor.
- New `<name-solver>` tag, automatically names tables, views, and columns according to regex naming rules.
- LiveSQL function infrastructure: developers can now add built-in and also user-defined functions to LiveSQL queries.
- LiveSQL dialect can be designated at runtime using application.properties, with fallback to discovery mode.
- LiveSQL fully typed SQL CASE and OVER clauses.
- LiveSQL RANGE window frame implemented for numbers (date/time are not yet supported since we don't model intervals yet).
- Fully independent Spring MyBatis generator.
- REST metrics implemented (experimental).
- Added "force-jdbc-type-on-write" attribute to `<type-solver>` functionality.
- Added extra prefixes and suffixes for Nitro DAOs and VOs.

## 3.3.3

- Implemented classic FK navigation (globally enabled or disabled).

## 3.3.2

- Maintenance release.

## 3.3.1

- LiveSQL mapper is now implemented as a Java mapper; no XML file is generated anymore.
- All HotRod Spring configuration can now be made using Java annotations.
- Experimental FK navigation.

## 3.3.0

- All functionality tested with SpringBoot release and web components.
- Removed MyBatis Configuration file; replaced by mapper scanning annotations.
- Experimental SQL metrics developed.

## 3.2.3

- Log4J upgraded to 2.17.1 in the generator.
- Log4j fully removed from runtimes (hotrod-jar and hotrod-livesql.jar). Now Log4j is only used in the generator.

## 3.2.2

- Fixed connection exception reporting.
- Cursors implemented for selectByExample().
- AOP tested with experimental SQL metrics.

## 3.2.1

- Removing "final" to VO getters and setters.
- Experimental implementation of table inheritance.

## 3.2.0

- Simplified hotrod.xml configuration file with many sensible default values.
- Standard archetype implemented for a Spring, Maven, MyBatis, command-line app.

## 3.1.0

- Maven Plugin released.
- Full `<type-solver>` implemented with OGNL logic.
- TXT column export implemented.
- XLSX column export implemented.
- HotRod PURGE operation implemented to remove residual temporary views.

## 3.0.5

- Fixing graph queries VOs prefixes and suffixes.

## 3.0.4

- Fixing prefixes and suffixes for abstract VOs.
- Removed default LiveSQL preview in logging. Can be enabled by DEBUG logging level.

## 3.0.3

- Adding tar.gz packaging for Docker releases.
- Fully dockerized example: web + hotrod + docker + java11 + maven + spring.

## 3.0.2

- Added Spring, Maven, web POC.

## 3.0.1

- Fixing Java 9 JAXB implementation.

## 3.0.0

- HotRod released to Maven Central Repository.

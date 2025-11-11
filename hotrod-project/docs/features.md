# Highlights of HotRod 5 ORM

As developers ourselves, we built HotRod because we were dissatisfied with the available ORM solutions at the time.

Our initial goal for HotRod was to accelerate the app development process. This included enabling quick prototyping and offering foundational support for the app's gradual growth. Essentially, during the project's early stages, schema discovery provided a competitive edge to the team; as the project expanded, features like LiveSQL, Dynamic SQL, and Nitro facilitated more complex functionality with minimal effort.

Some time later we recognized the need to lower the operational costs associated with running apps. Beyond the coding phase, we aimed to improve debugging, testing, support, maintenance, and, most importantly, the stability and performance of the applications themselves. This realization led us to incorporate features such as Torcs, SQL injection safety, API simplification, compile-time syntax checks, and many others.

## Signature Features

This is a list of distinctive features that we don't find in most ORM competitors. We believe HotRod excels in these areas.

### Entity Tuples

SQL queries can retrieve fully separated tuples when joining two or more tables or views. This includes completely modeled entities with naming conventions and column type resolutions, as well as additional on-the-fly computed columns, all of this with compilation-time syntax checks as you type.

### Schema Discovery

The discovery mechanism generates a fully-functional persistence layer in minutes. By specifying an existing database schema HotRod can discover all the tables and views in it and generate the fully-named and fully-typed persistence layer. This layer is utilized by CRUD operations and in LiveSQL queries, allowing for immediate prototyping. The schema discovery can also operate across multiple schemas, utilizing rules with additional configuration.

### Advanced CRUD

Once the initial prototyping is complete, CRUD continues to offer advanced functionality to support the application’s growth. This includes flexible predicates, advanced ordering, query pagination, multiple strategies for optimistic locking, and more.

### Configurable Data Type Resolution

The flexible data type resolution engine employs SQL dialect-based, custom rule-based, designated data types, and JDBC data types to determine the types for all tables and query columns. The Data type Resolution grants you full control over the data types used in database interactions.

### Updatable Entity Model

Entities for tables, views, and Nitro SELECTs separate their *Layout* (the structure of tables and queries) from their *Model* (custom behavior and implementation). This ensures that your hand-written domain logic remains safe from the ever-changing nature of table and query structures. When the persistence layer is updated the layout classes are refreshed to reflect the latest database structures while preserving your custom domain logic within the separated model classes.

### Feature-Rich LiveSQL

LiveSQL allows you to create flexible queries from your application, complete with compilation-time syntax checks as you type. The comprehensive syntax covers SELECT, INSERT, UPDATE, and DELETE SQL statements, in addition to all major SQL clauses and expressions, window functions, SQL meta-expressions, subqueries, plain and recursive CTEs, semi-joins, self-joins, lateral joins, row locking, and more.

### Built-In Low Performance Query Detection

Torcs automatically monitors all queries executed by your application and ranks them in multiple ways. By observing these rankings, you can identify bottlenecks, execution patterns, query execution plans, and error rates. Understanding the actual runtime behavior of your app &mdash; rather than the predicted one &mdash; is critical to addressing any performance issues in the persistence layer. All of this without the assistance of an expensive or elusive DBA.

### Advanced Dynamic SQL

This feature allows for flexible query definitions by declaring rules for including or excluding SQL segments using JEXL syntax. In short, the same dynamic query can take multiple forms when executed with different runtime parameters. DynamicSQL incorporates basic constructs such as IF and CHOOSE, as well as advanced features like TRIM, FOREACH, BIND, and nesting of SQL segments. Native SQL can always be appended to any SQL segment as needed.

### SQL Injection Safety

CRUD and LiveSQL queries are entirely free from SQL injection vulnerabilities. Nitro does allow SQL injection &mdash; although always disabled by default &mdash; if explicitly enabled on a per-query basis when the specific use case warrants it.

### Out-of-the-box Cursors

CRUD, Nitro, and LiveSQL queries can stream data rather than materializing large datasets in memory before consumption. This capability can significantly reduce the memory footprint of your application, improve performance, and optimize the use of hardware resources.

## World-Class Features

This section highlights the features of HotRod that are expected from any high-end Object-Relational Mapping (ORM) solution.

### LiveSQL Live Syntax Check

LiveSQL employs a builder pattern that enables the compiler to check the syntax of queries in real-time as you type. This prevents issues such as entering clauses out of order or using illegal expressions &mdash; for instance, placing an ORDER BY clause before a WHERE clause or attempting to compare incompatible types like a DATE value against a BOOLEAN value. Trivial errors are detected immediately, enhancing overall efficiency.

### Automatic Dialect Translation

Both CRUD and LiveSQL queries are automatically translated to the specific SQL syntax for each database engine. This translation is organized by data source, accommodating applications that use multiple databases. While your code consistently utilizes CRUD and LiveSQL queries in a standard format, each query is automatically rephrased by the corresponding dialect when executed. This approach not only simplifies coding but also lessens the migration effort if you decide to switch to a different database in the future.

### Converters

You can define column converters for any table, view, or SELECT query columns. These converters perform on-the-fly data conversions between different data domains. For example, a CHAR column in the database may represent a BOOLEAN value, or an INT might signify a discrete set of statuses represented as an ENUM in your application. The converters are bidirectional &mdash; they handle data conversions both when reading from and writing back to the database. A simpler version of converters, known as *data casting*, is also supported for compatible types, such as INTEGER and DOUBLE.

### SQL-Free Application Code

The persistence layer stores and executes all SQL queries, freeing your application code from directly typing SQL syntax. This allows you to focus on implementing business logic instead of writing and debugging SQL queries.

### Support for World-Class OLTP databases

The persistence layer supports leading relational Online Transaction Processing (OLTP) databases, encompassing both commercial and free options. It includes heap-based, clustered-index-based, and in-memory databases.

### Wide Tech Stack Compatibility

Currently, HotRod is developed for Java 25, Spring 6.x, and Spring Boot 3.x, with backward compatibility extending to Java 8, Spring 3.x, and Spring Boot 2.x.

### No-Config Mode for Easy Prototyping

The No-Configuration mode allows you to run HotRod without any layer configuration. Simply point it to a running database schema to discover its details and start prototyping immediately.

### Multi-datasource Aware

Your application can seamlessly connect to one or more databases, regardless of their version, edition, or brand. The persistence layer automatically selects the correct SQL dialect for each database, ensuring your app code remains free from specific customizations.

### Flexible Organization of the Persistence Layer

While sensible default settings are included, the layer configuration allows for customization with a rich set of options that cover base and extra folders, packages, sub-packages, prefixes, suffixes, and more.

### Pessimistic Row Locking

To facilitate thread-safe operations, customizable pessimistic row locking mechanisms are available by default in CRUD and LiveSQL.

### Multiple Optimistic Locking Strategies

To address real-world scenarios, three distinct optimistic locking strategies are implemented. These are particularly useful in situations involving less-than-ideal table structures or when the development team lacks the privileges or authority to modify existing database table structures.

### Native SQL

You can enhance any Nitro query with Native SQL. Using native dialect SQL extensions can be particularly beneficial for leveraging unique features available only in specific databases or when your application requires optimal performance from the database engine.

### Configuration Partitioning

For large applications or projects involving multiple teams, the layer configuration can be partitioned using fragments. These fragments (and sub-fragments) allow for the separation and tailoring of configuration for sub-modules of the application.

### Auto-generated Identities and Sequences

The persistence layer automatically discovers identity-generated primary keys. Sequences can be explicitly declared for each table they affect.

### Previewing LiveSQL

You can always view the final translated SQL for the dialect before execution. This transparency allows you to inspect the actual SQL query, which is invaluable for debugging, checking specific SQL extensions, or verifying parameter values and the exact list of return query columns and their types. This feature is distinct from SQL logging, which can be activated separately by the operations team.

### SQL Logging

Configurable SQL Logging allows the operation team to enable or disable logging of a deployed app, by changing the runtime logging configuration of it.

### Selecting Sequences

In certain scenarios, your application may need to select values from database sequences unrelated to tables. These sequences are typically used to generate unique values for APIs, file naming, and other purposes.

### Composite Primary Keys

Both composite and simple primary keys are fully supported across all CRUD functionalities. Although considered a basic feature, many ORMs fail to implement the former.

## Other Features

Of course there are many other features that are not included in this list. The main goal of this page is brevity. See the [HotRod 5 Documentation](./docs-5/README.md) for the full details of what HotRod can do.

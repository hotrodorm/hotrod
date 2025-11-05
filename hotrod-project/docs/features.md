# Highlights of HotRod 5 ORM

As developers ourselves we started HotRod when we decided that we didn't like any of the available ORM solutions at the time.

The initial goal of HotRod was to speed up the initial development of an app. This included quick prototyping and also providing support for the gradual growth of the app. In short, when a project is just starting schema discovery gives an edge to the team; then, when the project keeps on growing LiveSQL, Dynamic SQL, and Nitro can implement more complex functionality with little effort.

Some time later we realized we also wanted to reduce the operational cost of the apps. Apart from the coding phase itself we decided to consider the debugging, testing, support, maintenance, and above all the stability and performance of the apps themselves. That's when Torcs, SQL Injection safety, API simplification, compile-time syntax checks, and many others features came into play.

## Signature Features

This is a list of distinctive features that we don't find in most ORM competitors. We believe HotRod shines when it comes to these features.

**Entity Tuples**. SQL queries can retrieve fully-separated tuples for joined tables and views. This includes fully-modeled entities with naming rules and column type resolution, as well as additional on-the-fly computed columns, all with compilation-time syntax check while you type.

**Schema Discovery**. The discovery mechanism generates a fully-functional persistence layer in minutes. By specifying an existing database schema HotRod can discover all the tables and views in it and generate the fully-named and fully-typed persistence layer. This persistence layer is used by CRUD and in LiveSQL queries so you can start prototyping right away. The schema discovery can also work on multiple schemas using rules with extra configuration.

**Advanced CRUD**. Once the first rounds of prototyping are complete CRUD still offers advanced functionality to continue supporting the growth of the application, including flexible predicates, advanced ordering, query pagination, multiple strategies of optimistic locking, and more.

**Configurable Data Type Resolution**. The flexible data type resolution engine uses SQL dialect-based, custom rule-based, designated data types, and JDBC data types to decide the types of all tables and query columns. The persistence layer gives you full control of the data types you use when interacting with the database.

**Segregated Entity Modeling**. The entities for tables, views, and Nitro SELECTs separate their Layout (structure of the tables and queries) from their Model (custom behavior and implementation). This way their custom, hand-written domain logic is fully protected from the ever-changing nature of the tables and query structures. The persistence layer always updates the Layout classes to keep them up-to-date with the latest structure of the tables, views, and queries, while keeping your custom domain logic intact in the Model classes.

**Feature-Rich LiveSQL**. LiveSQL allows you to add flexible queries from your app with compilation-time syntax check while you type. The comprehensive syntax includes the SELECT, INSERT, UPDATE, and DELETE SQL statements, as well as, all major SQL clauses and expressions, window functions, SQL meta-expressions, subqueries, plain and recursive CTEs, semi-joins, self-joins, row locking, and more.

**Built-In Slow Performance Query Detection**. Unlike other ORMs HotRod includes the built-in Torcs layer that watches all the queries run by your application and ranks them in different ways. By observing these rankings you can identify the bottlenecks of your application, their execution patterns, their execution plans, their error rates, etc. The understanding &ndash; not of the predicted &ndash; but of the *actual* runtime behavior of your app is the first step to address its bottlenecks.

**Advanced Dynamic SQL**. Feature-full query definition that declares rules for inclusion or exclusion of SQL segments using JEXL syntax. In short, the same dynamic query will take many shapes when executed with different runtime parameters. DynamicSQL includes basic constructs such as IF/CHOOSE to advanced ones such as TRIM/FOREACH/BIND and nesting of SQL segments. Native SQL can always be added to any SQL segment.

**SQL Injection Free**. Free of SQL Injection in all CRUD and LiveSQL queries. Only Nitro offers SQL Injection &ndash; always disabled by default &ndash; if deliberately enabled on a per-query basis when the specific use case requires it.

**Out-of-the-box Cursors**. All CRUD, Nitro, and LiveSQL queries can stream data instead of materializing big data sets in memory before consuming them. This can dramatically reduce the memory footprint of your app, improve the app performance, and optimize the use of hardware resources.

## World-Class Features

This is a list of HotRod features that you would expect from any typical high-end ORM.

**LiveSQL Live Syntax Check**. LiveSQL uses a builder pattern that allows the compiler to check the syntax of queries while you type. This prevents you typing clauses out of order or illegal expressions such as typing an ORDER BY clause before a WHERE clause or trying to compare incompatible types such as a DATE value against a BOOLEAN value. Trivial errors are detected immediately.

**Automatic Dialect Translation**. The CRUD and LiveSQL queries are automatically translated to the specific SQL available in each database engine. This is segregated by data source to account for an application using multiple different databases. While your code always uses CRUD and LiveSQL queries typed in a standard manner each dialect automatically rephrases them when they are executed. Apart from simplifying the coding of your app, this strategy also reduces the migration effort should you choose to migrate to a different database in the future.

**Converters**. Column converters can be defined for any table, view, or even SELECT query columns. Converters perform on-the-fly data conversion between different data domains. For example, when a database CHAR is used to represent a BOOLEAN value, or when an INT value is used to represent a discrete status that your app represents as an ENUM. The converters are bidirectional so they convert data while reading from the database and also while writing back to it. The simpler variation of converters &ndash; *data casting* &ndash; is also supported when converting between compatible types such as INTEGER and DOUBLE and others.

**SQL-Free Source Code**. The persistence layer defines and runs all the SQL code instead of your application, making your source code SQL-free. You can freely dedicate your code to implement the business logic instead of typing and debugging SQL syntax.

**Support for World-Class OLTP databases**. The persistence layer supports the leading relational OLTP databases, including commercial and free ones. It covers heap-based, clustered-index-based, and in-memory databases.

**Wide Tech Stack Compatibility**. Currently developed for Java 25, Spring 6.x, and Spring Boot 3.x, it also includes compatibility back to Java 8 and Spring 3.x, and Spring Boot to 2.x.

**No-Config Mode for Easy Prototyping**. The No-Configuration mode can run with no layer configuration at all. Just point HotRod to a running database schema to discover its details and start using it in no time.

**Multi-datasource Aware**. Your app can seamlessly use one or more databases of the same or different version, edition, or even brand. The persistence layers will automatically switch on the correct SQL dialect for each one, so your app code is always free from specific customizations.

**Flexible Organization of the Persistence Layer**. Even though it includes sensible default settings, the layer configuration can customize your persistence layer with a rich set of settings that cover base and extra folders, packages and sub-packages, prefixes, suffixes, and more.

**Pessimistic Row Locking**. To implement thread-safe operations customizable pessimistic row locking mechanisms are available by default in CRUD and LiveSQL, and Nitro.

**Multiple Optimistic Locking Strategies**. To deal with real-world scenarios three separate optimistic locking strategies are implemented. These can be especially useful when the development needs to work with less-than-ideal table structures or when the development team does not have the ability or permission to modify the database table structures at will.

**Native SQL**. Native SQL can be added to any Nitro query. Native dialect SQL extensions can prove very beneficial in specific cases, specially when it comes to exploit uncommon features available only in some databases, or when your app needs to squeeze performance from the engine.

**Configuration Partitioning**. For large apps or multiple teams per project, the layer configuration can be partitioned using fragments. These fragments (and sub-fragments) can be used to separate and tailor the configuration of sub-modules of the app.

**Auto-generated Identities and Sequences**. The persistence layer automatically discovers identity-generated primary keys. When it comes to sequences, these can be used by explicitly declaring them for each table they affect.

**Previewing LiveSQL**. The final translated SQL for the dialect can always be viewed before it's run. This gives your app full access to inspect the actual SQL query being run, if you need to debug the app, to check specific SQL extensions, or if you need to see the specific parameter values or query columns returned by a query. This is different from SQL logging that can be activated separately by the operator of the application.

**SQL Logging**. Configurable SQL Logging allows the operation team to enable or disable logging of a deployed app, by changing the runtime logging configuration of it.

**Selecting Sequences**. Sometimes your app needs to select values from database sequences that are unrelated to tables. These are typically used to produce unique values for APIs, for file-naming, and other purposes.

**Composite Primary Keys Supported**. Composite and simple primary keys are fully supported in all CRUD functionality. Although still considered a basic feature, many ORMs fail to implement this feature.

## Other Features

Of course there are many other features that are not included in this list. The main goal of this page is brevity. See the [HotRod 5 Documentation](./docs-5/README.md) for the full details of what HotRod can do.

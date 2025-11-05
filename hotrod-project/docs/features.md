# Highlights of HotRod 5 ORM

As developers ourselves we started HotRod when we decided that we didn't like any of the available ORM solutions at the time.

The initial goal of HotRod was to speed up the initial development of an app. This included quick prototyping and also providing support for the gradual growth of the app. In short, when a project is just starting schema discovery gives an edge to the team; then, when the project keeps on growing LiveSQL, Dynamic SQL, and Nitro can implement more complex functionality with little effort.

Some time later we realized we also wanted to reduce the operational cost of the apps. Apart from the coding phase itself we decided to consider the debugging, testing, support, maintenance, and above all the stability and performance of the apps themselves. That's when Torcs, SQL Injection safety, API simplification, compile-time syntax checks, and many others features came into play.

## Signature Features

This is a list of distinctive features that we don't find in most ORM competitors. We believe HotRod shines when it comes to these features.

**Entity Tuples** retrieve fully-separated tuples for joined tables and views in SELECT queries. This includes fully-modeled entities with naming rules and column type resolution, as well as additional on-the-fly computed columns, all with compilation-time syntax check while you type.

**Schema Discovery**. The discovery mechanism generates a fully-functional persistence layer in minutes. By specifying an existing database schema HotRod can discover all the tables and views in it and generate the fully-named and fully-typed persistence layer. This persistence layer is used by CRUD and in LiveSQL queries so you can start prototyping right away. The schema discovery can also work on multiple schemas using rules with extra configuration.

**Advanced CRUD**. Once the first rounds of prototyping are complete CRUD still offers advanced functionality to continue supporting the growth of the application, including flexible predicates, advanced ordering, query pagination, multiple strategies of optimistic locking, and more.

**Advanced Data Type Resolution**. A flexible data type engine uses SQL dialect-based, custom rule-based, and designated data types to decide the types of all tables and query columns. The persistence layer gives you full control of the data types you use when interacting with the database.

**Layout and Model Modeling**. The entity modeling always separates the structure of the tables from their custom behavior. The custom, hand-written domain logic is fully protected from the ever-changing nature of the tables and query structure. The persistence layer always updates the Layout classes to keep them up-to-date with the latest structure of the tables, views, and queries, while keeping your custom logic intact in the Model classes.

**Feature-Rich LiveSQL**. Write LiveSQL queries from your app with compilation-time syntax check while you type. The comprehensive syntax includes the SELECT, INSERT, UPDATE, and DELETE SQL statemen, as well as, all major SQL clauses and expressions, window functions, SQL meta-expressions, subqueries, plain and recursive CTEs, semi-joins, self-joins, row locking, and more.

**Built-In Slow Performance Query Detection**. Unlike other ORMs HotRod includes the built-in Torcs layer that watches all the queries run by your application and ranks them in different ways. By observing these rankings you can identify the bottlenecks of your application for you, their execution patterns, their execution plans, their error rates, etc. The understanding of the *actual* runtime behavior of your app is the first step to address its bottlenecks.

**Advanced Dynamic SQL**. Feature-full query definition that declares rules for inclusion or exclusion of SQL segments using JEXL syntax. In short, the same query will take many shapes when executed with different runtime parameters. It includes basic constructs such as IF/CHOOSE to advanced ones such as TRIM/FOREACH/BIND and nesting of SQL segments. Native SQL can also be used.

**SQL Injection Free**. Free of SQL Injection in all CRUD and LiveSQL queries. Nitro offers SQL Injection &ndash; always disabled by default &ndash; only if deliberately enabled on a per-query basis when the specific use case requires it.

**Out-of-the-box Cursors**. All CRUD, Nitro, and LiveSQL queries can stream data instead of materializing big data sets in memory before consuming them. This can dramatically reduce the memory footprint of your app, improve the app performance, and optimize the use of hardware resources.

## World-Class Features

This is a list of features that you would expect from any typical high-end ORM.

**LiveSQL Live Syntax Check**. LiveSQL uses a builder pattern that allows the compiler to check the syntax of queries while you type. This prevents you typing clauses out of order or illegal expressions such as typing ORDER BY before a WHERE clause or comparing a DATE with a BOOLEAN. Flat out errors are detected immediately.

**Automatic Dialect Translation**. The CRUD and LiveSQL queries are automatically translated to the specific database engine of the datasource. Your CRUD and LiveSQL queries are always typed using standard SQL clauses and the dialect rephrases them accordingly. Apart from simplifying the coding of your app, this strategy also reduces the migration effort should you choose to migrate to a different database in the future.

**Converters**. Column converters can be defined for any table, view, or even SELECT query columns. Converters perform on-the-fly data conversion between fully different data domains. For example between CHAR and a BOOLEAN, or between an INT and an ENUM. The converters are bidirectional so they convert data while reading from the database and also while writing back to it. Data *casting* is also supported when converting between compatible types such as INTEGER and DOUBLE and many others.

**SQL-Free Source Code**. Your application does not include any SQL code in it. You can freely dedicate your code to implement the business logic instead of debugging SQL queries.

**World-class OLTP databases**. The persistence layer includes the most successful world-class relational OLTP databases, including commercial and free ones. It covers heap-based, clustered-index-based, and in-memory databases.

**Wide Tech Stack Support**. Currently tested with state-of-the-art Java 25, Spring 6.x, and Spring Boot 3.x, it includes tech stack support back to Java 8 and Spring 3.x, and Spring Boot to 2.x.

**No-Config Mode for Easy Prototyping**. The No-Configuration mode can run with no layer configuration at all. Just point HotRod to a running database schema and it discovers its details automatically.

**Multi-datasource Aware**. Your app can seamlessly use one or more databases &ndash; of the same or different brand, version, or edition. The persistence layers will automatically switch on the correct SQL dialect for each one, so your app code is always free from specific customizations.

**Feature-Full Configuration**. Even though it includes sensible default settings, the feature-full layer configuration can customize your configuration layer to control folder structure, naming, data types, and more.

**Row Locking**. To implement thread-safe operations the row locking mechanism are available by default in CRUD and LiveSQL, and Nitro.

**Multiple Optimistic Locking Strategies**. To deal with different real-world scenarios three separate optimistic locking strategies are included, that can tackle specific cases when the database table structure cannot be modified at will.

**Native SQL**. Native SQL can be added to any Nitro query. Native dialect SQL extensions can prove very beneficial in specific cases, specially when it comes to uncommon features available only in some databases, or when your app needs to squeeze performance from the engine.

**Flexible Organization of the Persistence Layer**. The persistence layer organization can be tailored to suit different teams setups. The layer configuration includes a rich set of parameters that cover base and extra folders, packages and sub-packages, prefixes, suffixes, and more.

**Configuration Partitioning**. For large apps or multiple teams per project, the layer configuration cab be partitioned using *fragments*. These fragments and sub-fragments can be used to separate and tailor the configuration of sub-modules of the app.

**Auto-generated Identities and Sequence**. The persistence layer automatically discovers Identity-generated primary keys. When it comes to sequences, these need to be explicitly specified by the developer to be used by one or multiple tables.

**Previewing LiveSQL**. The final translated SQL for the dialect can be viewed before it's run. This gives you full access to inspect the actualy SQL query being run, if you need to debug the app, to check specific SQL extensions, or if you need to see the specific parameter values or query columns returned by the query. This is different from SQL logging that can be activated separately.

**Selecting Sequences**. Sometimes your app needs to select from database sequences that are unrelated to tables. This is typically used to produce unique values for APIs, for file-naming, and other.

**SQL Logging**. Configurable SQL Logging allows the operation team to enable or disable logging of a deployed app, by changing the runtime logging configuration of it.

**Composite Primary Keys Supported**. Composite and simple primary keys are fully supported in all queries. This may be considered a basic feature, but many ORMs do not implement this feature.

## Other Features

Of course there are many other features that are not included in this list. The main goal of this page is brevity. See the HotRod 5 Documentation for the full details of what HotRod can do.

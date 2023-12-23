# HotRod ORM

HotRod 4 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

The persistence layer provides CRUD and LiveSQL functionalities to quickly start prototyping an application.

See [What's New](./hotrod-project/docs/docs-4/whats-new.md) in HotRod 4, [Version History](./hotrod-project/docs/version-history.md), and the [Roadmap](./hotrod-project/docs/roadmap.md). For documentation on the previous version see [HotRod 3 Documentation](./hotrod-project/docs/docs-3.4/README.md).


## Easy CRUD

The out-of-the-box CRUD methods can access rows by primary keys, foreign keys, or by example. SELECT, UPDATE, INSERT, and DELETE methods are automatically included in the CRUD persistence layer.

For example, to find an employee by primary key:

```java
  Employee emp = this.employeeDAO.select(134081);
```

To update the status of an invoice:

```java
  Invoice inv = this.invoiceDAO.select(5470);
  inv.setStatus("PAID");
  this.invoiceDAO.update(inv);
```


## The Power Of LiveSQL At Your Fingertips

LiveSQL can express SELECT, UPDATE, DELETE, and INSERT queries. A basic select with a simple condition
can look like:

```java
  List<Employee> employees = this.employeeDAO
    .select(e, e.salary.plus(e.bonuses).ge(40000).and(e.type.ne("EXT")))
    .execute();
```

The criteria can include parenthesis, complex predicates, subqueries, etc. For example, a more complex search condition can take the form:

```java
  List<Employee> employees = this.employeeDAO
    .select(e, e.type.ne("MANAGER").and(sql.exists(
        sql.select() .from(m) .where(m.branchId.ne(e.branchId).and(m.firstName.eq(e.firstName)))
      ))
    )
    .orderBy(e.branchId.desc(), e.firstName)
    .execute();
```

LiveSQL extensive SQL syntax can express complex expressions (of numeric, string, date/time, boolean, and binary types), functions, joins, aggregations, window functions, ordering, limiting, multilevel subqueries, multilevel set operators, traditional and recursive CTEs, lateral queries, etc. 

A simple join can look like:

```java
  List<Row> rows = sql
    .select(e.star(), b.name.as("branchName"))
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
    .orderBy(b.name, e.lastName.desc())
    .limit(50)
    .execute();
```

While a more complex query can look like:

```java
  List<Row> rows = sql
    .select(e.star(), b.name.as("branchName"))
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
    .orderBy(b.name, e.lastName.desc())
    .limit(50)
    .execute();
```


## Hello World

See HotRod in action with the [Hello World Example](./hotrod-project/docs/docs-4/guides/hello-world.md). It's a runnable simple example to run the queries shown above.


## Modules

The HotRod functionality is divided into five modules serving different purposes. From simple
out-of-the-box functionality available in CRUD and LiveSQL to advanced querying capabilities provided by Nitro and query optimization with Torcs and Torcs CTP.

These modules are:

- [CRUD](./hotrod-project/docs/docs-4/crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./hotrod-project/docs/docs-4/livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](./hotrod-project/docs/docs-4/nitro/README.md) &mdash; All the power of [Dynamic SQL](./hotrod-project/docs/docs-4/nitro/nitro-dynamic-sql.md) combined with [Graph Queries](./hotrod-project/docs/docs-4/nitro/nitro-graph-selects.md) and Native SQL when you need it
- [Torcs](./hotrod-project/docs/docs-4/torcs/README.md) &mdash; Detect slow queries at runtime and analyze them
- [Torcs CTP](./hotrod-project/docs/docs-4/torcs-ctp/README.md) &mdash; Generate execution plans of slow queries and visualize them in Check The Plan

## Examples

See [Hello CRUD!](./hotrod-project/docs/docs-4/crud/hello-crud.md) for the out-of-the-box functionality and its basic setup and to run other CRUD examples. 

<!--
For the basic usage of each module and more examples of each one see:
 - [Hello CRUD]() &amp; more [CRUD Examples]().
 - [Hello LiveSQL]() &amp; more [LiveSQL Examples]().
 - [Hello Nitro]() &amp; more [Nitro Examples]().
 - [Hello Torcs]() &amp; more [Torcs Examples]().
 - [Hello Torcs CTP]() &amp; more Torcs CTP Examples]().
-->


## Reference

Even though prototyping with simple functionality works out of the box, more advanced features need to be enabled by configuration.

The reference details how to activate all features, and how to use them.

- [Configuration File Reference](./hotrod-project/docs/docs-4/config/README.md)
- [Cheat Sheet](./hotrod-project/docs/docs-4/cheat-sheet.md)
- [Libraries](./hotrod-project/docs/docs-4/config/libraries.md)
- [Maven Integration](./hotrod-project/docs/docs-4/maven/README.md)
- [Supported Databases and Default Data Types](./hotrod-project/docs/docs-4/config/supported-databases.md)


## Guides

These guides focus on specific features and show examples enabling and using them.

These guides cover:

- [Schema Discovery](./hotrod-project/docs/docs-4/guides/schema-discovery.md)
- [Using Multiple DataSources](./hotrod-project/docs/docs-4/guides/using-multiple-datasources.md)
- [Custom Column Types](./hotrod-project/docs/docs-4/guides/mapping-column-types.md)
- [Custom Table and Column Names](./hotrod-project/docs/docs-4/guides/mapping-table-and-column-names.md)
- [Extending LiveSQL Functions](./hotrod-project/docs/docs-4/livesql/extending-livesql-functions.md)
- [Previewing LiveSQL](./hotrod-project/docs/docs-4/livesql/previewing-livesql.md)
- [Starting a Spring Boot Project from Scratch using PostgreSQL](./hotrod-project/docs/docs-4/guides/starting-a-maven-project-from-scratch-with-postgresql.md)
- [Automate Project Creation with One Command](./hotrod-project/docs/docs-4/maven/maven-arquetype.md)
- [Enabling SQL Logging](./hotrod-project/docs/docs-4/guides/enabling-sql-logging.md)


## Features Overview

HotRod follows the *Database First* approach. It assumes the database already exists with all tables ready. HotRod inspects the database and produces a fully functional
Spring persistence layer.

In a nutshell HotRod's features are:

- Straightforward CRUD and LiveSQL persistence layer for quick prototyping
- Default, custom, and rule-based data types
- Default, custom, and rule-based object names
- Support for major relational databases
- Flexible querying from Java using LiveSQL
- Index-aware persistence layer. Indexes shape the persistence functionality
- Auto-discovery of tables and views that can be used by CRUD and LiveSQL off-the-shelf
- Domain objects can be extended with custom properties and custom methods
- Seamlessly applies database changes to the data model, without losing custom properties or methods
- Includes powerful Dynamic SQL for more demanding needs
- Implements Optimistic Locking for concurrency control on a per-table basis
- Full access to Native SQL when needed
- Graph queries load data in data structure trees rather than plain list of rows
- Implements cursors to process queries with minimal memory usage
- Implements enum dimension-like tables to reduce joins and database query cost
- Works seamlessly with database views
- Implements converters for a richer modeling of the data
- Fully integrated with Maven builds, Maven persistence layer generation, and Maven arquetype
- Tailored for Spring and String Boot projects to gain access to all their 
features such as transaction management and AOP


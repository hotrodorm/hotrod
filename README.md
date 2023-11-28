# HotRod ORM

HotRod 4 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

It generates a persistence layer with minimal to no configuration and the CRUD and LiveSQL functionalities can quickly help to start prototyping an application.

See [What's New](./hotrod-project/docs/docs-4/whats-new.md) in HotRod 4, [Version History](./hotrod-project/docs/version-history.md), and the [Roadmap](./hotrod-project/docs/roadmap.md).

For documentation on the previous version see [HotRod 3 Documentation](./hotrod-project/docs/docs-3.4/README.md).


## Hello World

See HotRod in action with the [Hello World Example](./hotrod-project/docs/docs-4/guides/hello-world.md). It demonstrates the use of CRUD by reading a row using a primary key, as in:

```java
  Employee emp = this.employeeDAO.select(1045);
```

It also shows a simple join between two tables using LiveSQL:

```java
  EmployeeTable e = EmployeeDAO.newTable("e");
  BranchTable b = BranchDAO.newTable("b");

  List<Row> rows = this.sql
    .select(e.star(), b.name.as("branchName"))
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.lastName.like("%Smith%"))
    .execute();
```


## Modules

The HotRod functionality is divided into five modules serving different purposes. From simple
out-of-the-box functionality available in CRUD and LiveSQL to advanced querying capabilities provided by Nitro and query optimization with Torcs and Torcs CTP.

These modules are:

- [CRUD](./hotrod-project/docs/docs-4/crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./hotrod-project/docs/docs-4/livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](./hotrod-project/docs/docs-4/nitro/README.md) &mdash; All the power of [Dynamic SQL](./hotrod-project/docs/docs-4/nitro/nitro-dynamic-sql.md) combined with [Graph Queries](./hotrod-project/docs/docs-4/nitro/nitro-graph-selects.md) and Native SQL when you need it
- [Torcs](./hotrod-project/docs/docs-4/torcs/README.md) &mdash; Detect slow queries at runtime and analyze them
- [Torcs CTP](./hotrod-project/docs/docs-4/torcs-ctp/README.md) &mdash; Generate execution plans of slow queries and visualize them in Check The Plan


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


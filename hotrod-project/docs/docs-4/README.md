# HotRod 4

HotRod is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

A persistence layer is generated with a minimal configuration to no configuration and the CRUD and LiveSQL functionalities
can quickly help to start prototyping an application.

See [What's New](./whats-new.md) in HotRod 4, [Version History](../version-history.md), and the [Roadmap](../roadmap.md). 


## Hello World

See HotRod in action with the [Hello World Example](./guides/hello-world.md).


## Modules

HotRod functionality is divided into three modules serving different purposes. From simple out-of-the-box functionality
available for CRUD and LiveSQL to advanced querying capabilities provided by Nitro. 

These modules are:

- [CRUD](crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](nitro/README.md) &mdash; All the power of [Dynamic SQL](nitro/nitro-dynamic-sql.md) combined with [Graph Queries](nitro/nitro-graph-selects.md) and native SQL


## Reference

Even though prototyping with simple functionality works out of the box, more advanced features need to be enabled by configuration.

The reference details how to activate all features, and how to use them.

- [Configuration File Reference](config/README.md)
- [Cheat Sheet](./cheat-sheet.md)
- [Libraries](config/libraries.md)
- [Maven Integration](maven/README.md)
- [Supported Databases and Default Data Types](config/supported-databases.md)


## Guides

These guides focus on specific features and show examples enabling and using them.

These guides cover:

- [Schema Discovery](guides/schema-discovery.md)
- [Using Multiple DataSources](./guides/using-multiple-datasources.md)
- [Custom Column Types](guides/mapping-column-types.md)
- [Custom Table and Column Names](guides/mapping-table-and-column-names.md)
- [Extending LiveSQL Functions](livesql/extending-livesql-functions.md)
- [Previewing LiveSQL](./livesql/previewing-livesql.md)
- [Starting a Spring Boot Project from Scratch using PostgreSQL](guides/starting-a-maven-project-from-scratch-with-postgresql.md)
- [Automate Project Creation with One Command](maven/maven-arquetype.md)
- [Enabling SQL Logging](./guides/enabling-sql-logging.md)

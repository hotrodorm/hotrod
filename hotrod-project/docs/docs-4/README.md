# HotRod 4 (alpha, unreleased)

HotRod is an open source ORM for Spring and Spring Boot geared towards high performance persistence for relational databases.

The persistence layer can be generated with a minimal configuration and the CRUD functionality can quickly help to start 
prototyping an application.

Overview:
- [Features](./features.md) and [What's New](./whats-new.md)
- [Version History](../version-history.md)

HotRod includes four modules:
- [CRUD](crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](nitro/README.md) &mdash; Combine Native SQL with [Dynamic SQL](nitro/nitro-dynamic-sql.md) and [Graph Queries](nitro/nitro-graph-selects.md).
- `Torcs` &mdash; Detect slow queries at runtime and analyze them

Reference:
- [Configuration File Reference](config/README.md)
- [Cheat Sheet](./cheat-sheet.md)
- [Libraries](config/libraries.md)
- [Maven Integration](maven/README.md)
- `Eclipse Integration`
- [Supported Databases and Default Data Types](config/supported-databases.md)

Quick Guides:
- [Hello World!](./guides/hello-world.md)
- [Schema Discovery](guides/schema-discovery.md)
- [Starting a Spring Boot Project from Scratch using PostgreSQL](guides/starting-a-maven-project-from-scratch-with-postgresql.md)
- [Automate Project Creation with One Command](maven/maven-arquetype.md)
- [Custom Column Types](guides/mapping-column-types.md)
- [Custom Table and Column Names](guides/mapping-table-and-column-names.md)
- [Extending LiveSQL Functions](livesql/extending-livesql-functions.md)
- [Previewing LiveSQL](./livesql/previewing-livesql.md)
- [Enabling SQL Logging](./guides/enabling-sql-logging.md)
- [Using Multiple DataSources](./guides/using-multiple-datasources.md)

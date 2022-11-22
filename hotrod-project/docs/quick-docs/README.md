# HotRod

HotRod is an open source ORM for Spring and Spring Boot geared towards high performance persistence for relational databases.

The persistence layer can be generated with a minimal configuration and the CRUD functionality can quickly help to start 
prototyping an application.

Overview:
- [Features](./features.md).
- [Version History](./version-history.md).

HotRod includes four modules:
- [CRUD](crud/crud.md) &mdash; Quick and simple persistence for rapid prototyping.
- [LiveSQL](livesql/livesql.md) &mdash; Flexible querying from Java.
- [Nitro](nitro/nitro.md) &mdash; Combine native SQL with dynamic SQL and structured queries.
- `Torcs` &mdash; Detect slow queries at runtime and analyze them.

Reference:
- [Configuration File Reference](config/configuration-file-structure.md).
- [Libraries](config/libraries.md).
- [Maven Integration](maven/maven.md).
- `Eclipse Integration`.
- [Supported Databases and Default Data Types](config/supported-databases.md).
- [Cheat Sheet](./cheat-sheet.md).

Quick Guides:
- [Hello World!](./guides/hello-world.md)
- Starting a Maven Project from Scratch [using H2](guides/starting-a-maven-project-from-scratch-with-h2.md), or [using PostgreSQL](guides/starting-a-maven-project-from-scratch-with-postgresql.md).
- [Creating a Running Project in One Command](maven/maven-arquetype.md).
- [Mapping Column Types](guides/mapping-column-types.md).
- [Mapping Table and Column Names](guides/mapping-table-and-column-names.md).
- [Extending LiveSQL Functions](livesql/extending-livesql-functions.md).
- [Previewing LiveSQL](./livesql/previewing-livesql.md).
- [Enabling SQL Logging](./guides/enabling-sql-logging.md).


# HotRod

HotRod is an open source ORM for Spring and Spring Boot geared towards high performance persistence for relational databases.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. The Nitro and Torcs
modules tackle more complex or high performance queries with some extra configuration.

HotRod follows the *Database First* approach. It assumes the database already exists with all tables ready. HotRod inspects the database
and produces a Java persistence layer in seconds. Later, when the database experiences changes HotRod
retrieves and applies these changes to the persistence layer seamlessly.

Overview:
- [The Basics]().
- [Features](features.md).
- [Version History](./version-history.md).
- [Retrieving and Applying Database Changes]().

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
- [Chear Sheet](./cheat-sheet.md).

Quick Guides:
- [Hello World!](./guides/hello-world.md)
- Starting a Maven Project from Scratch [using H2](guides/starting-a-maven-project-from-scratch-with-h2.md), or [using PostgreSQL](guides/starting-a-maven-project-from-scratch-with-postgresql.md).
- [Creating a Running Project in One Command](maven/maven-arquetype.md).
- [Mapping Column Types](guides/mapping-column-types.md).
- [Mapping Table and Column Names](guides/mapping-table-and-column-names.md).
- [Extending LiveSQL Functions](livesql/extending-livesql-functions.md).
- [Enabling SQL Debugging](./guides/enabling-sql-debug.md).


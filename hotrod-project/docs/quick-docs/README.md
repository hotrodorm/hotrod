# HotRod

HotRod is an ORM for Spring and Spring Boot focused on high performance database persistence for relational databases.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. The Nitro and Torcs modules tackle complex or high performance queries with some extra configuration.

HotRod follows the *Database First* approach. It assumes the database already exists with all tables ready. HotRod inspects the database
and produces all classes along with a persistence layer in no time. Later, when the database experiences changes HotRod
retrieves and applies these changes to the persistence layer seamlessly.

Overview:
- [The Basics]().
- [Features](features.md).
- [Retrieving and Applying Database Changes]().

HotRod includes four modules:
- [HotRod CRUD](crud/crud.md) &mdash; Quick and simple persistence to start prototyping immediately.
- [HotRod LiveSQL](livesql/livesql.md) &mdash; Flexible querying from Java.
- [HotRod Nitro](nitro/nitro.md) &mdash; Advanced queries with high performance.
- `HotRod Torcs` &mdash; Detect slow queries at runtime and analyze them.

Reference:
- [Configuration File Reference](config/configuration-file-structure.md).
- [Libraries](config/libraries.md).
- [Maven Integration](maven/maven.md).
- [Ant Integration](ant/ant.md).
- `Eclipse Integration`.
- [Supported Databases and Default Data Types](config/supported-databases.md).

Quick Guides:
- [Starting a Maven Project from Scratch](guides/starting-a-maven-project-from-scratch.md).
- [Creating a Running Project in One Command](maven/maven-arquetype.md).
- [Mapping Database Data Types](config/type-solver.md).
- [Mapping Database Names](config/name-solver.md).
- [Extending LiveSQL Functions](livesql/extending-livesql-functions.md).

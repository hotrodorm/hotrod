# HotRod

HotRod is an ORM for Spring and Spring Boot geared towards high performance persistence on relational databases.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. The Nitro and Torcs modules tackle complex and high performance queries with extra configuration.

HotRod includes:
- [HotRod CRUD](module-crud.md). Quick and simple persistence to start prototyping in no time.
- [HotRod LiveSQL](module-livesql.md). Flexible querying from Java.
- [HotRod Nitro](./nitro/nitro.md). Advanced queries with high performance.
- [HotRod Torcs](module-torcs.md). Detect slow queries at run time and analyze them.

Reference:
- [Maven Integration](./maven/maven.md).
- [HotRod Configuration File Reference](./config/configuration-file-structure.md).
- [Database Support]().

How to Guides:
- [Starting A Maven Project from Scratch](./hello-world/creating-a-new-project.md).
- [Creating a Running Project in One Command](maven/maven-arquetype.md).
- [Flexible Type Solving](config/type-solver.md).
- [Smart Naming of Database Objects](config/name-solver.md).
- [Extending LiveSQL](livesql/custom-database-functions.md).

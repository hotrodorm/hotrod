# HotRod

HotRod is an ORM for Spring and Spring Boot geared towards high performance queries on relational databases.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. The Nitro and Torcs modules tackle more complex and high performance queries that require extra configuration.

HotRod includes:
- [HotRod CRUD](module-crud.md). Quick and simple persistence to start prototyping in no time.
- [HotRod LiveSQL](module-livesql.md). Flexible querying from Java.
- [HotRod Nitro](./nitro/nitro.md). Add high performance and complex queries.
- [HotRod Torcs](module-torcs.md). Find slow queries at run time and analyse them.

Reference:
- [Maven Integration](./maven/maven.md).
- [HotRod Configuration File Reference](./config/configuration-file-structure.md).
- [Database Support]().

How to Guides:
- [Setting Up A Maven Project Manually](./hello-world/creating-a-new-project.md).
- [Creating a Running Project with 1 Command](maven/maven-arquetype.md).
- [Type Solver](config/type-solver.md).
- [Name Solver](config/name-solver.md).
- [Extending LiveSQL Functions](livesql/custom-database-functions.md).

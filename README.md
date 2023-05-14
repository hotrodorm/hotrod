# HotRod

HotRod 4 is an open source ORM for Spring and Spring Boot geared towards high performance persistence for relational databases.

A persistence layer is generated with a minimal to no configuration and the CRUD and LiveSQL functionalities
can quickly help to start prototyping an application.


## Documentation

Documentation of the latest version can be found at [HotRod 4 Docs](./hotrod-project/docs/docs-4/README.md)
and [What's New in HotRod 4](hotrod-project/docs/docs-4/whats-new.md).

Documentation of the previous version can be found at [HotRod 3 Docs](hotrod-project/docs/docs-3.4/README.md).

HotRod includes three modules:

- CRUD &mdash; Quick and simple persistence for rapid prototyping
- LiveSQL &mdash; Flexible querying from Java
- Nitro &mdash; Combine Native SQL with Dynamic SQL and Graph Queries


## Hello World Example

See HotRod in action with the [Hello World Example](hotrod-project/docs/docs-4/guides/hello-world.md).


## Features

HotRod follows the *Database First* approach. It assumes the database already exists with all 
tables ready. HotRod inspects the database and produces a fully functional
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


# Features

HotRod is an open source ORM for Spring and Spring Boot applications that has a focus on high
performance relational persistence.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. 
The Nitro and Torcs modules tackle more complex or high performance queries with some extra 
configuration.

HotRod follows the *Database First* approach. It assumes the database already exists with all 
tables ready. With minimal configuration HotRod inspects the database and produces a fully functional
Java persistence layer. Later on, when the database experiences changes HotRod retrieves and applies
these changes to the persistence layer seamlessly.

In a nutshell HotRod's features are:

- Developed under the open source license Apache 2.0 and officially available at Maven Central Repository.
- Straightforward CRUD persistence layer for quick prototyping.
- Default, custom, and rule-based data types.
- Default, custom, and rule-based object names.
- Support for major relational databases.
- Flexible querying from Java using LiveSQL.
- Index-aware persistence layer. Indexes shape the persistence functionality.
- Domain objects can be extended with custom properties and custom methods.
- Seamlessly applies database changes to the data model, without losing custom properties or methods.
- Includes powerful Dynamic SQL for more demanding needs.
- Implements Optimistic Locking for concurrency control on a per-table basis.
- Full access to Native SQL when needed.
- Graph queries load data in data structures trees rather than plain list of rows.
- Implements cursors to process queries with minimal memory usage.
- Implements *enum* dimension-like tables to reduce joins and database query cost.
- Works seamlessly with database views.
- Implements converters for a richer modeling of the data.
- Extensible LiveSQL functions.
- Fully integrated with Maven builds, Maven persistence layer generation, and Maven arquetype.
- Tailored for Spring and String Boot projects to gain access to all their 
features such as transaction management and AOP.



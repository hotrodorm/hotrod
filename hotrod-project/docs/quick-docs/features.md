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

- Automated CRUD persistence layer for quick prototyping.
- Default and configurable data types.
- Default and configurable object names.
- Support for major relational databases.
- Flexible querying from Java using LiveSQL.
- Indexes shape the persistence layer. Adding or removing them have consequences.
- Domain objects can be extended with custom properties and custom methods.
- Seamlessly applies database changes to the data model, without losing custom properties or methods.
- Powerful Dynamic SQL for more demanding needs.
- Implements Optimistic Locking on a per-table basis.
- Full access to Native SQL when needed.
- With minimal configuration structured queries automatically load data in data structures trees rathen than
plain list of rows.
- Implements cursors to process queries with minimal memory usage.
- Implements *enum* dimension-like tables to reduce joins and database effort.
- Works seamlessly with database views.
- Implements converters for a richer modeling of the data.
- Extensible LiveSQL functions.
- Fully integrated with Maven builds, Maven persistence layer generation, and Maven arquetype.
- Tailored for Spring and String Boot projects to gain access to all their 
features such as transaction management and AOP.
- Developed under the open source license Apache 2.0 and officially available at Maven Central Repository.



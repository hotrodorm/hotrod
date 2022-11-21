# Features

HotRod is an ORM for Spring and Spring Boot applications that has a focus on performance relational persistence.

The CRUD and LiveSQL modules provide quick out-of-the-box persistence that can be used in minutes. The Nitro and Torcs
modules tackle more complex or high performance queries with some extra configuration.

HotRod follows the *Database First* approach. It assumes the database already exists with all tables ready. HotRod inspects the database
and produces a Java persistence layer in seconds. Later, when the database experiences changes HotRod
retrieves and applies these changes to the persistence layer seamlessly.

In a nutshell HotRod's features are:

- Automated CRUD persistence layer for quick prototyping.
- Default and configurable data types.
- Default and configurable object names.
- Support for major relational databases.
- Flexible querying from Java using LiveSQL.
- Indexes shape the persistence layer. Adding or removing them have consequences.
- Capable of seamlessly apply database changes to the data model when they happen,
without every losing custom changes.
- Powerful Dynamic SQL for more demanding needs.
- Implements Optimistic Locking as needed.
- Full access to Native SQL when needed.
- Structured queries automatically load data in tree data structures.
- Extensible LiveSQL functions.
- Fully integrated with Maven builds, Maven persistence layer generation, and Maven arquetype.
- Tailored for Spring and String Boot projects to gain access to all their 
features such as transaction management and AOP.
- Open source project (Apache 2.0) officially available at Maven Central Repository.



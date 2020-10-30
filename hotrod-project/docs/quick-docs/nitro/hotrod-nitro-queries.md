# HotRod Nitro Queries

The HotRod Nitro Enhancement allows the developer to run high performance, semi-automated queries.

The developer can add tailored queries to execute any custom build SQL query with or without native features. Apart from simple flat queries HotRod can also add a new dimension of hierarchical data and automated SQL rendering to solve very common problems -- what is known as *Structured SELECTs*.

Custom queries can typically access the whole range of the database services, and can also squeeze performance by the use of particular non-standard engine-specific features and peculiarities.

Enhanced queries are grouped into two categories: SQL queries that do not return data -- implemented by the `<query>` tag --, and SQL queries that return data -- implemented by the `<select>` tag.

## The &lt;query> Tag

A [general query](nitro-queries.md) does not return a result set and is implemented using `<query>` tag. It may return optional count of rows.

They are commonly used to perform changes in the database -- by running tailored UPDATE or DELETE statements -- but can actually run any SQL statement, including DML statements and stored procedures calls.

They can include the full native SQL language available on the database, as well as hints and non-standard features. They can also accept parameters.

General queries are exposed as Java methods with parameters in the DAO where they are defined. 

## The &lt;select> Tag

A [SELECT](nitro-selects.md) query returns tabular data that is typically received by the app as a list of rows.

A great deal of options have been added to make the most of these queries with the less amount of coding by the developer.

In these queries HotRod models the rows as fully typed structures with properties; it does not produce untyped data such as Java `Map<>`s.

These kind of queries offer an ample repertoire of enhancements to deal with a variety of real world cases. In the simplest form, the developer can directly
type the SQL statement and it will be run. More potent options allow the developer to enrich the query with automated list of columns, with hierarchies of data, dynamic SQL, and more.

SELECT queries are exposed as Java methods with parameters in the DAO where they are defined. 

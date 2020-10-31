# Selecting data using &lt;select>

HotRod allows you to add any tailored SELECT query using the &lt;select> tag. The resulting results set is represented by 
a newly created value object with formal properties and types. In other words, the SELECT query does not return an 
un-typed Map&lt;String, Object> but a fully named and fully typed value object.

One or more &lt;select> tags can be added inside any of:

 - &lt;table> tag,
 - &lt;view> tag, or
 -  &lt;dao> tag

Each &lt;select> produces a Java method -- optionally with parameters -- to execute the tailored SELECT statement.

This strategy helps you squeeze the full performance of the SQL dialect of your database engine. All specialized strategies of your engine can be used, including hints, non-standard SQL clauses available in the database, and all functions and tricks your engine provides.

The &lt;select> tag can include two kinds of SELECT statements: flat selects, and structures selects.

## Flat SELECTs

[Flat SELECTs](nitro-flat-selects.md) are the simplest and probably most useful and common form of the &lt;select> tag. It's recommended for beginners since they produce straightforward value objects that represent the result set of the query. They can be up and running in no time.

## Structured SELECTs

[Structured SELECTs](nitro-structured-selects.md) enhance the *flat select model* by adding a hierarchical structure to the result set.

Instead of a flat `List` of properties, columns are grouped in a hierarchy of VOs. These queries can return fully named, fully typed
tree-like structures of VOs using collections and/or associations.

This strategy can be very effective to resolve the "N + 1" problem when accessing data.

## Dynamic SQL

Both flat and structures SELECTs can also be enhanced by the use of [Dynamic SQL](dynamic-sql.md).

With Dynamic SQL queries change form depending on the parameter values supplied at runtime. Entire sections of the query may be included or excluded according to boolean logic. Java collections can be use to render iterative sections of the query as well, and much more.
 

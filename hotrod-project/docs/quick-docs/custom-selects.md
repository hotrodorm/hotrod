# SELECT queries using &lt;select>

HotRod allows you to add any tailored SELECT query using the &lt;select> tag. The resulting results set is represented by 
a newly created value object with formal properties and types. In other words, the SELECT query does not return an 
un-typed Map&lt;String, Object> but a fully named and fully typed value object.

One or more &lt;select> tags can be added inside any of:

 - &lt;table> tag,
 - &lt;view> tag, or
 -  &lt;dao> tag
 
Each &lt;select> produces a Java method -- optionally with parameters -- to execute the tailored SELECT statement.

Squeeze the full performance of the SQL dialect of your database engine. All specialized strategies of your engine can be used, including hints, non-standard SQL clauses available in the database, and all functions and tricks your engine provides.

The &lt;select> tag includes several kinds of SELECT statements:

 - [Flat SELECTs](flat-selects.md): are the simplest and probably most useful and common form of the &lt;select> tag. Recommended for beginners.
 - [Structured SELECTs](structured-selects.md): these queries enhance the flat select model by adding a structure to the result set. Instead of a `List` of VOs, these queries can return fully named, fully type tree-like structures of VOs using collections and association tags. This strategy can be very effective to resolve the "N + 1" problem when accessing data.
 - [Dynamic SQL](dynamic-sql.txt): Flat and Structured queries can be fully combined with this technology. With Dynamic SQL queries change form depending on the parameter values supplied at runtime. Entire sections of the query may be included or excluded according to boolean logic. Java collections can be use to render iterative sections of the query as well, and much more.
 

# The `<select>` Tag

This tags defines a Nitro select query in the DAO.

A `<select>` query is intended to execute queries that return a result set. To run
a typical query that does not return a result set use the `<query>` tag instead.

`<select>` tags can be added to any `<table>`, `<view>`, or `<dao>` tag.

Nitro selects allow the developer to combine parameterized queries with native and dynamic SQL. These queries
fall into two types: Flat Select and Graph Selects. Both of them can be used assemble complex queries with a 
few tags. Used appropriately these queries can execute with high performance. For more details see
[Nitro Queries](../../nitro/nitro.md).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `method` | The Java method that will execute this query | Required |
| `vo` | The VO class that will be generated to represent result set rows of the query | Optional [^2] |
| `mode` | The fecthing mode that defines how the query result will be represented in Java. Valid options are: `list`, `cursor`, `single-row` [^1] | `list` |
| `implements` | A comma-separated list of fully-qualified classes that will be added to the value object definition | N/A |

[^1]: When using the `list` option the method will return a `List<Row>`. When using the `cursor` option the method will return a `Cursor<Row>`. When using
`single-row` the method will return `Row`.
[^2]: See [Nitro Queries](../../nitro/nitro.md) for exact usage.


## General Structure

The body of this tag includes the SQL statement to be executed decorated with parameter definition, parameter applying,
parameter injection, and Dynamic SQL . It can also include one or more `<column>` tags, a `<columns>` tag, and one or
more `<complement>` tags.

Only `SELECT` queries (or other equivalent statement such as `VALUES`) can be executed using this tag. The 
query must return a result set.

Each included [`<parameter>`](./parameter.md) tag specify parameters for the query.

A parameter can be applied one or more times with using `#{name}` in the query, where `name` is the parameter name defined in
a `<parameter>` tag. Applying a parameter value is a safe and fully-typed way of parameterizing a query.

Also, a parameter can be injected one or more times with using `${name}` in the query, where `name` is the parameter name defined in
a `<parameter>` tag. See [A Note on SQL Injection](#a-note-on-sql-injection) for the security concerns about SQL Injection.

[Dynamic SQL](../../nitro/nitro-dynamic-sql.md) can be added to include or exclude fragments of the SQL query at runtime according 
to the parameter values.

Each included [`<column>`](./column.md) tag changes the default behavior of how a column is retrieved. It can 
change the resulting property name, its type, or can be used to apply a converter to it.

Alternatively, a single `<columns>` tag can be used to enable [Graph Queries](../../nitro/nitro-graph-selects.md).
Graph selects are an advanced feature that post-processes the received result set and produces trees of VOs instead
of flat rows. They can be useful to enhance a query with a few tags, and automatically generate VOs to retrieve complex 
data structures. They can also reuse existing VOs preserving all their custom properties and behavior.

The `<complement>` is used to enclose [Dynamic SQL](../../nitro/nitro-dynamic-sql.md) sections of the query in order to make the query parseable.
Parameter applying (`#{name}`) and injection (`${name}`) do not need to be enclosed by this tag and can be included as is.
Dynamic SQL sections need to be enclosed because when HotRod parses the query to retrieve its final result set structure,
it needs to make sure the SQL query is a valid one; the `<complement>` tag and all its content is excluded from this phase,
so it can be used to hide Dynamic SQL and thus, make the query valid for parsing purposes.


## Examples

A select query (with no parameters) that retrieves all the pending sales orders from the table `SALES` can be implemented as:

```xml
<select method="getPendingSales" vo="PendingSale">
  select * from sales where status = 'PENDING`
</select>
```

The above `<select>` query tag will add the following method to the DAO in the persistence layer:

```java
public List<PendingSale> getPendingSales() { ... }
```

If we wanted to get the pending sales for a specific branch, sorted by an ordinal column, then we 
could define a couple of parameters for it. The following example (for PostgreSQL) applies the first
parameter and injects the second one:

```xml
<select method="getPendingSalesOfBranch" vo="BranchPendingSale">
  <parameter name="branchId" java-type="java.lang.Integer" jdbc-type="NUMERIC" />
  <parameter name="orderColumn" java-type="java.lang.Integer" jdbc-type="NUMERIC" />
  select * 
  from sales 
  where status = 'PENDING` and branch_id = #{branchId}
  order by ${orderColumn}
</select>
```

The above `<select>` query tag will add the following method to the DAO in the persistence layer:

```java
public List<BranchPendingSale> getPendingSales(Integer branchId, Integer orderColumn) { ... }
```

There are multiple variations for SELECT queries, each one enhancing different aspects of it. See
[Nitro Queries](../../nitro/nitro.md) for an explanation of all of these variations.


## A Note on SQL Injection

Injecting a parameter value means to directly *concatenate* its value into the SQL statement. This can be seen as a
simple way of assembling SQL statement at first, and it actually is. However, the downside of it &mdash; and it's a big one
&mdash; is that SQL Injection can modify the whole SQL statement. If the parameter is not controlled by the application and
comes unfiltered from the application UI or other external source it can represent a big security hole in the application and
can lead to severe losses to the business.

In this particular example, the parameter `rows` is injected. If the value always comes from inside the application then there's no 
security vulnerability in using it. However, if the parameter were a `String` coming from a browser, a security hack could replace
it with something like `1 rows only; delete from invoices; select 1 + 1 fetch next 1 `. This would open the application to a 
hacker that could delete the entire database.

Use SQL Injection with care only when there's no other solution. Most of the time Dynamic SQL can provide similar functionality, 
and it's totally safe.










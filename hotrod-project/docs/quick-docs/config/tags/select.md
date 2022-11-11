# The `<select>` Tag

This tags defines a Nitro SELECT query in the DAO.

A `<select>` query is intended to execute queries that return a result set. To run
a typical query that does not return a result set use the `<query>` tag instead.

`<select>` tags can be added to any `<table>`, `<view>`, or `<dao>` tag.

Nitro queries allow the developer to include fully parameterized native queries in the application. These queries 
can combine Dynamic SQL logic with structured generation queries, and with Native SQL. This is a more 
advanced topic that can be used reduce complex generation logic to a few tags. Nitro queries are also geared 
towards high performance SQL and query optimization. For more details see [Nitro Queries](../../nitro/nitro.md).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `method` | The Java method that will execute this query | Required |
| `vo` | The VO class that will be generated to represent result set rows of the query | Optional [^2] |
| `mode` | The fecthing mode that defines how the query result will be represented in Java. Valid options are: `list`, `cursor`, `single-row` [^1] | `list` |

[^1]: When using the `list` option the method will return a `List<Row>`. When using the `cursor` option the method will return a `Cursor<Row>`. When using
`single-row` the method will return `Row`.
[^2]: See [Nitro Queries](../../nitro/nitro.md) for exact usage.


## General Structure

The body of this tag includes the SQL statement to be executed decorated with Dynamic SQL tags, parameter 
definition, parameter applying, and parameter injection. It can also include one or more `<column>` tags, 
a `<columns>` tag, and one or more `<complement>` tags.

Only `SELECT` queries (or other equivalent statement such as `VALUES`) can be executed using this tag. The 
query must return a result set.

Each included [`<parameter>`](./parameter.md) tag specify parameters for the query.

A parameter can be applied one or more times with the form `#{name}`, where `name` is the parameter name defined in
a `<parameter>` tag.

A parameter can be injected one or more times with the form `${name}`, where `name` is the parameter name defined in
a `<parameter>` tag. See [A Note on SQL Injection](#a-note-on-sql-injection) for the security concerns about SQL Injection.

Each included [`<column>`](./column.md) tag changes the default behavior of how a column is retrieved. It can 
change the resulting property name, its type, or can be used to apply a converter to it.

Alternatively, a single `<columns>` tag can be used to enable Structured queries. Structured queries are an advanced feature that
post-processed the received result set and produces trees of Java VOs instead of flat rows. They can be useful to retrieve
complex data structures with a query few tags. See [Structured Queries](../../nitro/nitro-structured-selects.md) for details.

The `<complement>` is used to enclose [Dynamic SQL](./dynamic-sql.md) sections of the query in order to make the query parseable.
Parameter applying (`#{name}`) and injection (`${name}`) do not need to be enclosed by this tag and can be included as is.
Dynamic SQL sections need to be enclosed because when HotRod parses the query to retrieve its final result set structure,
it needs to make sure the SQL query is a valid one; the `<complement>` tag and all its content is excluded from this phase,
so it can be used to hide Dynamic SQL and thus, make the query valid for parsing purposes.


## Examples

A query (with no parameters) that deletes all the rows of the table `TEMP_INVOICE` can be 
implemented with the `TRUNCATE` statement as:

```xml
<query method="cleanTempInvoices">
  truncate temp_invoice
</query>
```

The following example &mdash; for DB2 &mdash; includes Dynamic SQL, parameter applying, and parameter injection:

```xml
<query method="closeSales">
  <parameter name="soldOn" java-type="java.time.LocalDateTime" jdbc-type="TIMESTAMP" />
  <parameter name="branchId" java-type="java.lang.Integer" jdbc-type="NUMERIC" />
  <parameter name="rows" java-type="java.lang.Integer" jdbc-type="NUMERIC" />
  update sales
  set status = 'COMPLETED'
  where sold_on = #{soldOn}
    and status = 'ACCEPTED'
    <if test="branchId != null">
      and branch_id = #{branchId}
    </if>
    and fulfilled = 1
  order by fulfilled_on
  fetch next ${rows} rows only
</query>
```

In the example above we can see:
- It defines three parameters.
- The SQL statement is an UPDATE that does not return any rows.
- The parameter `soldOn` is **applied** to the query using the `#{}` construct.
- The parameter `rows` is **injected** into the query using the `${}` construct. This is the only way of using this
parameter since DB2 does not allow to *apply* parameters to the `FECHT NEXT` clause.
- Dynamic SQL is used to filter rows by `branch_id` if the parameter `branchId` has a non-null value. If the parameter
is null, the section `and branch_id = #{branchId}` is not included in the SQL statement at all.


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










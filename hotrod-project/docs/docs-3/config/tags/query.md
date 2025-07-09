# The `<query>` Tag

This tags defines a Nitro General Purpose query in the DAO.

A `<query>` is intended to execute queries that do not return a result set. This means, it's not intended to execute
a `SELECT` SQL statement. To run a `SELECT` and return a result set use the `<select>` tag instead.

`<query>` tags can be added to any `<table>`, `<view>`, or `<dao>` tag.

General purpose queries allow the developer to include parameterized queries that can include native and 
SQL in the application. Used appropriately, general purpose query can be tailored for high performance SQL and
query optimization. For more details see [Nitro Queries](../../nitro/nitro.md).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `method` | The Java method that will execute this query | Required |


## General Structure

The body of this tag includes the SQL statement to be executed decorated with parameter definition, parameter 
applying, parameter injection, and Dynamic SQL.

Any valid database query can be executed using this tag, including DDL (e.g. table creation), DML (e.g.
inserting or selecting), DCL (e.g. granting access), and TCL (transaction control). 

Each included [`<parameter>`](./parameter.md) tag specify parameters for the query.

A parameter can be applied one or more times with using `#{name}` in the query, where `name` is the parameter name defined in
a `<parameter>` tag. Applying a parameter value is a safe and fully-typed way of parameterizing a query.

Also, a parameter can be injected one or more times with using `${name}` in the query, where `name` is the parameter name defined in
a `<parameter>` tag. See [A Note on SQL Injection](#a-note-on-sql-injection) for the security concerns about SQL Injection.

[Dynamic SQL](../../nitro/nitro-dynamic-sql.md) can be added to include or exclude fragments of the SQL query at runtime according 
to the parameter values.


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










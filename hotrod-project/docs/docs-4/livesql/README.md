# The LiveSQL Module

This is the LiveSQL Module of the [HotRod ORM](../README.md).

The LiveSQL module allows the developer to write flexible SQL `SELECT` queries from Java programming code only.

LiveSQL uses tables, views, and columns &mdash; modeled by the CRUD module &mdash; to assemble and execute 
`SELECT`, `INSERT`, `UPDATE`, and `DELETE` queries.


## Example

If the database includes a table `EMPLOYEE` with a few columns, the related DAO can be used to create a table instance to write the query,
as shown below:

```java
@Autowired
private LiveSQL sql;

private void searching() {
  EmployeeTable e = EmployeeDAO.newTable();

  List<Row> rows = this.sql
    .select()
    .from(e)
    .where(e.name.like("A%"))
    .execute();
}
```

The DAO is used to create one instance of the table to use in the `FROM` clause of the table. More instances of the same 
or other tables can be used to join with or for self joins.

The table instance can be used to reference the columns of the table (as in `e.name`) and to assemble complex expressions to use in the query. 
Behind the scenes LiveSQL will assemble and run the query as:

```sql
SELECT * FROM employee WHERE name like 'A%'
```

Finally the `execute()` method runs the query and returns the result.


## Using Cursors

Instead of using the method `execute()` it's also possible to use the method `executeCursor()`. This method avoids 
materializing the whole result set at once into a `java.util.List` and uses buffering to read rows one at a time. This
option can reduce drastically the memory consumption of the application. 

The example above using a cursor takes the form:

```java
@Autowired
private LiveSQL sql;

private void searching() {
  EmployeeTable e = EmployeeDAO.newTable();

  Cursor<Row> rows = this.sql
    .select()
    .from(e)
    .where(e.name.like("A%"))
    .executeCursor();
}
```

Consider that cursors only live between the boundaries of a database transaction &ndash; that is, methods annotated with the 
`@Transactional` annotation &ndash; and that they are automatically closed when the transaction ends.


## LiveSQL Syntax

Each section of a SELECT statement has variations, and they are discussed below:

- [The SELECT Statement](./syntax/select.md)
    - [The FROM and JOIN Clauses](./syntax/from-and-joins.md)
    - [The WHERE Clause](./syntax/where.md)
    - [The GROUP BY Clause](./syntax/group-by.md)
    - [The HAVING Clause](./syntax/having.md)
    - [The ORDER BY Clause](./syntax/order-by.md)
    - [The OFFSET Clause](./syntax/offset.md)
    - [The LIMIT Clause](./syntax/limit.md)
    - [The DUAL &amp; SYSDUMMY1 Tables](./syntax/systables.md)

The INSERT statement has two main variations &ndash; using `VALUES` or from a `SELECT`:

- [The INSERT Statement](./syntax/insert.md)
    - [Specifying column names](./syntax/insert-columns.md)
    - [The VALUES clause](./syntax/insert-values.md)
    - [Inserting from a SELECT](./syntax/insert-select.md)

The UPDATE statement has a single variation:

- [The UPDATE Statement](./syntax/update.md)
    - [The SET clause](./syntax/update-set.md)
    - [The WHERE clause](./syntax/update-where.md)

The DELETE statement also has a single variation:

- [The DELETE Statement](./syntax/delete.md)
    - [The WHERE clause](./syntax/delete-where.md)

The Expression Language:

- [Expressions, Operators &amp; Functions](./syntax/expressions.md)
- [Aggregate Functions](./syntax/aggregate-functions.md)
- [Window Functions](./syntax/window-functions.md)
- [Subqueries](./syntax/subqueries.md)
- [Extending LiveSQL Functions](./extending-livesql-functions.md)


## Odds &amp; Ends

- [Previewing LiveSQL](./previewing-livesql.md)
- [LiveSQL and CRUD](./livesql-and-crud.md)
- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md)


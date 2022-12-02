# The LiveSQL Module

This is the LiveSQL Module of the [HotRod ORM](../README.md).

The LiveSQL module allows the developer to write flexible SQL `SELECT` queries from Java programming code only.

LiveSQL uses tables, views, and columns &mdash; modeled by the CRUD module &mdash; to assemble and run `SELECT` queries.


## Example

If the database includes a table `EMPLOYEE` with a few columns, the related DAO can be used to create table instances to use in a query,
as shown in the example code below:

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

The DAO is used to create one instance of the table (aliased `e` in this case) for the `FROM` clause of the table. More instances of the same 
or other table can be used for a self join or to join to other tables.

The table instance can be used to reference the columns of the table (as in `e.name`) and to assemble complex expressions to use in the query. 
Behind the scenes LiveSQL will assemble and run the query as:

```sql
SELECT * FROM employee WHERE name like 'A%'
```

Finally the `execute()` method runs the query and returns the result. A variant of this method called `executeCursor()` can be used instead,
to avoid materializing the whole result set at once and read rows one at a time.


## LiveSQL Syntax

Each sections of a `SELECT` statement has variations, and they are discussed below:

- [The SELECT Statement](./syntax/select.md).
    - [The FROM and JOIN Clauses](./syntax/from-and-joins.md).
    - [The WHERE Clause](./syntax/where.md).
    - [The GROUP BY Clause](./syntax/group-by.md).
    - [The HAVING Clause](./syntax/having.md).
    - [The ORDER BY Clause](./syntax/order-by.md).
    - [The OFFSET Clause](./syntax/offset.md).
    - [The LIMIT Clause](./syntax/limit.md).

The Expression Language:

- [Expressions, Operators &amp; Functions](./syntax/expressions.md).
- [Aggregate Functions](./syntax/aggregate-functions.md).
- [Window Functions](./syntax/window-functions.md).
- [Subqueries](./syntax/subqueries.md).
- [Extending LiveSQL Functions](./extending-livesql-functions.md).


## Odds &amp; Ends

- [Previewing LiveSQL](./previewing-livesql.md).
- [LiveSQL and CRUD](./livesql-and-crud.md).
- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md).



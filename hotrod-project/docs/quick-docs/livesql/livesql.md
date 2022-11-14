# LiveSQL

LiveSQL is a HotRod module that allows the developer to write flexible SQL `SELECT` queries using only Java programming code.

The generator produces Java classes that represent tables, views, and columns and they can be used to assemble and run a `SELECT` query.

## Example

If the database includes a table `EMPLOYEE` with a few columns, the related DAO can be used to create table instances to use in a query,
as shown in the example code below:

```java
  @Autowired
  private LiveSQL sql;

  private void searching() {
    EmployeeTable e = EmployeeDAO.newTable();

    List<Map<String, Object>> l = this.sql
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

- [The SELECT List](./syntax/select-list.md).
- [The FROM and JOIN Clauses](./syntax/from-and-joins.md).
- [The WHERE Clause](./syntax/where.md).
- [The GROUP BY Clause](./syntax/group-by.md).
- [The HAVING Clause](./syntax/having.md).
- [The ORDER BY Clause](./syntax/order-by.md).
- [The OFFSET Clause](./syntax/offset.md).
- [The LIMIT Clause](./syntax/limit.md).

The Expression Language:

- [Expressions](./syntax/expressions.md).
- [Operators &amp; Functions](./syntax/operators-and-functions.md).
    - [Numeric](./syntax/numeric-expressions.md).
    - [String](./syntax/string-expressions.md).
    - [Date &amp; Time](./syntax/datetime-expressions).
    - [Boolean](./syntax/boolean-expressions.md).
    - [Binary](./syntax/binary-expressions.md).
    - [Object](./syntax/object-expressions.md).
- [Aggregate Functions](./syntax/aggregate-functions.md).
- [Window Functions](./syntax/window-functions.md).
- [Subqueries](./syntax/subqueries.md).
- [Extending LiveSQL Functions](./extending-livesql-functions.md).


## Odds &amp; Ends

- [Previewing the SQL Statement](./previewing-the-sql-statement.md).
- [CRUD &amp; LiveSQL &mdash; SelectByCriteria()](./livesql-and-crud.md).
- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md).



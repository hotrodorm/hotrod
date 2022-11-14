# LiveSQL

LiveSQL is a HotRod module that allows the developer to write flexible SQL `SELECT` queries using Java programming only.

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

- [The SELECT List]().
- [FROM and JOINs]().
- [The WHERE Clause]().
- [The GROUP BY Clause]().
- [The HAVING Clause]().
- [The ORDER BY Clause]().
- [The OFFSET Clause]().
- [The LIMIT Clause]().

Assembling expressions:

- [Expressions]().
- [Operators &amp; Functions]().
    - [Numeric]().
    - [String]().
    - [Date &amp; Time]().
    - [Boolean]().
    - [Binary]().
    - [Object]().
- [Window Functions]().

Other:

- [Previewing the SQL Statement]().
- [CRUD &amp LiveSQL &mdash; SelectByCriteria()]().
- []().


## See also:

- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md).
- [Extending LiveSQL Functions](./extending-livesql-functions.md).



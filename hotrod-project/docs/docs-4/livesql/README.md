# The LiveSQL Module

This is the LiveSQL Module of the [HotRod ORM](../README.md).

The LiveSQL module allows the developer to write flexible SQL queries from Java programming code only.

LiveSQL uses tables, views, and columns &mdash; modeled by the CRUD module &mdash; to assemble and execute
`SELECT`, `INSERT`, `UPDATE`, and `DELETE` queries.


## Example

If the database includes an `EMPLOYEE` table with a few columns, the related DAO can be used to create a table instance to write the query,
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

The DAO is used to create one instance of the table that will be used within the `FROM` query clause. More
instances of the same or other tables &mdash; and/or views &mdash; can be used to define queries with joins.

The table instance can be used to reference the columns of the table (as in `e.name`) and to
assemble complex expressions to use in the query. Behind the scenes, LiveSQL will assemble the query as:

```sql
SELECT * FROM employee WHERE name like 'A%'
```

Finally the `execute()` method runs the query and returns the result set as a list of rows.

## LiveSQL Dialects

While assembling the query, LiveSQL generates the specific syntax according to the database engine in use, and this is fully transparent to the developer. Common clauses are very standardized so few differences can be noticed between each of them. Bigger differences can be typically seen in more advanced, less used clauses.

When the application is starting up, LiveSQL detects the specific database and version for each data source, and chooses the LiveSQLDialect to use for each one. This dialect deals automatically with all syntax changes behind the scenes when a query is executed.

The LiveSQLDialect can also be specified in the `application.properties` file , in case the developer
wants to disable the auto-detect functionality and prefers to declare it explicitly. This can be configured separately for each data source. See [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md) for more details.


## LiveSQL Statements

LiveSQL includes the four DML SQL statements SELECT, INSERT, UPDATE, and DELETE.

Note that all these statements participate in Spring transactions &mdash; in the same way as any other CRUD or Nitro query would do &mdash; according to the transaction demarcation and rules defined in the methods through Spring annotations.

The LiveSQL statements are described below:

- The [SELECT](./syntax/select.md) Statement

    The SELECT statement, by far, has the most complex syntax and it's the only one that returns data rows. The SQL Standard defines a list of clauses to filter rows (WHERE), to join multiple tables (FROM and JOIN), to aggregate them (GROUP BY) and to sort them (ORDER BY). SELECT statements can also include subqueries to define more complex logic. It implements the WITH clause, including it the WITH RECURSIVE variant.

- The [INSERT](./syntax/insert.md) Statement

    The INSERT statement inserts data into a table. The inserted values can be speficied as literal values in the statement itself or as the result of a query; both options are implemented in LiveSQL. Data can also be inserted into tables through views; however, each database defines different eligibility rules to decide which ones can be used for this purpose.

- The [UPDATE](./syntax/update.md) Statement

    LiveSQL implements the SQL-92 version of the UPDATE statement. Subqueries can be used to compute values or to search for rows. Rows can also be updated through views depending on the specific database restrictions.

- The [DELETE](./syntax/delete.md) Statement

    LiveSQL implements the SQL-92 version of the DELETE statement. Subqueries can be used to compute values or to search for rows. Rows can also be deleted through views depending on the specific database restrictions.

## The Expression Language

The Expression Language enhances the functionality of LiveSQL by allowing complex expressions for numeric computations, date and time arithmetic, boolean logic for predicates, advanced SQL features, etc. Each section below explains a different aspect of it:

- [Expressions, Operators &amp; Functions](./syntax/expressions.md)

    All the basic operators such as `+`, `-`, `*`, `/`, `=`, `<>`, `<`, `>`, `<=`, `>=`, `LIKE`, `BETWEEN`, `AND`, `OR`, `NOT`, `||`, etc., as well as common functions such as `ROUND()`,
`SUBSTRING()`, `CURRENT_DATE()`, `COALESCE()`, etc. Expressions can be used in any place of the SQL query where a scalar value or a predicate can be used. In short, they can appear in the select list of the query or subquery, in the `WHERE` clause to define simple or complex search rules, in the `GROUP BY` clause, `ORDER BY` clause, etc.

- [Subqueries](./syntax/subqueries.md)

    Subqueries are a standard feature of the SQL language that greatly enhances the expressiveness of a query. LiveSQL implements all typical subqueries such as scalar subqueries, table expressions, `IN/NOT IN`, `EXISTS/NOT EXISTS`, assymmetric operators, CTEs (Common Table Expressions), and lateral joins. The `WITH` and `WITH RECURSIVE` clauses are implemented. Aditionally, plain and correlated subqueries can be expressed in LiveSQL.

- [Set Operators](./syntax/set-operators.md)

    The set operators UNION [ALL], INTERSECT [ALL], EXCEPT [ALL] combine two or more SELECT queries into a single SELECT. Set operators can be inlined in the same level or nested using parenthesis, and can use ordering, offsets, and limiting, according to the SQL Standard.

- [Predefined Functions](./syntax/expressions.md)

    LiveSQL includes a basic list of predefined functions for each specific data type, that can be enhanced with custom functions.

- [Aggregate Functions](./syntax/aggregate-functions.md)

    The traditional aggregate functions such as `SUM()`, `MIN()`, and `MAX()`, etc. defined in the SQL-92 Standard. These functions are typically used when grouping rows using the `GROUP BY` clause; they compute a single value from a set of values from different rows.

- [Window Functions](./syntax/window-functions.md)

    Window functions &mdash; implemented with the `OVER()` clause &mdash; were defined in the SQL:2003 SQL Standard and they aggregate values from multiple rows. They don't consolidate rows, however, but keep rows in non-aggregated form. These functions enhance the traditional aggregate functions and define a new set of them to peek at and to compute values using related rows.

- [Parameterized Scalars](./syntax/expressions.md#boxing-scalars) and [Literal Scalars](./syntax/literals.md)

    Parameterized scalars are included using `sql.val()` or autoboxed by default in the LiveSQL syntax. Literal scalars can be explicitly added using `sql.literal()` when needed for specific cases, or to
improve query performance.

- [Extending LiveSQL Functions](./extending-livesql-functions.md)

    If a function is not included in the basic list of predefined functions it can be easily declared as a custom LiveSQL function. Once defined, custom functions can be used seamlessly as part of the LiveSQL syntax in any expression.


## Optimistic and Pessimistic Locking

While updating database rows, LiveSQL can be used to implement Optimistic Locking  using several
strategies that could suit different scenarios or existing database tables. See
[Optimistic Locking](optimistic-locking.md) for details.

LiveSQL also includes Pessimistic Locking that is implemented by the use of row-level lockings. These locks are
acquired by the use of the FOR UPDATE clause (and their variations in different databases). See
[Pessimistic Locking](syntax/for-update.md) for details.



Consider that cursors only live between the boundaries of a database transaction &ndash; that is, methods annotated with the
`@Transactional` annotation &ndash; and that they are automatically closed when the transaction ends.


## Extra Functionality

- [Previewing LiveSQL](./previewing-livesql.md)
- [LiveSQL and CRUD](./livesql-and-crud.md)
- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md)



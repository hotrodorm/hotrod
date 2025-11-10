# LiveSQL

LiveSQL allows you to write flexible SQL queries directly within your application code.

It utilizes tables, views, and columns &mdash; modeled by the CRUD module &mdash; to assemble and execute SELECT, INSERT, UPDATE, and DELETE queries.

## Example

Let’s consider a database table named EMPLOYEE. We can search this table using a custom predicate, as illustrated below:

```java
@Autowired
private LiveSQL sql;

private void searching() {
  EmployeeTable e = EmployeeDAO.newTable();

  List<Row> rows = this.sql
    .select()
    .from(e)
    .where(a.salary.plus(a.bonus).ge(75).and(e.name.like("A%")))
    .execute();
}
```

In the code, the DAO is used to create an instance of the table that will be used within the FROM query clause. Additional instances of the same or other tables &mdash; and/or views &mdash; can be used to define queries with joins.

The table instance allows you to reference the table's columns (e.g., e.name) and to assemble complex expressions for use in the query. Behind the scenes, LiveSQL will construct the query as follows:


```sql
SELECT * FROM employee WHERE salary + bonus >= 75 AND name LIKE 'A%'
```

Finally, the `execute()` method runs the query and returns the result set as a list of rows.

## LiveSQL Dialects

When assembling a query, LiveSQL automatically generates the appropriate syntax for the database engine in use, making this process completely transparent to developers. While common clauses are largely standardized, allowing for minimal noticeable differences, more significant variations often emerge in advanced or less commonly used clauses.

During application startup, LiveSQL detects the specific database and version for each data source. It then selects the appropriate LiveSQLDialect for each one, which manages all syntax changes behind the scenes during query execution.

Developers can also specify the LiveSQLDialect in the `application.properties` file. This option is useful for those who wish to disable the auto-detection feature and declare the dialect explicitly. This configuration can be set individually for each data source. For more details, see [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md).

## LiveSQL Statements

LiveSQL supports the four essential DML SQL statements: SELECT, INSERT, UPDATE, and DELETE.

All of these statements participate in Spring transactions, functioning similarly to any other CRUD or Nitro queries. This adherence to transaction demarcation and rules is dictated by the Spring annotations defined in the methods.

The LiveSQL statements are described below:

- The [SELECT](./syntax/select.md) Statement

    The SELECT statement features the most complex syntax and is the only one that returns data rows. The SQL Standard defines several clauses to filter rows (WHERE), join multiple tables (FROM and JOIN), aggregate data (GROUP BY), and sort results (ORDER BY). Additionally, SELECT statements can include subqueries and set operations (UNION, INTERSECT, EXCEPT) to establish more intricate logic. It also supports Common Table Expressions (WITH and WITH RECURSIVE).

- The [INSERT](./syntax/insert.md) Statement

    The INSERT statement is used to add data to a table. The values to be inserted can be specified as literal values within the statement itself or as the result of a query; both options are supported in LiveSQL. Additionally, data can be inserted into tables through views. However, each database establishes different eligibility rules to determine which views can be used for this purpose.

- The [UPDATE](./syntax/update.md) Statement

    LiveSQL implements the SQL-92 version of the UPDATE statement. Subqueries can be utilized to compute values or to search for specific rows. Additionally, rows can be updated through views, contingent upon the restrictions defined by the specific database.

- The [DELETE](./syntax/delete.md) Statement

    LiveSQL implements the SQL-92 version of the DELETE statement. Subqueries can be used to compute values or to identify specific rows. Additionally, rows can be deleted through views, subject to the restrictions imposed by the specific database.

## The Expression Language

The Expression Language enhances LiveSQL's functionality by enabling complex expressions for numeric computations, date and time arithmetic, boolean logic for predicates, and advanced SQL features. Each section below elaborates on a different aspect of this powerful tool:

- [Expressions, Operators &amp; Functions](./syntax/expressions.md)

    All basic operators, such as `+`, `-`, `*`, `/`, `=`, `<>`, `<`, `>`, `<=`, `>=`, `LIKE`, `BETWEEN`, `AND`, `OR`, `NOT`, `||`, and others, are supported, along with common functions like `ROUND()`, `SUBSTRING()`, `CURRENT_DATE()`, `COALESCE()`. Expressions can be used in any part of the SQL query where a scalar value or predicate is valid. Specifically, they can appear in the select list of a query or subquery, in the WHERE clause to define simple or complex search criteria, and in the GROUP BY and ORDER BY clauses, among other places.

- [Subqueries](./syntax/subqueries.md)

    Subqueries are a standard feature of the SQL language that significantly enhance the expressiveness of a query. LiveSQL supports all typical subquery types, including scalar subqueries, table expressions, IN/NOT IN, EXISTS/NOT EXISTS, asymmetric operators, Common Table Expressions (CTEs), and lateral joins. The WITH and WITH RECURSIVE clauses are also implemented. Additionally, both plain and correlated subqueries can be effectively expressed in LiveSQL.

- [Set Operators](./syntax/set-operators.md)

    The set operators UNION [ALL], INTERSECT [ALL], and EXCEPT [ALL] combine two or more SELECT queries into a single result set. These operators can be used inline at the same level or nested within parentheses, allowing for flexibility in query construction. Additionally, they support ordering, offsets, and limits in accordance with the SQL Standard.

- [Predefined Functions](./syntax/expressions.md)

    LiveSQL provides a basic list of predefined functions for each specific data type, which can be extended with custom functions to enhance functionality.

- [Aggregate Functions](./syntax/aggregate-functions.md)

    Traditional aggregate functions, such as SUM(), MIN(), and MAX(), that are defined in the SQL-92 Standard. These functions are commonly used to compute a single value from a set of values across multiple rows when grouping rows with the GROUP BY clause.

- [Window Functions](./syntax/window-functions.md)

    Window functions, implemented with the OVER() clause, were introduced in the SQL:2003 Standard. Unlike traditional aggregate functions, window functions aggregate values from multiple rows without consolidating them, allowing the original rows to remain in non-aggregated form. These functions extend the capabilities of traditional aggregate functions by providing a new set of tools to peek at and compute values using related rows.

- [Parameterized Scalars](./syntax/expressions.md#boxing-scalars) and [Literal Scalars](./syntax/literals.md)

    Parameterized scalars can be included using `sql.val()`, or they are autoboxed by default in LiveSQL syntax. Additionally, literal scalars can be explicitly added using `sql.literal()` when needed for specific cases or to enhance query performance.

- [Extending LiveSQL Functions](./extending-livesql-functions.md)

    If a function is not part of the predefined list of functions, it can easily be declared as a custom LiveSQL function. Once defined, custom functions can be seamlessly integrated into the LiveSQL syntax within any expression.

## Optimistic and Pessimistic Locking

When updating database rows, LiveSQL supports Optimistic Locking through various strategies that can be tailored to fit different scenarios or existing database tables. For more details, see [Optimistic Locking](optimistic-locking.md).

Additionally, LiveSQL incorporates Pessimistic Locking, which is implemented using row-level locks. These locks are acquired through the FOR UPDATE clause and its variations across different databases. For further information, refer to [Pessimistic Locking](syntax/pessimistic-locking.md).

In the context of pessimistic locking, it is important to note that locks exist only within the boundaries of a database transaction &mdash; specifically, in methods annotated with the `@Transactional` annotation. These locks are automatically released once the transaction ends; therefore, pessimistic locking cannot span multiple transactions.


## Related Functionality

- [Previewing LiveSQL](./previewing-livesql.md)
- [LiveSQL and CRUD](./livesql-and-crud.md)
- [Designating a LiveSQL Dialect](designating-a-livesql-dialect.md)



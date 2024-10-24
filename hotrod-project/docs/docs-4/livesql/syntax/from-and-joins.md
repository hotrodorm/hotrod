# The FROM and JOIN Clauses

The `FROM` and `JOIN` clauses specify the tables, views, and subqueries where the data of a SELECT query comes from. Subqueries
are supported starting in version 4.1.

A LiveSQL query can join multiple tables and views in a SELECT query.


## Example

The following query joins two tables and a view.

```java
AccountTable a = AccountDAO.newTable("a");
TransactionTable t = TransactionDAO.newTable("t");
BigAccountsView ba = BigAccountsDAO.newView("ba");

List<Row> rows = sql 
    .select() 
    .from(a) 
    .join(t, t.accountId.eq(a.id))
    .join(ba, ba.accountId.eq(t.accountId))
    .execute();
```

The resulting query is:

```sql
SELECT * 
FROM account a
JOIN transaction t ON t.account_id = a.id
JOIN big_accounts ba ON ba.account_id = t.account_id
```

## Table and View Instances

Notice that, first of all, all table and view instances are instantiated. Once this is done, they can be used in the LiveSQL query.

In the example above, the table and view instances `a`, `t`, and `ba` are created using the DAO methods
`newTable()` and `newView()` respectively. They can include an optional alias parameter (recommended) 
that is used in the query. If omitted, LiveSQL will pick one automatically.

Each table or view can participate multiple times in the same query (e.g reflexive queries). In that case it needs to be instantiated
multiple times with different alias to differentiate them.

Tables and views are added to the query with the `from()` and `join()` clauses (and its variations).


## Joining

If the query includes more than a single table, view, or subquery it will include one or more *join* clauses for them. The semantics 
of them follow the standard semantics of the SQL Standard.

The first table or view is included in the `FROM` clause and the rest is included using `JOIN` clauses.


## Self-Referencing Joins

If a query includes the same table or view more than once &mdash; as in self-referencing joins (parent and child table 
are the same one) &mdash; then it's mandatory to define aliases for each instance. This ensures the appropriate columns
are used from each table instance when writing the join predicates.

The following example illustrates a self-referencing join:


```java
EmployeeTable e = EmployeeDAO.newTable("e");
EmployeeTable m = EmployeeDAO.newTable("m"); // same table with alias "m" for "manager"

List<Row> rows = sql 
    .select(e.name, m.name.as("manager_name")) 
    .from(e) 
    .leftJoin(m, m.id.eq(e.managerId))
    .execute();
```

The resulting query is:

```sql
SELECT e.name, m.name as "manager_name"
FROM employee e
LEFT JOIN employee m ON m.id = e.manager_id
```


## Join Predicates

The join predicate is used to match rows in a traditional join with an `ON` clause. The join predicate must be any expression that
evaluates to a boolean.

**Note**: In the LiveSQL docs a *boolean expression* is equivalent to a *predicate*. They are used interchangeably.

Most of the time join predicates are equalities: this leads to a special kind of joins called *equi-joins*. We less often see other more complex 
expressions as join predicates: that's the general form of a join called *theta joins*.


## Join Types

LiveSQL implements the most common types of join:

| Join Type | Variation | in LiveSQL | Typical SQL Syntax |
| -- | -- | -- | -- |
| INNER JOIN | *theta-join* | `join(t, predicate)` | `JOIN t ON predicate` |
| INNER JOIN | USING | `join(t, column...)` | `JOIN t USING (column...)` |
| INNER JOIN | NATURAL | `naturalJoin(t)` | `NATURAL JOIN t` |
| LEFT JOIN | *theta-join* | `leftJoin(t, predicate)` | `LEFT JOIN t ON predicate` |
| LEFT JOIN | USING | `leftJoin(t, column...)` | `LEFT JOIN t USING (column...)` |
| LEFT JOIN | NATURAL | `naturalLeftJoin(t)` | `NATURAL LEFT JOIN t` |
| RIGHT JOIN | *theta-join* | `rightJoin(t, predicate)` | `RIGHT JOIN t ON predicate` |
| RIGHT JOIN | USING | `rightJoin(t, column...)` | `RIGHT JOIN t USING (column...)` |
| RIGHT JOIN | NATURAL | `naturalRightJoin(t)` | `NATURAL RIGHT JOIN t` |
| FULL JOIN | *theta-join* | `fullJoin(t, predicate)` | `FULL JOIN t ON predicate` |
| FULL JOIN | USING | `fullJoin(t, column...)` | `FULL JOIN t USING (column...)` |
| FULL JOIN | NATURAL | `naturalFullJoin(t)` | `NATURAL FULL JOIN t` |
| CROSS JOIN | -- | `crossJoin(t)` | `CROSS JOIN t` |
| UNION JOIN | -- | `unionJoin(t)` | `UNION JOIN t` |


Joins support subqueries, table expressions, CTEs (plain and recursive), and lateral joins. Tabular functions are not yet supported. 
See [Subqueries](./subqueries.md) for details and examples.

**Note**: Since the exact SQL Syntax may vary from database to database, LiveSQL adapts it automatically behind the scenes for each specific database.










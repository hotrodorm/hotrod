# The FROM and JOIN Clauses

The `FROM` and `JOIN` clauses specify the tables and views where the data of a SELECT query comes from.

A LiveSQL query can join multiple tables and views in a SELECT query.


## Example

 The following
query joins two tables and a view.

```java
AccountTable a = AccountDAO.newTable("a");
TransactionTable t = TransactionDAO.newTable("t");
BigAccountsView ba = BigAccountsDAO.newView("ba");

List<Map<String, Object>> rows = this.sql 
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

Notice that first all table and view instances are instantiated. Then, they can be used in the LiveSQL query.

In the example above, the table and view instances such as `a`, `t`, and `ba` are created using the DAO methods
`createTable()` and `createView()` respectively. They can include an optional alias parameter (recommended) 
that is used in the query. If omitted, LiveSQL will pick one automatically.

Tables and views are added to the query with the `from()` and `join()` clauses.


## Joining

If the query include more than a single table or view it will include one or more *join* clauses for them. The semantics 
of them follow the standard semantics of the SQL Standard

## Self-Referencing Joins

If a query includes the same table or view more than once &mdash; as in self-referencing joins (parent and child table 
are the same one) &mdash; then it's mandatory to define aliases for each instance. This ensures the appropriate columns
are used from each table instance when writing the join predicates.

The following example illustrate a self-referencing join:


```java
EmployeeTable e = EmployeeDAO.newTable("e");
EmployeeTable m = EmployeeDAO.newTable("m"); // same table with alias "m" for "manager"

List<Map<String, Object>> rows = this.sql 
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

The join predicate is the second parameter of a traditional join with an `ON` clause. The join predicate can be any expression that
evaluates to a boolean.

**Note**: In the LiveSQL docs a *boolean expression* is equivalent to a *predicate*. They are used interchangeably.

Most of the time join predicates are equalities: this leads to a special kind of joins called *equi-joins*. We less often see other more complex 
expressions as join predicates: that's the general form of a join called *theta joins*.


## Join Types

LiveSQL implements the most common join types:

| Join Type | Variation | LiveSQL code | SQL Syntax |
| -- | -- | -- | -- |
| INNER JOIN | *theta-join* | `join(t, predicate)` | `JOIN t ON predicate` |
| INNER JOIN | USING | `join(t, column...)` | `JOIN t USING (column...)` |
| INNER JOIN | NATURAL | `naturalJoin(t)` | `NATURAL JOIN t` |
| LEFT JOIN | *theta-join* | `leftJoin(t, predicate)` | `LEFT JOIN t ON predicate` |
| LEFT JOIN | USING | `leftJoin(t, column...)` | `JOIN t USING (column...)` |
| LEFT JOIN | NATURAL | `naturalLeftJoin(t)` | `NATURAL LEFT JOIN t` |
| RIGHT JOIN | *theta-join* | `rightJoin(t, predicate)` | `RIGHT JOIN t ON predicate` |
| RIGHT JOIN | USING | `rightJoin(t, column...)` | `JOIN t USING (column...)` |
| RIGHT JOIN | NATURAL | `naturalRightJoin(t)` | `NATURAL RIGHT JOIN t` |
| FULL JOIN | *theta-join* | `fullJoin(t, predicate)` | `FULL JOIN t ON predicate` |
| FULL JOIN | USING | `fullJoin(t, column...)` | `JOIN t USING (column...)` |
| FULL JOIN | NATURAL | `naturalFullJoin(t)` | `NATURAL FULL JOIN t` |
| CROSS JOIN | -- | `crossJoin(t)` | `CROSS JOIN t` |
| UNION JOIN | -- | `unionJoin(t)` | `UNION JOIN t` |


Joining subqueries, literal table expressions, tabular functions, lateral joins, and aliasing a table expression are not yet supported.

**Note**: The equivalent SQL Syntax may vary from database to database. LiveSQL adapts it accordingly behind the scenes.









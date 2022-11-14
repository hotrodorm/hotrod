# The FROM and JOIN Clauses

The `FROM` and `JOIN` clauses specify the tables and views where the data comes from.

## Example

Multiple tables and views can be combined into a query to join them and produce the desired result. The following
query joins two tables and a view.

```java
AccountTable a = AccountDAO.newTable("a");
TransactionTable t = TransactionDAO.newTable("t");
BigAccountsView ba = BigAccountsDAO.newTable("ba");

List<Map<String, Object>> rows = this.sql //
    .select() //
    .from(a) //
    .join(t, t.accountId.equals(a.id)
    .join(ba, ba.accountId.equals(t.accountId)
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

First all table and view instances are instantiated, so they can be used in the LiveSQL query. The table/view instances such 
as `a`, `t`, and `ba` are created using the DAO methods `createTable` and `createView`. They optionally have an alias 
parameter that will be used in the query. If omitted, LiveSQL will pick one automatically.

Tables and view are added right after the select-list using a `from()` clause.


## Joining

If the query include more than a single table or view it will include *join* clauses to join them. There are several variation of these clauses

**Note**: Most join clauses are implemented by LiveSQL, except for lateral joins.

## Self-Referencing Joins

If a query includes the same table or view more than once &mdash; as in self-referencing joins (parent and child table are the same one),
then it's mandatory to define aliases for each instance. This ensures the appropriate columns are used from each table instance 
when writing the join predicates.


## Join Predicates

The join predicate is the second parameter of a join and can be any expression that evaluates to a boolean. In LiveSQL a *boolean expression*
is equivalent to a *predicate*.

Most of the time join predicates are equalities: this leads to a special kind of joins called *equi-joins*. We less often see other more complex 
expressions as join predicates: that's the general form of a join called *theta joins*.


## Join Types

LiveSQL implements the most common join types:

| Join Type | Variation | LiveSQL code |
| -- | -- | -- |
| INNER JOIN | *&lt:redicate>* | `join(t, <predicate>)` |
| INNER JOIN | USING | `join(t, <column>...)` |
| INNER JOIN | NATURAL | `naturalJoin(t)` |
| LEFT JOIN | *&lt:redicate>* | `leftJoin(t, <predicate>)` |
| LEFT JOIN | USING | `leftJoin(t, <column>...)` |
| LEFT JOIN | NATURAL | `naturalLeftJoin(t)` |
| RIGHT JOIN | *&lt:redicate>* | `rightJoin(t, <predicate>)` |
| RIGHT JOIN | USING | `rightJoin(t, <column>...)` |
| RIGHT JOIN | NATURAL | `naturalRightJoin(t)` |
| FULL JOIN | *&lt:redicate>* | `fullJoin(t, <predicate>)` |
| FULL JOIN | USING | `fullJoin(t, <column>...)` |
| FULL JOIN | NATURAL | `naturalFullJoin(t)` |
| CROSS JOIN | -- | `crossJoin(t)` |
| UNION JOIN | -- | `unionJoin(t)` |










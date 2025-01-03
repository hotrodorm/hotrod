# The GROUP BY Clause

The `GROUP BY` clause defines a criteria to aggregate rows in the result set of a `SELECT` statement.

It has a single form where it accepts a list of expressions as parameters. This list of expressions can
include one or more expressions and each one can be as simple or as complex as needed.


## Grouping By Plain Values

The following query computes the total balance of the checking accounts by region and type:


```java
AccountTable a = AccountDAO.newTable("a");

List<Row> rows = this.sql
    .select(a.region, a.type, sql.sum(a.balance))
    .from(a) 
    .where(w.type.eq("CHK"))
    .groupBy(a.region, a.type)
    .execute();
```

The resulting query is:

```sql
SELECT a.region, a.type, sum(a.balance)
FROM account a
WHERE type = 'CHK'
GROUP BY a.region, a.type
```

In this example the grouping expressions are very simple: just plain columns. No extra processing
is needed to separate the groups. 

Note that the select list includes the `sum()` function. This is an aggregate function that can be
used in aggregate queries. See [Aggregate Functions](./aggregate-functions.md) for details.


## Grouping By Complex Expressions

Not only plain values can be used to aggregate rows. Virtually any expression can be used for it [^1]. See [Expressions](./expressions.md) 
for more details on expressions.

[^1]: Technically not all expressions can be used for aggregating. Different databases impose different restrictions on them. The most 
common restrictions affect values that do not have inherent ordering such as images, UUIDs, arrays, etc., or values that are too large 
and impractical for this purpose.

The following query aggregates rows by a more complex expression:


```java
AccountTable a = AccountDAO.newTable("a");

List<Row> rows = this.sql
    .select(
      a.region,
      sql.caseWhen(a.type.in("CHK", "INV"), "DISP").elseValue("N/A").end(),
      sql.sum(a.balance))
    .from(a) 
    .where(w.type.eq("CHK"))
    .groupBy(a.region, sql.caseWhen(a.type.in("CHK", "INV"), "DISP").elseValue("N/A").end())
    .execute();
```

The resulting query is:

```sql
SELECT a.region, case when a.type in ('CHK', 'INV') then 'DISP' else 'N/A' end, sum(a.balance)
FROM account a
WHERE type = 'CHK'
GROUP BY a.region, case when a.type in ('CHK', 'INV') then 'DISP' else 'N/A' end
```

This query aggregates the rows by a value computed on the fly by a `CASE` expression.

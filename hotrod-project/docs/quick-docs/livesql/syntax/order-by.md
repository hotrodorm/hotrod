# The ORDER BY Clause

The `ORDER BY` clause defines a criteria to sort rows in the result set of a `SELECT` statement.

It has a single form where it accepts a list of *ordering expressions* as parameters. An ordering
expression is a typical LiveSQL expression that is decorated with extra ordering features.


## Ordering Features

The ordering of rows can be specified with virtually [^1] any LiveSQL expression qualified with an 
ordering. Additionally each expression can specify the behavior of nulls. In short these ordering
expressions include:

[^1]: Technically not all expressions can be used for sorting. Different databases impose restrictions
on them. The most common restrictions affect values that do not have inherent ordering such as images, 
UUIDs, arrays, etc., or values that are too large and impractical for this purpose.

- Ascending or descending ordering (required).
- Location of nulls in the ordering domain (optional).

To sort in ascending order qualify the expression with `.asc()`. If a descending order is needed then the 
expression needs to be qualified with the `.desc()`. This tells LiveSQL that the expression should be
sorted in reverse order.

Nulls, on the other hand, are treated in special ways; they can be considered below or above any value in the
database. Unfortunately, each database implements the sorting of nulls in a different way, and this behavior 
may not correspond to the requirements of the query. In case it doesn't LiveSQL adds the methods
`nullsFirst()` and `nullsLast()` to clearly establish this ordering.


## Example

The following query returns the unpaid payments, sorted by client and in descending due date:

```java
PaymentTable p = PaymentDAO.newTable("p");

List<Map<String, Object>> rows = this.sql
    .select()
    .from(p) 
    .where(p.status.eq("UNPAID"))
    .orderBy(p.clientName, p.dueDate.desc())
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM payment
WHERE status = 'UNPAID'
ORDER BY client_name, due_date desc
```

In this example the grouping expressions are very simple: just plain columns. SELECT queries can sort 
by virtually any expression computed on the fly. See [Expressions](./expressions.md) for details.


## Combining Aggregations and Sorting

When the query is aggregated the sorting expressions need to be aggregated as well. In the presence of
a `GROUP BY` clause non-aggregated expressions are out of the SQL scope for the `ORDER BY` clause.

The following query finds unpaid payments, groups them by client, and finally sorts each client by minimum due date:


```java
PaymentTable p = PaymentDAO.newTable("p");

List<Map<String, Object>> rows = this.sql
    .select(p.clientName, sql.min(p.dueDate), sql.max(p.dueDate))
    .from(p) 
    .where(p.status.eq("UNPAID"))
    .groupBy(p.clientName)
    .orderBy(sql.min(p.dueDate).desc())
    .execute();
```

The resulting query is:

```sql
SELECT client_name, min(due_date), max(due_date)
FROM payment
WHERE status = 'UNPAID'
GROUP BY client_name
ORDER BY min(due_date) desc
```

Note the `.orderBy()` method includes an aggregated expression.


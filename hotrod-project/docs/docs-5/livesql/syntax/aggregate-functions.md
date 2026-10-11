# Aggregate Functions

Aggregate functions aggregate values from multiple rows to compute a single result.

Traditional aggregate functions consolidate rows into fewer rows according to the grouping
criteria. However, in SQL:2003 the SQL Standard introduced *window functions*; although they
still compute values from multiple rows, they do not consolidate rows.


## Traditional Aggregation

These functions consolidate rows according to the grouping criteria:

| Aggregate Function in SQL | In LiveSQL |
| -- | -- |
| `COUNT(*)` | `sql.count()` |
| `COUNT(<expression>)` | `sql.count(<expression>)` |
| `SUM(<expression>)` | `sql.sum(<expression>)` |
| `MIN(<expression>)` | `sql.min(<expression>)` |
| `MAX(<expression>)` | `sql.max(<expression>)` |
| `AVG(<expression>)` | `sql.avg(<expression>)` |
| `GROUP_CONCAT(<expression>, <sep>, <ordering>)` | `sql.groupConcat(<expression>, <sep>, <ordering>)` |
| `COUNT(DISTINCT <expression>)` | `sql.countDistinct(<expression>)` |
| `SUM(DISTINCT <expression>)` | `sql.sumDistinct(<expression>)` |
| `AVG(DISTINCT <expression>)` | `sql.avgDistinct(<expression>)` |
| `GROUP_CONCAT(DISTINCT <expression>, <sep>, <ordering>)` | `sql.groupConcatDistinct(<expression>, <sep>, <ordering>)` |

Starting in SQL:2003 aggregation functions can also implement the FILTER clause to include only a subset of the values:

| Aggregate Function with FILTER in SQL | In LiveSQL |
| -- | -- |
| `COUNT(*) FILTER(WHERE <predicate>)` | `sql.count().filter(<predicate>)` |
| `COUNT(<expression>) FILTER(WHERE <predicate>)` | `sql.count(<expression>).filter(<predicate>)` |
| `SUM(<expression>) FILTER(WHERE <predicate>)` | `sql.sum(<expression>).filter(<predicate>)` |
| `MIN(<expression>) FILTER(WHERE <predicate>)` | `sql.min(<expression>).filter(<predicate>)` |
| `MAX(<expression>) FILTER(WHERE <predicate>)` | `sql.max(<expression>).filter(<predicate>)` |
| `AVG(<expression>) FILTER(WHERE <predicate>)` | `sql.avg(<expression>).filter(<predicate>)` |
| `COUNT(DISTINCT <expression>)` | `sql.countDistinct(<expression>).filter(<predicate>)` |
| `SUM(DISTINCT <expression>)` | `sql.sumDistinct(<expression>).filter(<predicate>)` |
| `AVG(DISTINCT <expression>)` | `sql.avgDistinct(<expression>).filter(<predicate>)` |

LiveSQL automatically adapts the query for pre-SQL:2003 databases using the CASE solution. For example, the SQL:2003 query:

```
SELECT
  sum(amount) FILTER(WHERE branch_id in (15, 18))
FROM invoice
WHERE status = 'PAID';
```

Is rephrased automatically behind the scenes for older databases as:

```
SELECT
  sum(CASE WHEN branch_id in (15, 18) THEN amount END)
FROM invoice
WHERE status = 'PAID';
```

## Window Functions

All aggregations above can also be combined with window functions. Window functions compute values from multiple rows but do not consolidate rows. Namely: `SUM()`, `MIN()`, `MAX()`, `AVG()`, `GROUP_CONCAT()`.

For example, the SQL expression:

```sql
  sum(d.amount)
    FILTER(WHERE d.accrued_date <= date '2026-05-15')
    OVER (PARTITION BY d.account_id ORDER BY depositDate DESC)
```

Can be expressed in LiveSQL as:

```java
  sql.sum(d.amount)
     .filter(d.accruedDate.le(LocalDate.of(2026, 5, 15)))
     .over().partitionBy(d.accountId).orderBy(d.depositDate.desc()).end()
```



See [Window Functions](./window-functions.md) for more details.


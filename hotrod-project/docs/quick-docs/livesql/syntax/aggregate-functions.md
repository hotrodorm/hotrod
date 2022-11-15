# Aggregate Functions

Aggregate functions aggregate values from multiple rows to compute a single result.

Traditional aggregate functions consolidate rows into fewer rows according to the grouping
criteria. However, in SQL:2003 the SQL Standard introduced *window functions*; although they
still compute values from multiple rows, they do not consolidate rows.


## Traditional Aggregation

These functions consolidate rows according to the grouping criteria:

| Aggregate Function | In LiveSQL |
| -- | -- |
| `COUNT(*)` | `sql.count()` |
| `COUNT(<expression>)` | *not implemented* |
| `SUM(<expression>)` | `sql.sum(<expression>)` |
| `MIN(<expression>)` | `sql.min(<expression>)` |
| `MAX(<expression>)` | `sql.max(<expression>)` |
| `AVG(<expression>)` | `sql.avg(<expression>)` |
| `GROUP_CONCAT(<expression>, <sep>, <ordering>)` | `sql.groupConcat(<expression>, <sep>, <ordering>)` |
| `COUNT(DISTINCT <expression>)` | `sql.countDistinct(<expression>)` |
| `SUM(DISTINCT <expression>)` | `sql.sumDistinct(<expression>)` |
| `AVG(DISTINCT <expression>)` | `sql.avgDistinct(<expression>)` |
| `GROUP_CONCAT(DISTINCT <expression>, <sep>, <ordering>)` | `sql.groupConcatDistinct(<expression>, <sep>, <ordering>)` |


## Window Functions

Window functions compute values from multiple rows but do not consolidate rows. 

Some traditional aggregate functions have been enhanced to operate as window functions as well. Namely: `SUM()`,
`MIN()`, `MAX()`, `AVG()`, `GROUP_CONCAT()`.

See[Window Functions](./window-functions.md) for more details.


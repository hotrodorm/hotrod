# Aggregate Functions

Aggregate functions aggregate values from multiple rows to compute a single result.

In the traditional form aggregations reduce the number of rows according to each group 
being aggregated. Alternatively, used as window functions aggregations can peek at other
rows and compute aggregated values without for each row without compacting the result set.

Aggregate functions can be divided in several types.

## Traditional Aggregation

These functions consolidate rows according to the grouping criteria:

| Aggregate Function | In LiveSQL |
| -- | -- |
| COUNT(*) | sql.count() |
| COUNT(<expression>) | *not yet implemented* |
| SUM(<expression>) | sql.sum(<expression>) |
| MIN(<expression>) | sql.min(<expression>) |
| MAX(<expression>) | sql.max(<expression>) |
| AVG(<expression>) | sql.avg(<expression>) |
| GROUP_CONCAT(<expression>, <sep>, <ordering>) | sql.groupConcat(<expression>, <sep>, <ordering>) |
| COUNT(DISTINCT <expression>) | sql.countDistinct(<expression>) |
| SUM(DISTINCT <expression>) | sql.sumDistinct(<expression>) |
| AVG(DISTINCT <expression>) | sql.avgDistinct(<expression>) |
| GROUP_CONCAT(DISTINCT <expression>, <sep>, <ordering>) | sql.groupConcatDistinct(<expression>, <sep>, <ordering>) |

## Window Functions

The following aggregate expressions can also operate as window functions. They do not
consolidate rows, but only compute values from multiple rows.

| Aggregate Function | In LiveSQL |
| -- | -- |
| SUM(<expression>) OVER(...) | sql.sum(<expression>).over(...) |
| MIN(<expression>) OVER(...) | sql.min(<expression>).over(...) |
| MAX(<expression>) OVER(...) | sql.max(<expression>).over(...) |
| AVG(<expression>) OVER(...) | sql.avg(<expression>).over(...) |
| GROUP_CONCAT(<expression>, <sep>, <ordering>) OVER(...) | sql.groupConcat(<expression>, <sep>, <ordering>).over(...) |

























# Window Functions

Window functions do not consolidate rows, but only compute values from multiple rows and do not require
a `GROUP BY` clause. Some traditional aggregate functions have been enhanced to also work as window functions.

They can be divided in three groups, according to their semantics: enhanced, analytical, and positional analytical functions.


## Enhanced Window Functions

These are the traditional aggregate functions, enhanced to work as window functions:

| Aggregate Function | In LiveSQL |
| -- | -- |
| `SUM(<expression>) OVER(...)` | `sql.sum(<expression>).over(...)` |
| `MIN(<expression>) OVER(...)` | `sql.min(<expression>).over(...)` |
| `MAX(<expression>) OVER(...)` | `sql.max(<expression>).over(...)` |
| `AVG(<expression>) OVER(...)` | `sql.avg(<expression>).over(...)` |
| `GROUP_CONCAT(<expression>, <sep>, <ordering>) OVER(...)` | `sql.groupConcat(<expression>, <sep>, <ordering>).over(...)` |


## Analytical Functions

These functions rank rows according to their values and produce a result in a different domain of values.

| Aggregate Function | In LiveSQL |
| -- | -- |
| `ROW_NUMBER() OVER(...)` | `sql.rowNumber().over(...)` |
| `RANK(<expression>) OVER(...)` | `sql.rank(<expression>).over(...)` |
| `DENSE_RANK(<expression>) OVER(...)` | `sql.denseRank(<expression>).over(...)` |
| `NTILE(<expression>) OVER(...)` | `sql.ntile(<expression>).over(...)` |


## Positional Analytical Functions

Positional analytical functions peek at other rows of the table, view or result set to retrieve a value. They
can be useful to find changes in values over time or to differentiate different groups of data according to custom
logic.

| Aggregate Function | In LiveSQL |
| -- | -- |
| `LEAD(<expression>) OVER(...)` | `sql.lead(<expression>).over(...)` |
| `LAG(<expression>) OVER(...)` | `sql.lead(<expression>).over(...)` |



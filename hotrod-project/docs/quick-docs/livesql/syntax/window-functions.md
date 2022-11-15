# Window Functions

The SQL Standard SQL:2003 defined window functions. Window functions do not consolidate rows, but only compute
values from multiple rows and do not require a `GROUP BY` clause. Some traditional aggregate functions have 
been enhanced to also work as window functions.



## Example

The following query computes on the fly the column `total_deposits_to_date` &mdash; that doesn't exist in the table &mdash;
by adding up the `amount` column according to a subgroup and ordering.

| account_id | deposit_date | amount | *total_deposits_to_date* |
| -- | -- | -- | -- |
| 51 | 2022-10-05 | 100 | 100 |
| 51 | 2022-10-25 | 50 | 150 |
| 64 | 2022-10-02 | 20 | 20 |
| 64 | 2022-10-15 | 40 | 60 |
| 64 | 2022-10-17 | 70 | 130 |


```java
DepositTable d = DepositDAO.newTable("d");

List<Map<String, Object>> rows = this.sql
    .select(d.accountId, d.depositDate, d.amount,
      sql.sum(d.amount).over().partitionBy(d.accountId).orderBy(d.depositDate.asc()).end().as("total_deposits_to_date")
    )
    .from(d) 
    .execute();
```

The resulting query is:

```sql
SELECT d.account_id, d.deposit_date, d.amount,
  SUM(d.amount) OVER(PARTITION BY d.account_id ORDER BY d.deposit_date) AS total_deposits_to_date
FROM deposit d
```


## Types of Window Functions

Window functions can be divided in three groups: enhanced, analytical, and positional analytical functions.


### Enhanced Window Functions

These are the traditional [Aggregate Functions](./aggregate-functions.md), now enhanced to work as window functions:

| Aggregate Function | In LiveSQL |
| -- | -- |
| `SUM(<expression>) OVER(...)` | `sql.sum(<expression>).over()...` |
| `MIN(<expression>) OVER(...)` | `sql.min(<expression>).over()...` |
| `MAX(<expression>) OVER(...)` | `sql.max(<expression>).over()...` |
| `AVG(<expression>) OVER(...)` | `sql.avg(<expression>).over()...` |
| `GROUP_CONCAT(<expression>, <sep>, <ordering>) OVER(...)` | `sql.groupConcat(<expression>, <sep>, <ordering>).over()...` |


### Analytical Functions

These functions rank rows according to their values and produce a result in a different domain of values.

| Aggregate Function | In LiveSQL |
| -- | -- |
| `ROW_NUMBER() OVER(...)` | `sql.rowNumber().over()...` |
| `RANK(<expression>) OVER(...)` | `sql.rank(<expression>).over()...` |
| `DENSE_RANK(<expression>) OVER(...)` | `sql.denseRank(<expression>).over()...` |
| `NTILE(<expression>) OVER(...)` | `sql.ntile(<expression>).over()...` |


### Positional Analytical Functions

Positional analytical functions peek at other rows of the table, view or result set to retrieve a value. They
can be useful to find changes in values over time or to differentiate different groups of data according to custom
logic.

| Aggregate Function | In LiveSQL |
| -- | -- |
| `LEAD(<expression>, <offset>, <default>) OVER(...)` | `sql.lead(<expression>, <offset>, <default>).over()...` |
| `LAG(<expression>, <offset>, <default>) OVER(...)` | `sql.lead(<expression>, <offset>, <default>).over()...` |


## Window Frames and Window Exclusions

Window frames further restrict the rows the window operates on.



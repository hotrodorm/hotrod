# Window Functions

The SQL Standard SQL:2003 defined window functions. Window functions do not consolidate rows, but only compute
values from multiple rows and do not require a `GROUP BY` clause. Some traditional aggregate functions have 
been enhanced to also work as window functions.


## Example

The following query computes on the fly the column `total_deposits_to_date` &mdash; that doesn't exist in the table &mdash;
by adding up the `amount` column according to a subgroup and ordering.

| account_id | deposit_date | amount | *total_deposits_to_date* |
| :--: | :--: | --: | --: |
| 51 | 2022-10-05 | 100 | 100 |
| 51 | 2022-10-25 | 50 | 150 |
| 64 | 2022-10-02 | 20 | 20 |
| 64 | 2022-10-15 | 40 | 60 |
| 64 | 2022-10-17 | 70 | 130 |


```java
DepositTable d = DepositDAO.newTable("d");

List<Map<String, Object>> rows = this.sql
    .select(d.accountId, d.depositDate, d.amount,
      sql.sum(d.amount).over().partitionBy(d.accountId).orderBy(d.depositDate.asc()).end()
        .as("total_deposits_to_date")
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

Window frames change the default set of rows a window function operates on. Window frames are an advanced feature
and different database engines implement them to different degrees.

First, the *subgroup* the window frame can see is delimited by the `PARTITION BY` clause. If not present, then the
subgroup includes all the rows of the result set.

Second, window frames segregate rows using three strategies: by rows, by ranges, and by groups. The rows strategy
is the simplest to understand, and the other ones can be explained based on this one.

### Frame Based on Rows

By default a frame includes all rows between the begining of the subgroup until the current row of the subgroup. Both
borders can be changed specifying the initial row (or range, or group) and the ending row. In LiveSQL a frame based 
on rows is defined by adding `.rows()` in the window function.

For example, if a query needs to compute a value aggregating withing a 3 rows before and 2 rows after, then it's frame 
needs to be set as:

```java
.over().partitionBy(...).orderBy(...).rows().betweenPreceding(3).andFollowing(2).end()
```

This is different from the default frame:

```java
.over().partitionBy(...).orderBy(...).rows().betweenUnboundedPreceding().andCurrentRow().end()
```

that can be just typed as:

```java
.over().partitionBy(...).orderBy(...).end()
```

The lower bound of the frame can be set as:

| LiveSQL | Description |
| -- | -- |
| `.unboundedPreceding()` | Starts at the beginning of the subgroup (default). Upper bound cannot be customized |
| `.preceding(<offset>)` | Starts at preceding `<offset>` rows. Upper bound cannot be customized |
| `.currentRow()` | Starts at the current row. Upper bound cannot be customized |
| `.betweenUnboundedPreceding()` | Starts at the beginning of the subgroup (default). Upper bound can be customized |
| `.betweenCurrentRow()` | Starts at the current row |
| `.betweenPreceding(<offset>)` | Starts at preceding `<offset>` rows |
| `.betweenFollowing(<offset>)` | Starts at the following `<offset>` rows |

If the upper bound can be customized it can be set as:

| LiveSQL | Description |
| -- | -- |
| `.andCurrentRow()` | Ends at the current row (default) |
| `.andUnboundedFollowing()` | Ends at the end of the subgroup |
| `.andPreceding(<offset>)` | End at the preceding `<offset>` rows |
| `.andFollowing(<offset>)` | End at the following `<offset>` rows |


### Frame Based on Ranges

A frame can also be based on a range of rows. The range does not limit by a number of rows, but by a difference in value
instead. For example, it can limit by values between -15.00 and +45.00. Ranges are only suitable for numeric and date-time 
data types, where arithmetic addition and subtraction are possible.

Their lower and upper bounds can be customized with a similar logic as shown above for rows.


### Frame Based on Groups

A frame can also be based on group of rows. The frame is limited by numbers of groups of values rather than a number of rows.

Their lower and upper bounds can be customized with a similar logic as shown above for rows.


### Frame Exclusions

Once the frame is set, rows can still be excluded using exclusion definitions. The following options can be used:

| LiveSQL | Description |
| -- | -- |
| `.excludeNoOthers()` | Do not exclude any rows (default) |
| `.excludeCurrentRow()` | Exclude the current row only |
| `.excludeTies()` | Exclude rows with ties |
| `.excludeGroup()` | Exclude rows in the same group |










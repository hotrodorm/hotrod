# The HAVING Clause

The `HAVING` clause filters out aggregated rows from the result set of a `SELECT` statement.

It's different from the `WHERE` clause. `WHERE` filters rows before they are aggregated, 
while `HAVING` filters rows after they are aggregated.

It has a single form where it accepts a boolean expression (aka *predicate*) as a parameter. This boolean
expression can be as simple or as complex as needed and it must evaluate to a boolean value. Expressions
included in this predicate must be aggregated expressions, since non-aggregated expressions are not 
available anymore in this SQL scope.


## Example

The following query selects all lottery games with added prizes over 1 million:


```java
LotteryPrizesTable lp = LotteryPrizesDAO.newTable();

List<Row> rows = this.sql
    .select(lp.game_id, sql.sum(prize))
    .from(lp) 
    .groupBy(lp.gameId)
    .having(sql.sum(prize).gt(1_000_000))
    .execute();
```

The resulting query is:

```sql
SELECT game_id, sum(prize)
FROM lottery_prizes
GROUP BY game_id
HAVING sum(prize) > 1000000
```

In this example the predicate in the `HAVING` clause is quite simple.

A predicate can be more complex to accomodate arithmetic, boolean logic, parenthesis, subqueries, etc. 
See [Expressions](./expressions.md) for more details and examples.


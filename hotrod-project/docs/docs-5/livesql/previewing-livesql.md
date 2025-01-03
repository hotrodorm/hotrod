# Previewing LiveSQL

When a LiveSQL statement is executed it is first rendered as a native SQL statement. Then, this native
SQL statement is sent to the database engine for its actual execution.

Notice that previewing a SQL statement is different from
[Enabling SQL Logging](../guides/enabling-sql-logging.md). When previewing, the application code is actively
retrieving the SQL form of a LiveSQL statement. Conversely, when activating the logging the engine
passively logs the SQL statement details and its execution details automatically with a few
configuration lines.

A LiveSQL statement can be previewed using the `getPreview()` method.

Previewing does have a marginal cost due to translation according to the
specific SQL dialect implemented in the database and/or version of it.


## Example

The following example previews a `SELECT` statement before executing it:

```java
DepositTable d = DepositDAO.newTable("d");

Select<Row> query = this.sql
    .select(d.balance)
    .from(d)
    .where(d.accountId.eq(10014874).and(d.status.ne("PENDING")))
    .orderBy(d.transactionDate.desc());

System.out.println(query.getPreview());

List<Row> rows = query.execute();
```

The code above displays the actual SQL statement being executed as:

```sql
SELECT d.balance
FROM deposit d
WHERE d.account_id = ? AND d.status <> ?
ORDER BY d.transaction_date DESC
```

The specific parameter values are also included at the end of the query preview (not shown above).


## Variations

There are three variations of the object that assembles the query. Each one produces a
different query assembler that can be used to preview the query:

| LiveSQL Type | Description | Previewing Object |
| -- | -- | -- |
| Generic SELECT | LiveSQL that runs SELECT queries | Select&lt;Row> q = sql.select()... |
| Entity SELECT | DAOs using the `.select(<t>, <predicate>)` method | EntitySelect&lt;EntityVO> q = dao.select()... |
| DML Query | LiveSQL that executes UPDATE, DELETE, and INSERT queries | DMLQuery q = sql.update()...<br/>DMLQuery q = sql.delete()...<br/>DMLQuery q = sql.insert()... |

All of these assembler objects (Select&lt;Row>, EntitySelect&lt;EntityVO>, and DMLQuery) offer the `.getPreview()` method that returns the actual SQL query that will be executed in the database.

The preview includes parameterized values (using `?`) with their corresponding values, as well
as literal values directly included in the query.
















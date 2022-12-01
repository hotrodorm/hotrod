# Previewing LiveSQL

When a LiveSQL statement is executed it is first rendered as a native SQL statement. Then, this native 
SQL statement is sent to the database engine for its actual execution.

Notice that previewing a SQL statement is different from 
[Enabling SQL Logging](../guides/enabling-sql-logging.md). When previewing, the application code is actively
retrieving the SQL form of a LiveSQL statement. When activating the logging the engine logs the SQL statement
details and its execution details automatically with a few configuration lines.

A LiveSQL statement can be previewed using the `getPreview()` method.

Rendering in SQL for previewing does have a marginal cost due to translation according to the specific SQL 
dialect implemented in the database and/or version of it. Previewing a SQL statement should be done with this cost in mind
for high volume LiveSQL statements.

## Example

The following example previews a `SELECT` statement before executing it:

```java
DepositTable d = DepositDAO.newTable("d");

ExecutableSelect<Map<String, Object>> query = this.sql
    .select(d.balance)
    .from(d) 
    .where(d.accountId.eq(10014874).and(d.status.ne("PENDING")))
    .orderBy(d.transactionDate.desc());

System.out.println(query.getPreview());

List<Map<String, Object>> rows = query.execute();
```

The code above displays the actual SQL statement being executed as:

```sql
SELECT d.balance
FROM deposit d
WHERE d.account_id = 10014874 AND d.status = 'PENDING'
ORDER BY d.transaction_date DESC
```


# Previewing LiveSQL

When a LiveSQL statement is executed it is first rendered as a native SQL statement. Then, this native 
SQL statement is sent to the database engine for its actual execution.

For debugging purposes this native SQL statement can be previewed using the `getPreview()` method of LiveSQL.

Rendering in SQL for previewing does have a marginal cost due to translation according to the specific
database version features. Previewing a SQL statement should be done with this cost in mind
for high volume LiveSQL statements.

## Example

The following example previews a `SELECT` statement before executing it:

```java
DepositTable d = DepositDAO.newTable("d");

ExecutableSelect<Map<String, Object>> query = this.sql
    .select(d.balance)
    .from(d) 
    .where(d.accountId.eq(10014874).and(d.status.ne("PENDING")))
    .orderBy(d.transactionDate.desc())
    .execute();

System.out.println(query.getPreview());

List<Map<String, Object>> rows = query.execute();
```

This code will display the resulting SQL statement as:

```sql
SELECT d.balance
FROM deposit d
WHERE d.account_id = 10014874 AND d.status = 'PENDING'
ORDER BY d.transaction_date DESC
```


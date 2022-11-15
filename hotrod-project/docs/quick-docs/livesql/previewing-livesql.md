# Previewing LiveSQL

When executed a LiveSQL statement is first assembled into native SQL statement and then is sent 
to the database engine for execution.

For debugging purposes this native SQL statement can be previewed using the `getPreview()` method of LiveSQL.

Rendering in SQL for previewing does have a marginal cost, so this functionality
should be used with this cost in mind for high volume LiveSQL statements.

## Example

The following example previews a `SELECT` statement before executing it:

```java
DepositTable d = DepositDAO.newTable("d");

ExecutableSelect<Map<String, Object>> select = this.sql
    .select(d.balance)
    .from(d) 
    .where(d.accountId.eq(10014874).and(d.status.ne("PENDING")))
    .orderBy(d.transactionDate.desc())
    .execute();

System.out.println(select.getPreview());

List<Map<String, Object>> rows = ps.execute();
```

This code will display the resulting SQL statement as:

```sql
SELECT d.balance
FROM deposit d
WHERE d.account_id = 10014874 AND d.status = 'PENDING'
ORDER BY d.transaction_date DESC
```


# The FOR UPDATE Clause

The `FOR UPDATE` clause obtains a FOR UPDATE lock on all the selected rows. 

Any other SQL statement that tries to obtain a FOR UPDATE lock on one of these rows will wait (or timeout)
for the lock to be released. The lock is automatically released when the transaction ends, either with
a commit or a rollback.

Other SELECT statements that don't request a lock will be able to select these rows normally, when ran
in the default isolation level. Higher isolation levels may also force the concurrent SELECTs to wait for
the rows to be released by the query that is holding them.

FOR UPDATE is available for LiveSQL's SELECT statements, and for CRUD's Select By Criteria method.
 

## Example

The following method debits the amount of $1000 from an account, only if the account balance is at least $1500:

```java
@Transactional
public void debit(Integer accountId) {

  AccountTable a = AccountDAO.newTable("a");

  List<Account> rows = this.accountDAO
    .select(a, a.id.eq(accountId))
    .forUpdate()
    .execute();
    
  if (rows.empty()) throw new RuntimeException("Account not found");
  if (rows.get(0).getBalance() < 1500) throw new RuntimeException("Insufficient funds");

  sql.update(a)
     .set(a.balance, a.balance.minus(1000))
     .execute();
}
```

The `forUpdate()` clause ensures the selected row is not modified before it's updated.


## Performance Impact

Locking rows serves as a strategy to perform consistent date read or updates in critical code sections.
As such, locking should be used sparely, only when needed, and for very short period of times. Keeping
a lock for a longer time will harm database concurrency and could degrade the performance of your application.

In short, it's advised to get the lock(s), quickly perform the required changes, and then end the transaction
(commit/roll back) to release the lock(s).

Keep in mind that locking many rows may also negatively affect the database performance. Most of the time
a simple strategy is to lock the main row(s) for a transaction &mdash; the entry point of data &mdash; instead
of locking all possible rows affected by it. This way just a minimal number of locks are required with 
the same effect as locking the entire set of data rows.

In most database a lock requires a write-to-disk operation, even for SELECTs. This necessarily is more
resource intensive than a simple SELECT.

The UPDATE, DELETE, and INSERT statements demarcated by a transaction will automatically acquire locks 
that can compete between them, and also with the FOR UPDATE statement.

Finally, even considering the performance drawbacks stated above, using a good strategy can dramatically
reduce the performance impact of locking, and will bring all the benefits that a critical section of code
requires.


## Supported Databases

The following table shows which databases do support the FOR UPDATE clause. The specific SQL syntax may vary, but not the functionality of it.

| Database   | FOR UPDATE |
| ---------- | :--------: |  
| Oracle     | Yes        |
| DB2        | Yes        |
| PostgreSQL | Yes        |
| SQL Server | Yes        |
| MySQL      | Yes        |
| MariaDB    | Yes        |
| Sybase ASE | --         |
| H2         | Yes        |
| HyperSQL   | --         |
| Derby      | --         | 

Other extra features related to lockinsg such as FOR SHARE, WAIT, SKIP LOCKED, and table/column narrowing are
not implemented in the FOR UPDATE clause, since they represent more exotic uses of locking and don't add 
value to normal usage of it.

*Note*: Because of the internals of the SQL Server engine, some version of this database may lock entire data pages
rather than single rows. Use locks with caution in this database.

 

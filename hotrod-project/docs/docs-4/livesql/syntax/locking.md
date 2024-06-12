# Pessimistic Locking

The SQL Standard defines clauses to obtain row locks when selecting data from tables. While there
are many variations, the most used ones are FOR UPDATE and FOR SHARE.

The FOR UPDATE and FOR SHARE clauses are used to implement the Pessimistic Locking strategy in the database
where rows are locked to ensure their values are not changed by other concurrent sessions until the end of the 
transaction.

Alternatively, it's also possible to use [Optimistic Locking](../optimistic-locking.md) 
in LiveSQL, a strategy that actually avoids the use of locks and could reach higher concurrency.

FOR UPDATE and FOR SHARE are available in LiveSQL's SELECT statements and also in CRUD's Select By Criteria methods.


## Locks Mechanics

While a lock has been acquired (in one or more rows), any other SQL statement that tries to obtain a 
lock on one of these rows will wait for the lock to be released, or will eventually timeout. Locks are 
automatically released when transactions end, either with commits or rollbacks.

**Note**: Locks only affect other queries that declare locking. Other vanilla SELECT statements that don't 
declare locks will be able to select these rows normally, when ran in the default (typically READ COMMITTED) 
isolation level. Higher isolation levels may also force the concurrent SELECTs to wait for
the rows to be released by the query that is holding them.


## CRUD Example

This example implements Pessimistic Locking on the table ACCOUNT defined as:

```sql
create table account (
  id int primary key,
  acc_num varchar(16),
  balance int
);
```

The following method debits the amount of $1000 from account #6704, only if the account balance
is at least $1500 and validations in other tables are successful:

```java
@Transactional
public void debit() {
  
  AccountTable a = AccountDAO.newTable("a");
  
  Account account = this.accountDAO
    .select(a, a.id.eq(6704))
    .forUpdate() // Obtains a lock in the selected row(s)
    .executeOne();
  
  if (account == null) throw new RuntimeException("Account not found");
  if (account.getBalance() < 1500) throw new RuntimeException("Insufficient funds");
  
  // perform validations in other tables...
  
  sql.update(a)
     .set(a.balance, a.balance.minus(1000))
     .where(a.id.eq(6704));
     .execute();
}
```

The FOR UPDATE clause ensures the selected row is not modified before it's updated. Note that the
method is annotated with `@Transactional`; this ensures the lock is kept between the execution of the
SELECT statement and the execution of the UPDATE statement.


## LiveSQL Example

In a similar way, a LiveSQL query can lock rows as well. For example:

```sql
  List<Row> rows = sql
    .select()
    .from(i)
    .where(i.amount.ge(500))
    .orderBy(i.orderDate.desc())
    .limit(5)
    .forUpdate()
    .skipLocked()
    .execute();
```

If the query joins multiple tables, typically only rows of the first one are locked. This, however, is
database-dependent; consult the specific database documentation to find out if one or more tables could
be locked simultaneously.

## Locking Modes

HotRod implements two locking modes: FOR UPDATE and FOR SHARE.

- FOR SHARE (applied using `.forShare()`) obtains locks for UPDATE and DELETE operations on the data, while allowing other FOR SHARE selects to run concurrently.
- FOR UPDATE (applied using `.forUpdate()`) obtains locks for SELECT, UPDATE and DELETE operations on the data. FOR UPDATE 
are fully exclusive.

The locking modes supported in each database are:

| Database   | FOR UPDATE | FOR SHARE |
| ---------- |:----------:|:---------:|  
| Oracle     | Yes*1      | --        |
| DB2        | Yes        | --        |
| PostgreSQL | Yes        | Yes       |
| SQL Server | Yes*2      | --        |
| MySQL      | Yes        | Yes       |
| MariaDB    | Yes        | Yes       |
| Sybase ASE | -- *3      | --        |
| H2         | Yes        | --        |
| HyperSQL   | -- *3      | --        |
| Derby      | -- *3      | --        |

*1 The SQL syntax in Oracle prevent the query to combine FETCH NEXT n ROWS ONLY with FOR UPDATE/FOR SHARE. There's a workaround to combine them, later on in this document. 
*2 SQL Server locks "pages of rows" and not specific rows. A FOR UPDATE (`with (updlock)`) could become inefficient in high concurrency situations.
*3 Locking is not available in plain SQL, but only when using cursors and/or in stored procedures.


## Locking Concurrency Options

Both locking modes can be further tailored by specifying how to handle concurrency situations, when the rows of interest are already 
locked by other sessions. There are three main cases:

- NOWAIT: When this clause is specified the SELECT query will generate an error if the rows of interest are already locked by other sessions.
- WAIT <n>: When a wait time (in seconds) is specified the SELECT query will wait for the specified time to obtain the lock and return the rows. 
If it times out the query will generate an error.
- SKIP LOCKED: The query will try to select the rows of interest but will skip any rows already locked by another sessions. This option can
be particularly efficient to implement queuing using tables.

The concurrency options supported in each database are:

| Database   | NOWAIT | WAIT &lt;seconds> | SKIP LOCKED |
| ---------- | ------- | ---------- | ----------- |  
| Oracle     | Yes     | Yes        | Yes         |
| DB2        | --      | --         | --          |
| PostgreSQL | Yes     | --         | Yes         |
| SQL Server | --      | --         | Yes         |
| MySQL      | Yes     | --         | Yes         |
| MariaDB    | Yes     | Yes        | Yes         |
| Sybase ASE | --      | --         | --          |
| H2         | Yes     | Yes        | Yes         |
| HyperSQL   | --      | --         | --          |
| Derby      | --      | --         | --          |


## Combining Locking with Other Clauses

The FOR UPDATE clause always need a FROM clause that references a table, or in some cases updatable views.
It can be combined with the WHERE, ORDER BY, OFFSET, and LIMIT clauses.

FOR UPDATE is not available to queries that use:

- DISTINCT
- GROUP BY
- HAVING
- UNION [ALL], INTERSECT [ALL], EXCEPT [ALL]

Generally speaking, any of these clauses above prevent the use of FOR UPDATE since they do not keep a 1:1
relationship between the source rows from a table and resulting rows of the query.

The following query combines all the other clauses. Consider each expression or filtering condition can be
as simple (as shown here) or as complex as needed:

```java
  InvoiceTable i = InvoiceDAO.newTable("i");

  List<Row> rows = sql
    .select()
    .from(i)
    .where(i.clientId.eq(1547))
    .orderBy(i.purchaseDate.desc())
    .offset(60)
    .limit(20)
    .forUpdate()
    .execute();
```

### Oracle Syntax Issue

Unfortunately the Oracle SQL Syntax doesn't allow all combinations of SQL clauses when using locking 
as in other databases such as PostgreSQL, H2, MySQL, etc. This issue affects particularly queries that need to 
combine limiting rows (e.g. `FETCH NEXT x ROWS ONLY`) with locking (`FOR UPDATE`).

The issue seems to be caused by Oracle creating an internal view to implement `FETCH NEXT x ROWS ONLY`. 
Nevertheless, there is a -- rather ugly -- workaround. For example, the *invalid* Oracle query:

```sql
SELECT *
FROM invoice
WHERE amount >= 500
ORDER BY order_date DESC
FETCH NEXT 1 ROWS ONLY
FOR UPDATE SKIP LOCKED;
```

Can be rephrased as a *valid* Oracle query as:

```sql
select *
from invoice
where id in (
  SELECT id
  FROM invoice
  WHERE amount >= 500
    ORDER BY order_date DESC
  FETCH NEXT 1 ROWS ONLY
)
FOR UPDATE SKIP LOCKED;
```

**Note**: It's not clear if this solution could return zero rows during high database load due to race
conditions. It's advised to recheck with a simple non-locking count of rows, if the workaround returns 
zero rows... just to be on the safe side.


## Performance Considerations

Locking rows serves as a strategy to perform consistent data read and updates in critical code sections.
As such, locking should be used sparely, only when needed, and for very short period of times. Keeping
a lock for a longer time will harm database concurrency and could degrade the performance of your application.

In short, it's advised to get the lock(s), quickly perform the required changes, and then end the transaction
(commit/roll back) to release the lock(s).

Keep in mind that locking many rows may also negatively affect the database performance. Most of the time
a simple strategy is to lock the main row(s) for a transaction &mdash; the entry point of data &mdash; instead
of locking all possible rows affected by it. This way if all application changes follow the same strategy,
just a minimal number of locks are required with the same effect as locking the entire set of data rows.

All the rows selected by the filtering predicate (the WHERE clause) are locked even if the application
processes only a subset of them. This means the search criteria must be as narrow as possible to avoid
consuming an unnecessary amount of database resources. 

In most databases a lock requires a write-to-disk operation, even for SELECTs. This necessarily is more
resource intensive than a simple SELECT.

The UPDATE, DELETE, and INSERT statements demarcated by a transaction will automatically acquire locks 
that can compete between them, and also with the SELECT FOR UPDATE statements.

Finally, even considering the performance drawbacks stated above, using a good strategy can dramatically
reduce the performance impact of locking, and will bring all the benefits that a critical section of code
requires.


## Benefits

Compared to Optimistic Locking, Pessimistic Locking is far easier to code. The example above is much shorter
and simpler to write and to debug than the corresponding code using Optimistic Locking.

Once the rows are selected and the lock(s) are acquired Pessimistic Locking ensures the transaction can be
completed. Well, as long as there's not a catastrophic database failure. This is not true for Optimistic
Locking.


## Weaknesses

There are three main weaknesses to Pessimistic Locking:

- For once, it's typically more resource intensive in the database, compared to Optimistic Locking, and 
that could reduce application concurrency or performance.
- Second, for higher concurrency it may become impossible to obtain a lock on one or more rows, and this
would lead to SELECT ... FOR UPDATE timeouts, that the application will need to deal with gracefully.
- Also, some databases may not implement row-level locking at all, or could implement it in a less
desirable way. For example, Sybase ASE or HyperSQL do not implement row-level locking; SQL Server, on
the other hand, does implement it but it ends up locking entire disk pages instead of single rows,
locking much more data than intended, and that could lead to higher chances of SELECT timeouts.


**Notes**:

- Other extra features related to locking such as FOR KEY SHARE, FOR KEY UPDATE, and table/column narrowing are
not currently implemented.
- Because of the internals of the SQL Server engine, some versions of this database may lock entire data pages
rather than single rows. Use locks with caution in this database.

 

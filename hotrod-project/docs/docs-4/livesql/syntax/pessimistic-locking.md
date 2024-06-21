# Pessimistic Locking

The SQL Standard defines clauses to obtain row-level locks when selecting data from tables. While there
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

```java
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

LiveSQL implements two locking modes: FOR UPDATE and FOR SHARE.

- FOR UPDATE obtains exclusive locks in the selected data so doesn't change until the end of the transaction and ensures it can be updated
- FOR SHARE obtains locks in the selected data so it doesn't change until the end of the transaction

The following table compares the locking modes:

| Feature    | FOR UPDATE | FOR SHARE |
| ---------- | ---------- | --------- |
| Use when | you want the data to stay the same for the duration of this operation and you expect **to make changes** to it | you want the data to stay the same for the duration of this operation and you **won't make changes** to it |
| Locks last until | the end of the transaction | the end of the transaction |
| Affects queries in other sessions | UPDATE, DELETE, and SELECT | UPDATE, DELETE |
| Plain queries with no locking in other sessions | will run normally | will run normally |
| Queries in other sessions trying to use FOR SHARE | will wait until this transaction completes, and then will get the locks | will succeed immediately and will also obtain similar locks |
| Queries in other sessions trying to use FOR UPDATE | will wait until this transaction completes, and then will get the locks | will wait until this transaction completes, and then will get the locks |


The locking modes supported by each database are:

| Database   | FOR UPDATE | FOR SHARE |
| ---------- |:----------:|:---------:|
| Oracle     | Yes *1     | --        |
| DB2        | Yes        | --        |
| PostgreSQL | Yes        | Yes       |
| SQL Server | Yes *2     | --        |
| MySQL      | Yes        | Yes       |
| MariaDB    | Yes        | Yes       |
| Sybase ASE | -- *3      | --        |
| H2         | Yes        | --        |
| HyperSQL   | -- *3      | --        |
| Derby      | -- *3      | --        |

*1 The Oracle SQL syntax does not allow a query to combine FETCH NEXT n ROWS ONLY with FOR UPDATE/FOR SHARE. There's a workaround to combine them, shown later on in this document.

*2 SQL Server locks "pages of rows" and not specific rows. A FOR UPDATE (`with (updlock)`) could become inefficient in high concurrency situations.

*3 Locking is not available in plain SQL queries, but only when using cursors and/or in stored procedures.


## Locking Concurrency

Both locking modes can be further tailored by specifying how to handle concurrency situations, when the rows of interest are already 
locked by other sessions. There are three main cases:

- **NOWAIT**: When this clause is specified the SELECT query will generate an error if the rows of interest are already locked by other sessions.
- **WAIT &lt;seconds>**: When a wait time (in seconds) is specified the SELECT query will wait for the specified time to obtain the lock and return the rows. 
If it times out the query will generate an error.
- **SKIP LOCKED**: The query will try to select the rows of interest but will skip any rows already locked by another sessions. This option can
be particularly efficient to implement queuing using tables.

The concurrency options supported by each database are:

| Database   | NOWAIT  | WAIT &lt;seconds> | SKIP LOCKED |
| ---------- |:-------:|:----------:|:-----------:|
| Oracle     | Yes     | Yes        | Yes         |
| DB2        | --      | --         | --          |
| PostgreSQL | Yes     | --         | Yes         |
| SQL Server | --      | --         | Yes         |
| MySQL      | Yes     | --         | Yes         |
| MariaDB    | Yes     | Yes        | Yes         |
| Sybase ASE | --      | --         | --          |
| H2         | Yes *1  | Yes *1     | Yes *1      |
| HyperSQL   | --      | --         | --          |
| Derby      | --      | --         | --          |

*1 Available since version 2.2.220.


## Combining Locking with Other Clauses

The FOR UPDATE and FOR SHARE clauses always need a FROM clause that references a table
or updatable views. It can be combined with the WHERE, ORDER BY, OFFSET, 
and LIMIT clauses.

The FOR UPDATE and FOR SHARE clauses are not available for queries that use:

- DISTINCT
- GROUP BY
- HAVING
- UNION [ALL], INTERSECT [ALL], EXCEPT [ALL]

Generally speaking, any of these clauses above prevent the use of FOR UPDATE or FOR SHARE since
they do not produce a 1:1 relationship between the source rows from a table and resulting
rows of the query.

The following query combines all the other clauses. Consider each expression can be
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
as in other databases such as PostgreSQL, H2, MySQL, MariaDB, etc. This issue affects particularly queries that need to 
combine limiting rows (e.g. `FETCH NEXT x ROWS ONLY`) with locking (`FOR UPDATE`).

The issue seems to be caused by Oracle creating an internal view to implement `FETCH NEXT x ROWS ONLY`. 
Nevertheless, there is a *rather ugly* workaround.

For example, the *invalid* Oracle query:

```sql
SELECT *
FROM invoice
WHERE amount >= 500
ORDER BY order_date DESC
FETCH NEXT 1 ROWS ONLY
FOR UPDATE SKIP LOCKED;
```

Can be rephrased as a *valid* Oracle query as shown below:

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

In LiveSQL/CRUD this query can be phrased as:

```java
  InvoiceTable i = InvoiceDAO.newTable();
  InvoiceTable j = InvoiceDAO.newTable();

  List<Invoice> = invoiceDAO
    .select(i, i.id.in(sql.select(j.id).from(j).where(j.amount.ge(500)).orderBy(j.orderDate.desc()).limit(1)))
    .forUpdate()
    .skipLocked()
    .execute();
```

**Note**: It's not clear if this solution could return zero rows during high database load due to race
conditions. It's advised to consider a recheck with a simple non-locking count of rows, if the 
workaround returns zero rows.


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

Pessimistic Locking can have the following benefits:

- Compared to Optimistic Locking, Pessimistic Locking is far easier to code. The example above is much shorter
and simpler to write and to debug than the corresponding code using Optimistic Locking. Compare the
simplicity of the [CRUD Example with Pessimistic Locking](#crud-example) against the verbosity of the
[CRUD Example with Optimistic Locking](../optimistic-locking.md#crud-example); in the latter
coding the `update()` method is far more error-prone.  

- Once the rows are selected and the lock(s) are acquired Pessimistic Locking will have all the resources
to complete the transaction. This is not true for Optimistic Locking; in this strategy the work
may fail when updating data, something that happens typically at the end of the transaction.


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

- Other locking modes such as FOR KEY SHARE, FOR KEY UPDATE, and table/column narrowing are
not currently implemented.
- Because of the internals of the SQL Server engine, some versions of this database may lock entire data pages
rather than single rows. Use locks with caution in this database.

 

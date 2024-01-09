# Optimistic Locking in LiveSQL

Optimistic Locking is a strategy to read database rows and then modify them, while
checking that no other concurrent database session has changed them in the meantime.

While the alternative strategy of [Pessimistic Locking](syntax/for-update.md) prevents
data changes by actually acquiring locks on the database rows, the Optimistic Locking
avoids these locks, by checking the data has not been modified during the update phase of the change.


## Strategies

Optimistic Locking does not require any changes while reading data from the database. 

All three strategies read rows of data from the database, and then later try to make updates
of these rows while checking there have been no changes to the data in the meantime.
That is, the checks are performed at the end, while updating the new values into the database.

There are three common strategies to implement Optimistic Locking, described below.


### Strategy #1 &mdash; Version Number

This strategy considers the use of a version number column while updating. The table must be fitted
with an extra column to store a version number that is incremented on every row update.
While updating, this version number is checked against the value that was previously read:
If they are equal then the UPDATE succeeds, otherwise it fails.

The downside of this strategy is that it requires an extra *artificial* column in the table that probably
is not included in the database design, and that could potentially require changing all related
processes once added.

It's benefits are that is lightweight, fully reliable if implemented correctly, and also very lightweight
for the application and for the database.


### Strategy #2 &mdash; Checking the Entire Row

This strategy checks that no changes have been made to the entire row while updating it by re-checking 
all values of it for changes. While the row is being updated, each and every column values is compared
against the values previously read: if no change is detected the UPDATE succeeds; if any change is
detected the UPDATE fails.

The downside of this strategy is that it requires to read and check the entire row while updating,
and not just the data that is of interest. Particularly, if the table has many columns, or it has
data-intensive columns this could prove resource intensive for the database and also would 
require a lot of network bandwidth to send data back and forth between the database and 
application. The correct comparison of nullable columns can also be tricky and error-prone for
rookie developers.

The upside is that a table can be used "as is" and no extra artificial column 
needs to be added to it.


### Strategy #3 &mdash; Checking a TIMESTAMP Column

This strategy makes use of an existing TIMESTAMP column that keeps the last updated time for each row.
While updating a row, the value of this column is compared to the value that was previously read: if they
are exactly equal the UPDATE succeeds, otherwise it fails.

The obvious downside of this strategy is that the table may not have a TIMESTAMP. Also, even if it 
exists this solution may not be adequate for high concurrency applications. Two or more concurrent
session could potentially update values using the exact same TIMESTAMP value, potentially leading
to loss of data.

The upside, is that there's no need to add any extra column to the database if a TIMESTAMP can
be used for this purpose. This strategy can be a good solution for applications with
low-to-medium expected concurrency.


## Example

The following example will use strategy #1, with an extra column called `row_version`:

```sql
create table bank_account (
  id int primary key,
  acc_num varchar(16),
  balance int,
  row_version int
);
```

The application will first read the row for an account (e.g. 6704):

```java
  BankAccountTable a = BankAccountDAO.newTable("a");
  BankAccount account = sql.select().from(a).where(a.id.eq(6704)).executeOne();
  if (account == null) throw new RuntimeException("Account not found");
```

The application will then compute some new values according to some business logic (e.g. add $100 to the balance):

```java
  int newBalance = account.getBalance() + 100;
```

Finally, the application will try to update the row:

```java
  int count = sql.update(a)
                 .set(a.balance, newBalance)
                 .set(a.rowVersion, a.rowVersion.plus(1))
                 .where(a.id.eq(6704).and(a.rowVersion.eq(account.getRowVersion())));
  if (count == 0) throw new RuntimeException("Could not update account -- concurrent changes detected.");
```

The `count` tell us how many rows were updated, and that tells us if the row was untouched or had any changes in it.
If the count is 1 then the row had no changes and the UPDATE was successful; if the count was zero, then the
row suffered some changes in the meantime, and the UPDATE was not successful.


## Conclusion

Optimistic locking can successfully update rows according to the business logic and detect if data has been
modified while it's being used.

The example above demonstrates how to use Optimistic Locking in LiveSQL using Strategy #1 "Version Number".
The other two strategies are implemented in a similar way, all of them detecting changes by counting how
many rows were updated. In all cases the comparison of values must be done in the database and not in the
application, to make sure the count is accurate.

Optimistic Locking may fail more often when there's high number of concurrent sessions. At the same time
Optimistic Locking is faster than acquiring locks so the possibility of changes happening between reads
and updates is reduced.

In any case, the locking strategy of the alternative Pessimistic Locking can also fail, specially under stress
when too many concurrent sessions try to lock a row, and these are not released timely; in these cases fails
will be due to locking timeouts.

When detecting Optimistic Locking update failures, the application will need to deal with that case gracefully. 
Maybe pointing out the issue with the end user and ask to verify the new values and to resubmit the change.

Finally, Optimistic Locking can be quite advantageous for high concurrency, since it avoid acquiring database locks
in the database.
 





 
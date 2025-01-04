# The `<optimistic-locking>` Tag

This tag can be added to a table to enable Optimistic Locking in it. It must indicate
which strategy is desired to implement optimistic locking in the table.

Optimistic Locking affects the UPDATE by primary key and DELETE by primary key operations.
Selecting data will retrieve all the information of a row or rows, and this data
will be used to control concurrency according to the selected strategy.

## Attributes

It includes the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| strategy | The strategy to implement optimistic locking in this table. One of: `version-number`, `timestamp`, or `full-row-check`. | Required |
| column | - In case the version-number strategy is selected this is the numeric column used as a version number. Must be an integer-like type<br/>- In case the timestamp strategy is selected this is the TIMESTAMP column used to verify unchanged rows<br/>- In case the full-row-check strategy is selected this  attribute should not be specified | Required in some cases |


## Goal

Optimistic locking can be used to control concurrent updates on a table. These update operation
can take the form of an `UPDATE` or `DELETE` SQL statement that follows a `SELECT` SQL statement.

When updating or deleting the row HotRod will craft the SQL statement to validate the row hasn't
been changed since reading it. If intact, the operation will succeed. If changed, a runtime
exception will be thrown with the aim of aborting the transaction. The business logic may choose
to catch this exeption and try again; it would need to read the row again while doing so, to make retrieve the updated values.


## Optimistic Locking Strategies

HotRod currently supports three optimistic locking strategies. They all throw a `StaleDataException`
during UPDATE and DELETE queries if they detect a row was modified or deleted after it was read, possibly by another concurrent session.

### Version Number

This strategy uses a version number column to store a version for the row data. Every time the
row is updated this version is incremented. This way an UPDATE or DELETE operation on the row
can detect if it was changed after it was read from the database.

This version requires the table to allocate an extra column just for this purpose and, thus, will
most likely need a table strcuture change to implement it.

### Timestamp

This strategy uses a TIMESTAMP column to store the date and time when the row was last updated.
Every time the row is updated this timestamp is updated. This way an UPDATE or DELETE operation
on the row can detect if it was changed after it was read from the database.

One disadvantage of this strategy is that it may changes the original purpose of the column that
may have been intended for a different intention (not low level updates). Another drawback is that
this strategy may fail to detect the change if the frequency of modification is high compared to
the granularity of the column (3 seconds, 1 millisecond, 1 microsecond, and so on).

This strategy is best suited for cases when the table already has a timestamp column that can be
used for this purpose and when the database table cannot be easily changed.

### Full Row Check

This strategy detects concurrent row changes while updating or deleting a row by checking all the
values of the row for changes.

The benefit of this strategy is that is doesn't require an extra column to be added to the table
at all. The downside is that it needs to compare all the columns of the table before updating, and this
could place extra load in the database or be outright impossible to achieve. This extra load could
be significant when comparing LOB types or other heavy values that need to be sent back and forth
over the network and to be fully compared before performing UPDATE or DELETE operations.

## Comparison of Strategies

The following table sohws the features and drawbacks of each strategy:

| Features                   | Version Number | Timestamp | Full Row Check |
| -- |:--:|:--:|:--:|
| Always Detect Row Changes  | :heavy_check_mark: | :x: *1 | :heavy_check_mark: |
| Can always be implemented  | :heavy_check_mark: | :heavy_check_mark: | :x: *2 |
| Always highly performant   | :heavy_check_mark: | :heavy_check_mark: | :x: *3 |
| No extra network usage     | :heavy_check_mark: | :heavy_check_mark: | :x: *4 |
| No table structure changes | :x: | :heavy_check_mark: | :heavy_check_mark: |

*1 May fail to detect row changes if the timestamp column granularity is to coarse or if the transaction frequency is too high.

*2 Cannot be implemented if the table has data types that do not support equality comparisons (e.g. RAW, LONGBLOB, file-based types, etc).

*3 The performance may suffer if the database has to compare a lot of data (e.g. LOB columns) each time a row is updated.

*4 May consume extra network bandwith to send the row back each time it's updated.

## Examples

The following examples implement different optimistic locking strategies in the same table ACCOUNT, that originally has four columns:

```sql
CREATE TABLE account (
  id INT PRIMARY KEY NOT NULL,
  acc_num VARCHAR(20) NOT NULL,
  balance INT NOT NULL
  updated_at TIMESTAMP NOT NULL
);
```

### Example 1 - Version Number Strategy

To use the Version Number strategy the table ACCOUNT needs to have an extra column to store the
version number of the row. In this example, this column will have the name `row_version`, as shown below:

```sql
CREATE TABLE account (
  id INT PRIMARY KEY NOT NULL,
  acc_num VARCHAR(20) NOT NULL,
  balance INT NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  row_version INT NOT NULL -- we need to add an extra column
);
```

When specifying the table in the configuration file the `<optimistic-locking>` tag is used to
declare the column, as in:

```xml
<hotrod>

  <table name="account">
    <optimistic-locking strategy="version-number" column="row_version" />
  </table>

</hotrod>
```

Then, we can use the DAO as we normally do to SELECT, INSERT, UPDATE, and DELETE rows. Consider
that UPDATE and DELETE can now throw `StaleDataException` in case the row being updated or
deleted had changed (or was removed).

```java
  Account a = this.accountDAO.select(102501);
  a.setBalance(a.getBalance() + 100);
  this.accountDAO.update(a); // can throw StaleDataException
```

### Example 2 - Timestamp Strategy

To use the Timestamp strategy the table ACCOUNT needs to have a TIMESTAMP column to store the last time it was updated; our ACCOUNT table already has one. If it didn't have one, we would need to add a column for this purpose; otherwise we could not use this strategy.

In this case there's no need to change the table:

```sql
CREATE TABLE account (
  id INT PRIMARY KEY NOT NULL,
  acc_num VARCHAR(20) NOT NULL,
  balance INT NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
```

Now, we need to declare the optimistic locking strategy in the configuration file, as in:

```xml
<hotrod>

  <table name="account">
    <optimistic-locking strategy="timestamp" column="updated_at" />
  </table>

</hotrod>
```

Again, we can use the DAO as we normally do to SELECT, INSERT, UPDATE, and DELETE rows. Consider
that UPDATE and DELETE can now throw `StaleDataException` in case the row being updated or
deleted had changed (or was removed). There's no change from the application code perspective:

```java
  Account a = this.accountDAO.select(102501);
  a.setBalance(a.getBalance() + 100);
  this.accountDAO.update(a); // can throw StaleDataException
```

### Example 3 - Full Row Check Strategy

To use the Full Row Check strategy the table ACCOUNT needs to have columns that are comparable
to themselves. All typical data types (numbers, chars, dates, timestamps, etc.) adhere to this requirement; only a handful of exotic data types may not be practically comparable; e.g. BLOBs
or similar.

This strategy doesn't require any extra change to the table. It remains as is:

```sql
CREATE TABLE account (
  id INT PRIMARY KEY NOT NULL,
  acc_num VARCHAR(20) NOT NULL,
  balance INT NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
```

We declare the optimistic locking strategy in the configuration file, as in:

```xml
<hotrod>

  <table name="account">
    <optimistic-locking strategy="full-row-check" />
  </table>

</hotrod>
```

In the same way as before, we can use the DAO as we normally do to SELECT, INSERT, UPDATE,
and DELETE rows. Consider that UPDATE and DELETE can now throw `StaleDataException` in case the
row being updated or deleted had changed (or was removed). There's no change from the application
code perspective:

```java
  Account a = this.accountDAO.select(102501);
  a.setBalance(a.getBalance() + 100);
  this.accountDAO.update(a); // can throw StaleDataException
```

























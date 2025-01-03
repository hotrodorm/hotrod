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
| strategy | The strategy to implement optimistic locking in this table. One of: `version-number`, `timestamp`, or `full-row-check`. | N/A |
| column | - In case the version-number strategy is selected this is the numeric column used as a version number. Must be an integer-like type<br/>- In case the timestamp strategy is selected this is the TIMESTAMP column used to verify unchanged rows<br/>- In case the full-row-check strategy is selected this  attribute should not be specified | N/A |
| value | The SQL expression that produces a new value when for the TIMESTAMP column. This attribute is required the the timestamp strategy is selected | N/A |


## Goal

Optimistic locking can be used to control concurrent updates on a table. These update operation
can take the form of an `UPDATE` or `DELETE` SQL statement that follows a `SELECT` SQL statement.

When updating or deleting the row HotRod will craft the SQL statement to validate the row hasn't
been changed since reading it. If intact, the operation will succeed. If changed, a runtime
exception will be thrown with the aim of aborting the transaction. The business logic may choose
to catch this exeption and try again; it would need to read the row again while doing so, to make retrieve the updated values.


## Optimistic Locking Strategies

HotRod currently supports three optimistic locking strategies:

- **Version Number**: A column of the table is dedicated to store a version number for the row. Every
time the row is updated the version number changes. `UPDATE` and `DELETE` operations can detect changes in this value and abort the ongoing transaction on such event.
- **Timestamp Column**: If a timestamp column happens to be already included in the table
it can be used for optimistic locking. Each time the row is changed this timestamp is updated. The upside
of this strategy is that no extra column is required. The downside of it is that the
column may have been intended for a different goal (not low level updates), and also the granularity of the timestamp
value could be insufficient on highly updated rows that can occur more than once a second (or milliseconds, microseconds, nanoseconds).
- **Full Row Check**: This strategy checks the entire row for changes while updating it or deleting it. The benefit of this strategy is that is doesn't require an extra column to be added to the table.
The downside is that it needs to compare all the columns of the table before updating, and this
could place extra load in the database. This extra load could be significant when comparing LOB
types or other heavy values that need to be sent back and forth over the network and then
fully compared before performing update operations.

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
    <optimistic-locking strategy="timestamp" column="updated_at" value="current_timestamp" />
  </table>

</hotrod>
```

**Note**: the `current_timestamp` SQL expression is a valid expression that generates a TIMESTAMP with the current date and time in the H2 database. Change accordingly for the specific database you are using.

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

























# General Purpose Queries using &lt;query>

A General Purpose Query does not return a result set and is implemented using `<query>` tag. It may return optional count of rows.

Typically these correspond to `UPDATE`, `DELETE` SQL statements, but can actually include any valid SQL statement in the specific database such as `INSERT`, `CREATE`, `ALTER`, etc.

Each `<query>` tag must included in a `<table>`, `<view>`, or `<dao>` tag. The corresponding DAO class exposes the `<query>` as a Java method.

They are commonly used to perform changes in the database &mdash; by running tailored `UPDATE` or `DELETE` statements &mdash; but can actually 
run any valid SQL statement, including DML statements and stored procedures calls.

For example, a typical DML query could look like:

```xml
<query method="closeFullyPaidInvoices">
  update invoice
  set outstanding = 0
  where amount_paid >= amount_receivable
</query>
```
Nevertheless, any valid query can be used. For example:

```xml
<query method="prepareDailyTransactions">
  truncate daily_transactions_tbl
</query>
```

The above two queries return the number of affected rows and are exposed to the developer as Java methods:

```java
public int closeFullyPaidInvoices() { ... }

public int prepareDailyTransactions() { ... }
```

They can include the full native SQL language available on the database, such as hints and any non-standard features.


## Parameters

Parameters can be also be added by using the &lt;parameter> tag, paired with the `#{x}` parameter insertions points to enhance the query.

**Note**: General queries do no require the use of the `<complement>` tag, at all.

For example:

```xml
<query method="closeClientsPaidInvoices">
  <parameter name="clientId" java-type="Integer" />
  update invoice
  set outstanding = 0
  where amount_paid >= amount_receivable
    and client_id = #{clientId}
</query>
```

The above query return the number of updated rows and produces the following Java method:

```java
public int closeClientsPaidInvoices(Integer clientId) { ... }
```

See [Query Parameters](nitro-parameters.md) for more details on parameters.

## Dynamic SQL

General pupose queries can be enhanced with Dynamic SQL. Dynamic allows the query to include or exclude fragments of the SQL statement at runtime, based on the values of the supplied parameters.

For example:

```xml
<query method="removeDraftArticles">
  <parameter name="authorId" java-type="Integer" />
  delete from article
  where status = 'draft'
  <where>
    <if test="authorId != null">and author_id = #{authorId}</if>
  </where>
</query>
```
 
The above query return the number of deleted rows and produces the following Java method:

```java
public int removeDraftArticles(Integer authorId) { ... }
```

Now, the actually executed SQL statement takes different shape depending on the runtime parameter(s):

- If the value of the `authorId` is null the SQL statement will be run as:

```sql
delete from article
where status = 'draft'
```

- Otherwise, If the value of the `authorId` is not null, the SQL statement will be run as:

```sql
delete from article
where status = 'draft'
  and author_id = #{authorId}
```





# Selecting Without a FROM Clause

Most databases allow SELECT queries without a FROM clause. For example, in MySQL you can run the
following queries:

```sql
select 3 * 7;
select concat('Hello', ' ', 'World'), 1 + 1, curdate(), date_add(curdate(), interval 1 day) as tomorrow;
```

Queries like the above can compute scalar values or retrieve system information that is unrelated
to any table.

The exceptions are Oracle, DB2, and Apache Derby databases that do require a `FROM` clause in each SELECT statement. 
To perform simple queries without a bona fide table Oracle includes the `DUAL` table and DB2 and Derby 
include the `SYSIBM.SYSDUMMY1` table. Both of them are read-only tables and have a single column and a single row that
is perfect for these purposes.

In Oracle and DB2 the first query can take the form:

```sql
select 3 * 7 from dual; -- Oracle
select 3 * 7 from sysibm.sysdummy1; -- DB2
select 3 * 7 from sysibm.sysdummy1; -- Apache Derby
```

Nevertheless, when using Oracle, DB2, or Derby, LiveSQL automatically adds a `FROM` clause when a SELECT query doesn't include it.

For example the query:

```java
List<Row> rows = sql.select(sql.val(3).mult(7)).execute();
```

Is perfectly valid in all supported databases, such as MySQL, PostgreSQL and also for Oracle, DB2, and Derby. For the last 
three databases LiveSQL adds the FROM clause behind the scenes.

In any case, if you prefer to clearly include the `FROM dual` clause (or `FROM sysibm.sysdummy1` clause), LiveSQL accepts
it. The query above can be written as:

```java
List<Row> rows = sql.select(sql.val(3).mult(7)).from(sql.DUAL).execute(); // Oracle
List<Row> rows = sql.select(sql.val(3).mult(7)).from(sql.SYSDUMMY1).execute(); // DB2
List<Row> rows = sql.select(sql.val(3).mult(7)).from(sql.SYSDUMMY1).execute(); // Apache Derby
```

# The DUAL &amp; SYSDUMMY1 Tables

Most databases don't need a `FROM` clause in a SELECT query. For example, in MySQL you can run the
following queries:

```sql
select 3 * 7;
select concat('Hello', ' ', 'World'), 1 + 1, curdate(), date_add(curdate(), interval 1 day) as tomorrow;
```

Queries like the above can compute scalar values or retrieve system information that is unrelated
to any table.

However, the Oracle and DB2 databases do require a `FROM` clause in each SELECT statement. To perform simple
queries without a bona fide table Oracle includes the `DUAL` table and DB2 includes the 
`SYSIBM.SYSDUMMY1` table. Both of them are read-only tables and have a single column and a single row.

In Oracle and DB2 the first query can take the form:

```sql
select 3 * 7 from dual; -- Oracle
select 3 * 7 from sysibm.sysdummy1; -- DB2
```

In LiveSQL these tables can be used as:

```java
List<Row> rows = sql.select(sql.val(3).mult(7)).from(sql.DUAL).execute(); // Oracle
List<Row> rows = sql.select(sql.val(3).mult(7)).from(sql.SYSDUMMY1).execute(); // DB2
```


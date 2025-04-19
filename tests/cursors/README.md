# SELECT Buffering

This is to define how a SELECT query can be read using buffering in the supported databases, in the context of using LiveSQL's *cursor* functionality.

```java
  PreparedStatement ps = conn.prepareStatement(SQL, ResultSet.TYPE_FORWARD_ONLY,
    ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
```

## Oracle - Success

Prepare the row generation function:

```sql
create or replace type numbers_t as table of number;

create function generate_series (minnumber integer, maxnumber integer)
   return numbers_t
   pipelined
   deterministic
is
begin
   for i in minnumber .. maxnumber loop
      pipe row (i);
   end loop;
   return;
end;
/
```
Test query:

```sql
select column_value as id, rpad('a', 1000, substr(column_value, -1) || 'a') as name
from table(generate_series(1, 4000000))
```

Success:

- It seems like Oracle always does buffering, irrespectively of the transaction or fetch size.


## DB2 LUW - Success

Test query:

```sql
with digit(d) as (values 0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
s as (
  select a.d + 10 * b.d + 100 * c.d + 1000 * d.d + 10000 * e.d as id
  from digit a cross join digit b cross join digit c cross join digit d cross join digit e
)
select id, repeat('a' || right(id, 1), 1000) as name from s
```

Success:

- It seems like DB2 LUW always does buffering, irrespectively of the transaction or fetch size.

## PostgreSQL - Success

Test query:

```sql
select x, repeat('a' || right('' || t.x, 1), 1000) as name from generate_series(1, 1000000) t (x)
```

Success with:

```java
    conn.setAutoCommit(false);
    ps.setFetchSize(50);
```

## SQL Server - Success

Test query:

```sql
with digit as (
  select 0 as d union all select 1 union all select 2 union all select 3 union all select 4
  union all select 5 union all select 6 union all select 7 union all select 8 union all select 9        
),
s as (
  select a.d + 10 * b.d + 100 * c.d + 1000 * d.d + 10000 * e.d as id
  from digit a cross join digit b cross join digit c cross join digit d cross join digit e
)
select id, replicate(concat('a', right(id, 1)), 1000) as title from s;
```

Success:

- It seems like SQL Server always does buffering, irrespectively of the transaction or fetch size.

## MySQL - Success

Test query:

```sql
with digit as (
  select 0 as d union all select 1 union all select 2 union all select 3 union all select 4
  union all select 5 union all select 6 union all select 7 union all select 8 union all select 9        
),
s as (
  select a.d + 10 * b.d + 100 * c.d + 1000 * d.d + 10000 * e.d as id
  from digit a cross join digit b cross join digit c cross join digit d cross join digit e
)
select id, repeat(concat('a', right(id, 1)), 1000) as title from s;
```

Success with:

```java
   // The transaction is irrelevant for buffering
   ps.setFetchSize(Integer.MIN_VALUE); // Use this exact value
```

## MariaDB - Success

Test query:

```sql
with digit as (
  select 0 as d union all select 1 union all select 2 union all select 3 union all select 4
  union all select 5 union all select 6 union all select 7 union all select 8 union all select 9        
),
s as (
  select a.d + 10 * b.d + 100 * c.d + 1000 * d.d + 10000 * e.d as id
  from digit a cross join digit b cross join digit c cross join digit d cross join digit e
)
select id, repeat(concat('a', right(id, 1)), 1000) as title from s;
```

Success with:

```java
   // The transaction is irrelevant for buffering.
   ps.setFetchSize(50);
     // A positive (> 0) value enables buffering.
     // 0 means no buffering.
     // Not setting the fetch size means no buffering as well.
```

## Sybase ASE

## H2 - Fail

Test query:

```sql
select t.x as id, repeat('a' || right(t.x, 1), 1000) as name from system_range(1, 1000000) t
```

Tried but couldn't conclude:

```java
    conn.setAutoCommit(false);
    ps.setFetchSize(50);
```

## HyperSQL

## Derby


FOR UPDATE/ FOR SHARE cannot be used is any of the below clauses are used (pg):

Allowed in:

- SELECT
- FROM/JOIN
- WHERE
- ORDER BY
- OFFSET
- LIMIT

Oracle:

- DISTINCT, CURSOR, SET Operators, aggregations

PG:

- DISTINCT                    
- GROUP BY
- HAVING
- WINDOW
- UNION/INTERSECT/EXCEPT

| Database   | FOR UPDATE | FOR SHARE | NOWAIT | WAIT &lt;seconds> | SKIP LOCKED | OF &lt;tables> | OF &lt;columns> |
| ---------- | ---------- | --------- | ------- | ---------- | ----------- | ---------------- | ----------------- |  
| Oracle     | Yes        | --        | Yes     | Yes        | Yes         | --               | Yes               |
| DB2        | Yes        | --        | --      | --         | --          | --               | Yes               |
| PostgreSQL | Yes        | Yes       | Yes     | --         | Yes         | Yes              | --                |
| SQL Server | Yes*1      | --        | --      | --         | Yes         | Yes              | --                |
| MySQL      | Yes        | Yes       | Yes     | --         | Yes         | Yes              | --                |
| MariaDB    | Yes        | Yes       | Yes     | Yes        | Yes         | --               | --                |
| Sybase ASE | -- *2      | --        | --      | --         | --          | --               | --                |
| H2         | Yes        | --        | Yes     | Yes        | Yes         | --               | --                |
| HyperSQL   | -- *3      | --        | --      | --         | --          | --               | --                |
| Derby      | -- *4      | --        | --      | --         | --          | --               | Yes               |

*1 SQL Server locks "pages of rows" not specific rows. A FOR UPDATE (`with (updlock)`) could become inefficient in high concurrency situations.
*2 Locking is only available for cursors or in stored procedures.
*3 FOR UPDATE is only available in HyperSQL when using cursors.
*4 FOR UPDATE is only available in Apache Derby when using cursors.


## Oracle

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10);
insert into x (a, b) values (2, 30);
insert into x (a, b) values (3, 30);
insert into x (a, b) values (4, 40);
insert into x (a, b) values (5, 50);

select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

**Note**: Limiting rows and locking at the same time is not possible in a direct way as in other databases (PostgreSQL, H2, MySQL, etc.) since Oracle
creates an internal view to implement `FETCH NEXT x ROWS ONLY`. There is a -- rather ugly -- workaround. The query:

```sql
SELECT a.*
FROM invoice a
WHERE a.amount >= 500
ORDER BY a.order_date DESC
FETCH NEXT 1 ROWS ONLY
FOR UPDATE SKIP LOCKED;
```

Can be rephrased as:

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

It's not clear if this solution could return zero rows during high database load due to race conditions. It's advised to recheck with a simple non-locking count
of rows, if the workardoun returns zero rows... just to be on the safe side.


## DB2

```sql
-- No need for BEGIN TRANSACTION
select * from x where a >= 3 for update of b with rs;
update x set b = b + 1 where a = 4;
commit;
```

Notes:
- `WITH RS`: "Read Stability" is needed to ensure proper locking of the row while is being used by the session.
- `OF <list-of-cols>`: The list of columns must be unqualified and always belong to the first table in the FROM clause.

## PostgreSQL

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

begin transaction;
select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

## SQL Server

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

select * from x with (updlock) where a >= 3;
update x set b = b + 1 where a = 4;
commit;
```

Notes:
- SQL Server locks "pages of rows", not specific rows. A FOR UPDATE (`with (updlock)`) could become highly innefficient in
a multi-threaded environment.

## MySQL

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

## MariaDB

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

## Sybase ASE

N/A

Notes:
- FOR UPDATE is only available as part of the declaration of a cursor or inside a stored procedure. It's not available for simple queries. 

## H2

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

Notes:
- The default timeout to wait for a lock is very low (1000 ms). This can be changed at instance startup with a parameter or with SQL (FOR UPDATE WAIT <seconds>) in each query execution.


## HyperSQL

N/A

## Derby

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

Notes:
- In Derby a SELECT FOR UPDATE does not lock the SELECT, but subsequent changing queries like UPDATE, DELETE.

### Test

```sql
create table x (a int, b int);
insert into x (a, b) values (1, 10), (2, 20), (3, 30), (4, 40), (5, 50);

begin transaction;
select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;

begin transaction;
select * from x where a >= 3 for update;
update x set b = b + 1 where a = 4;
commit;
```

# Update with Join

The UPDATE statement can take four main different forms. Each database -- particularly high end ones includes several extra variations for very specific cases, that are not included here:

## Main Variations

### 1. SQL-92 UPDATE

This can be a simple UPDATE from SQL-92 with or without subqueries. This is supported by all databases. For example:

```sql
UPDATE t SET a = 1, b = 2 WHERE c > 10;

UPDATE t SET a = 1, b = (select d from u where u.id = t.id) WHERE c > 10;
```

### 2. Update with Join

This is a form that joins one or more tables with the table being updated, and values can
be set from the joined tables to update the main one. For example:

```sql
UPDATE t
SET t.a = u.e + v.f, t.b = u.d
FROM u -- some dbs require the first joined table to use FROM
LEFT JOIN v on ...
WHERE u.id = t.id
  AND t.c > 10;

UPDATE t
SET t.a = u.e + v.f, t.b = u.d
JOIN u -- other dbs accept FROM or [LEFT|RIGHT] JOIN
LEFT JOIN v on ...
WHERE u.id = t.id
  AND t.c > 10;
```

### 3. Update with Subqueries

This is the most advanced form and can use complex subqueries to retrieve the related values.
Unlike the second form, this form requires the subquery to have a max cardinalty of 1 for each
row of the table being updated (quite nice!). For example:

```sql
update invoice i
set (tax_rule_name, tax_law, tax_pct) = (
  select name, law, pct
  from tax_rule r
  where r.id = i.tax_rule_id
)
where branch_id = 10;
```

### 4. Update with CTE

Common Table Expression can be combined into UPDATE statements in some databases. For example, in PostgreSQL:

```sql
with
y as (
  select x.id as iid, x.tax_rule_id, r.*
  from invoice x
  left join tax_rule r on r.id = x.tax_rule_id
)
update invoice i
set tax_rule_name = y.name, tax_law = y.law, tax_pct = y.pct
from y
where y.iid = i.id and i.branch_id = 10;
```



## Summary

The following table indicates since which versions each database implements an update form:

| Database   | Form #1<br/>SQL-92  | Form #2<br/>Joins | Form #3<br/>Subqueries | Form #4<br/>CTE |
| ---        |:--------:|:-------:|:-------:|:-------:|
| Oracle     | Yes      | 23c     | 11g2*   | --      |
| DB2 LUW    | Yes      | 11.1    | 10.5*   | --      |
| PostgreSQL | Yes      | 9.3*    | 9.5     | 9.3*    |
| SQL Server | Yes      | 2014*   | --      | 2014*   |
| MySQL      | Yes      | 5.5*    | --      | 8.0     |
| MariaDB    | Yes      | 10.0*   | --      | --      |
| Sybase ASE | Yes      | --      | --      | --      |
| H2         | Yes      | --      | --      | --      |
| HyperSQL   | Yes      | --      | --      | --      |
| Derby      | Yes      | --      | --      | --      |

*Supported in this version or older.


Form #2 implementation details differ as shown below:

| Database   | Allowed | Table Expr Allowed | Aliased Table | JOIN type | JOIN Pred | Multiple matches      | No Matches   |
| ---        | ---     | --                 | --            | ---       | ---       | --------------------- | ------------ |
| Oracle     | Yes     | Yes              | --            | FROM, then [LEFT] JOINs | WHERE | Not Allowed           | Not modified* |
| DB2        | Yes     | Yes                | --            | FROM (using commas)     | WHERE | Not Allowed           | Not modified* |
| PostgreSQL | Yes     | Yes                | --            | FROM (using commas)     | WHERE | Allowed, Unpredicable | Not modified* |
| SQL Server | Yes     | Yes                | Yes           | FROM, then [LEFT] JOINs | JOIN  | Allowed, Unpredicable | JOIN: Not modified, LEFT JOIN: Sets Null |
| MySQL      | Yes     | Yes                | --            | In UPDATE, [LEFT] JOINs | JOIN  | Allowed, Unpredicable | JOIN: Not modified, LEFT JOIN: Sets Null |
| MariaDB    | Yes     | Yes                | --            | In UPDATE, [LEFT] JOINs | JOIN  | Allowed, Unpredicable | JOIN: Not modified, LEFT JOIN: Sets Null |
| Sybase ASE | --      | --                 | --            | --        | --        | --                    | --           |
| H2         | --      | --                 | --            | --        | --        | --                    | --           |
| HyperSQL   | --      | --                 | --            | --        | --        | --                    | --           |
| Derby      | --      | --                 | --            | --        | --        | --                    | --           |

*Updates using a LEFT JOIN (that is, setting nulls) can be done performing the outer join inside a table expression. See examples in DB2 and PostgreSQL. I don't know if the optimizer pushes the filter down -- i.e. if this form is or not efficient. I consider this solution a rather clunky workaround, and it seems much simpler and optimal to use form #3 for DB2 and PostgreSQL.

Form #3 implementatiom matrix:

| Database   | Allowed  | Multiple Matches | No Matches |
| ---        | ---      | ---------------- | ---------- |
| Oracle     | Yes      | Not Allowed      | Set Nulls  |
| DB2        | Yes      | Not Allowed      | Set Nulls  |
| PostgreSQL | Yes      | Not Allowed      | Set Nulls  |
| SQL Server | --       | --               | --         |
| MySQL      | --       | --               | --         |
| MariaDB    | --       | --               | --         |
| Sybase ASE | --       | --               | --         |
| H2         | --       | --               | --         |
| HyperSQL   | --       | --               | --         |
| Derby      | --       | --               | --         |

Form #4 implementatiom matrix:

| Database   | Allowed  |
| ---        | ---      |
| Oracle     | --       |
| DB2        | --       |
| PostgreSQL | Yes      |
| SQL Server | Yes      |
| MySQL      | Yes      |
| MariaDB    | --       |
| Sybase ASE | --       |
| H2         | --       |
| HyperSQL   | --       |
| Derby      | --       |



## Oracle

```sql
create table invoice (
  id number(6),
  amount number(6),
  branch_id number(6),
  tax_rule_id number(6),
  tax_rule_name varchar2(25),
  tax_law varchar2(15),
  tax_pct decimal(6, 2)
);

insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (1, 100, 10, 201, -1, 'pending', -1);
insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (2, 101, 20, 201, -1, 'pending', -1);
insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (3, 102, 10, 202, -1, 'pending', -1);
insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (4, 103, 10, 203, -1, 'pending', -1);

create table tax_rule (
  id number(6) primary key not null,
  name varchar2(25),
  law varchar2(15),
  pct decimal(6, 2)
);

insert into tax_rule (id, name, law, pct) values
  (201, 'Rule 201', 'Law #501', 5.25);
insert into tax_rule (id, name, law, pct) values
  (202, 'Rule 202', 'Law #502', 8.15);
```

### Oracle Form #2

```sql
update invoice i
set tax_rule_name = r.name, tax_law = r.law, tax_percent = r.percent
from (select * from tax_rule) r
where r.id = i.tax_rule_id
  and branch_id = 10;
```

Notes:

- Multiple matches are **not allowed** per updated row.
- Update rows with no matches are **not modified**.

### Oracle Form #3 (https://dbfiddle.uk/qfRzqY5k)

```sql
update invoice i
set (tax_rule_name, tax_law, tax_percent) = (
  select name, law, percent
  from tax_rule r
  where r.id = i.tax_rule_id
)
where branch_id = 10;
```

Notes:

- Multiple matches are **not allowed** per updated row.
- Update rows with no matches are **set to null**.

### Oracle Form #4 -- Not Supported

## DB2

```sql
create table invoice (
  id int,
  amount int,
  branch_id int,
  tax_rule_id int,
  tax_rule_name varchar(25),
  tax_law varchar(15),
  tax_pct decimal(6, 2)
);

insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (1, 100, 10, 201, -1, 'pending', -1),
  (2, 101, 20, 201, -1, 'pending', -1),
  (3, 102, 10, 202, -1, 'pending', -1),
  (4, 103, 10, 203, -1, 'pending', -1);

create table tax_rule (
  id int primary key not null,
  name varchar(25),
  law varchar(15),
  pct decimal(6, 2)
);

insert into tax_rule (id, name, law, pct) values
  (201, 'Rule 201', 'Law #501', 5.25),
  (202, 'Rule 202', 'Law #502', 8.15);
```

### DB2 Form #2 (https://dbfiddle.uk/Pr68_hBT)

```sql
update invoice i
set tax_rule_name = r.name, tax_law = r.law, tax_pct = r.pct
from tax_rule r -- comma-separated list of tables
where r.id = i.tax_rule_id -- join predicates in the WHERE clause
  and branch_id = 10;
```

Notes:

- Multiple matches are **not allowed** per updated row.
- Update rows with no matches are **not modified**.

Updates using a LEFT JOIN can be done performing the outer join inside a table expression:

```sql
update invoice i
set tax_rule_name = y.name, tax_law = y.law, tax_pct = y.pct
from (
  select x.id as iid, x.tax_rule_id, r.*
  from invoice x
  left join tax_rule r on r.id = x.tax_rule_id
) y
where y.iid = i.id and i.branch_id = 10;
```

### DB2 Form #3 (https://dbfiddle.uk/_3y49QMu)

```sql
update invoice i
set (tax_rule_name, tax_law, tax_pct) = (
  select name, law, pct
  from tax_rule r
  where r.id = i.tax_rule_id
)
where branch_id = 10;
```

Notes:

- Multiple matches are **not allowed** per updated row.
- Update rows with no matches are **set to null**.

### DB2 Form #4 -- Not Supported

## PostgreSQL

```sql
create table invoice (
  id int,
  amount int,
  branch_id int,
  tax_rule_id int,
  tax_rule_name varchar(25),
  tax_law varchar(15),
  tax_pct decimal(6, 2)
);

insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (1, 100, 10, 201, -1, 'pending', -1),
  (2, 101, 20, 201, -1, 'pending', -1),
  (3, 102, 10, 202, -1, 'pending', -1),
  (4, 103, 10, 203, -1, 'pending', -1);

create table tax_rule (
  id int primary key not null,
  name varchar(25),
  law varchar(15),
  pct decimal(6, 2)
);

insert into tax_rule (id, name, law, pct) values
  (201, 'Rule 201', 'Law #501', 5.25),
  (202, 'Rule 202', 'Law #502', 8.15);
```

### PostgreSQL Form #2 (https://dbfiddle.uk/YfAoVLYE)

```sql
update invoice i
set tax_rule_name = r.name, tax_law = r.law, tax_pct = r.pct
from tax_rule r -- comma-separated list of tables
where r.id = i.tax_rule_id -- join predicates in the WHERE clause
  and branch_id = 10;
```

Notes:

- Multiple matches are **allowed** per updated row (with unpredictable results).
- Update rows with no matches are **not modified**.

Updates using a LEFT JOIN can be done performing the outer join inside a table expression:

```sql
update invoice i
set tax_rule_name = y.name, tax_law = y.law, tax_pct = y.pct
from (
  select x.id as iid, x.tax_rule_id, r.*
  from invoice x
  left join tax_rule r on r.id = x.tax_rule_id
) y
where y.iid = i.id and i.branch_id = 10;
```


### PostgreSQL Form #3 (https://dbfiddle.uk/PLiRRt3d)

```sql
update invoice i
set (tax_rule_name, tax_law, tax_pct) = (
  select name, law, pct
  from tax_rule r
  where r.id = i.tax_rule_id
)
where branch_id = 10;
```

Notes:

- Multiple matches are **not allowed** per updated row.
- Updated rows with no matches are **set to null**.

### PostgreSQL Form #4 (https://dbfiddle.uk/eiBBJHaF)

```sql
with
y as (
  select x.id as iid, x.tax_rule_id, r.*
  from invoice x
  left join tax_rule r on r.id = x.tax_rule_id
)
update invoice i
set tax_rule_name = y.name, tax_law = y.law, tax_percent = y.percent
from y
where y.iid = i.id and i.branch_id = 10;
```

## SQL Server

```sql
create table invoice (
  id int,
  amount int,
  branch_id int,
  tax_rule_id int,
  tax_rule_name varchar(25),
  tax_law varchar(15),
  tax_pct decimal(6, 2)
);

insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (1, 100, 10, 201, -1, 'pending', -1),
  (2, 101, 20, 201, -1, 'pending', -1),
  (3, 102, 10, 202, -1, 'pending', -1),
  (4, 103, 10, 203, -1, 'pending', -1);

create table tax_rule (
  id int primary key not null,
  name varchar(25),
  law varchar(15),
  pct decimal(6, 2)
);

insert into tax_rule (id, name, law, pct) values
  (201, 'Rule 201', 'Law #501', 5.25),
  (202, 'Rule 202', 'Law #502', 8.15);
```

### SQL Server Form #2 (https://dbfiddle.uk/uJHyUEFr)

```sql
update i -- only alias here
set tax_rule_name = r.name, tax_law = r.law, tax_pct = r.pct
from invoice i
join tax_rule r on r.id = i.tax_rule_id -- inner or left joins
where branch_id = 10;
```

Notes:

- Multiple matches are **allowed** per updated row (with unpredictable results).
- Update rows with no matches are **not modified** when using JOIN. Use LEFT JOIN to set nulls.

Updating joining the same table (https://dbfiddle.uk/lxfzijlv):

```sql
update widget
set ordering = x.rn * 10
from widget w
join (
  select t.*, row_number() over(order by ordering) as rn
  from widget t
) x on x.id = w.id;
```

### SQL Server Form #3 -- Not Supported


### SQL Server Form #4 (https://dbfiddle.uk/1HVF6UWo)

```sql
with r as (select * from tax_rule)
update i
set tax_rule_name = r.name, tax_law = r.law, tax_pct = r.pct
from invoice i
left join r on r.id = i.tax_rule_id
where branch_id = 10;
```

## MySQL


```sql
create table invoice (
  id int,
  amount int,
  branch_id int,
  tax_rule_id int,
  tax_rule_name varchar(25),
  tax_law varchar(15),
  tax_pct decimal(6, 2)
);

insert into invoice (id, amount, branch_id, tax_rule_id, tax_rule_name, tax_law, tax_pct) values
  (1, 100, 10, 201, -1, 'pending', 10),
  (2, 101, 20, 201, -1, 'pending', 12),
  (3, 102, 10, 202, -1, 'pending', 10),
  (4, 103, 10, 203, -1, 'pending', 19);

create table tax_rule (
  id int primary key not null,
  name varchar(25),
  law varchar(15),
  pct decimal(6, 2)
);

insert into tax_rule (id, name, law, pct) values
  (201, 'Rule 201', 'Law #501', 5.25),
  (202, 'Rule 202', 'Law #502', 8.15);
```

### MySQL Form #2 (https://dbfiddle.uk/hffmZdIh)

```sql
update invoice i
left join (select * from tax_rule) r on r.id = i.tax_rule_id
set i.tax_rule_name = r.name, i.tax_law = r.law, i.tax_pct = r.pct
where branch_id = 10;
```

Notes:

- Multiple matches are **allowed** per updated row (with unpredictable results).
- Update rows with no matches are **not modified** when using JOIN. Use LEFT JOIN to set nulls.

A non-correlated table expression can also be used. For example (https://dbfiddle.uk/QibCOGXu):

```sql
update invoice i,
  (select branch_id, avg(tax_pct) as atp from invoice group by branch_id) x
set tax_pct = x.atp
where x.branch_id = i.branch_id
```

Updating joining the same table (https://dbfiddle.uk/0RqFu1am):

```sql
update widget w
join (
  select t.*, row_number() over(order by ordering) as rn
  from widget t
) x on x.id = w.id
set w.ordering = x.rn * 10
```


### MySQL Form #3 -- Not Supported


### MySQL Form #4 (https://dbfiddle.uk/LfuZFgJK)

```sql
with r as (select * from tax_rule)
update invoice i
left join r on r.id = i.tax_rule_id
set i.tax_rule_name = r.name, i.tax_law = r.law, i.tax_pct = r.pct
where branch_id = 10;
```

## MariaDB (10.6)

Similar behavior as MySQL, except that Form #4 is not supported.


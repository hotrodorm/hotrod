# Update with Join

The UPDATE statement can take three main different forms. Each database -- particularly high end ones includes several extra
variations for very specific cases, that are not included here:

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

```sqlUPDATE t
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

```sqlupdate invoice i
set (tax_rule_name, tax_law, tax_pct) = (
  select name, law, pct
  from tax_rule r
  where r.id = i.tax_rule_id
)
where branch_id = 10;
```

## Summary

| Database   | Form 1 | Form 2    | Form 3   |
| --         | --     | --        | --       |
| Oracle     | Yes    | --        | Yes (SM) |
| DB2        | Yes    | Yes (SM)  | Yes (SM) |
| PostgreSQL | Yes    | Yes       | Yes (SM) |
| SQL Server | Yes    | Yes       | --       |
| MySQL      | Yes    | Yes       | --       |
| MariaDB    | Yes    | Yes       | --       |

SM: Single Match Enforced. If more than a single value is found for an update row an error is thrown.
 
 
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

Not Supported in Oracle.


### Oracle Form #3 (fiddle: https://dbfiddle.uk/qfRzqY5k)

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

### DB2 Form #2 (fiddle: https://dbfiddle.uk/Pr68_hBT)

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

### DB2 Form #3 (fiddle: https://dbfiddle.uk/_3y49QMu)

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

### PostgreSQL Form #2 (fiddle: https://dbfiddle.uk/YfAoVLYE)

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


### PostgreSQL Form #3 (fiddle: https://dbfiddle.uk/PLiRRt3d)

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

### SQL Server Form #2 (fiddle: https://dbfiddle.uk/uJHyUEFr)

```sql
update i
set tax_rule_name = r.name, tax_law = r.law, tax_pct = r.pct
from invoice i
join tax_rule r on r.id = i.tax_rule_id -- inner or left joins
where branch_id = 10;
```

Notes:
- Multiple matches are **allowed** per updated row (with unpredictable results).
- Update rows with no matches are **not modified**.


### SQL Server Form #3

Not supported.


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

### MySQL Form #2 (fiddle: https://dbfiddle.uk/hffmZdIh)

```sql
update invoice i, tax_rule r -- all table references
set i.tax_rule_name = r.name, i.tax_law = r.law, i.tax_pct = r.pct
where r.id = i.tax_rule_id
  and branch_id = 10;
```

Notes:
- Multiple matches are **allowed** per updated row (with unpredictable results).
- Update rows with no matches are **not modified**.


### MySQL Form #3

Not supported.


## MariaDB (10.6)

Same behavior as MySQL 8.


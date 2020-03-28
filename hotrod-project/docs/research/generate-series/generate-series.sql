-- The examples generate a table series from 2 to 4 (both included) and column named id:

-- === Oracle 12c ===
with
g(id) as (select 2 from dual union all select id + 1 from g where id < 4)
select id from g

-- === DB2 10.5 ===
with
g(id) as (select 2 from sysibm.sysdummy1 union all select id + 1 from g where id < 4)
select id from g

-- === PostgreSQL 9.x ===
select * from generate_series(2, 4) g(id);
-- or using a CTE:
with recursive 
g as (select 2 as id union all select id + 1 from g where id < 4)
select id from g;

-- === SQL Server 2014 ===
with
g as (select 2 as id union all select id + 1 from g where id < 4)
select id from g

-- === MariaDB 10.3 ===
with recursive
g as (select 2 as id union all select id + 1 from g where id < 4)
select id from g;

-- === MySQL 8.0 ===
with recursive
g as (select 2 as id union all select id + 1 from g where id < 4)
select id from g;

-- === Sybase ASE 16 ===
-- Not available as a row generator or a CTE.

-- === H2 1.4.197 ===
with
g(id) as (select 2 union all select id + 1 from g where id < 4)
select id from g;

-- === Apache Derby 10 ===
-- N/A

-- === Hyper SQL ===
with recursive g(id) as (values 2 union select id + 1 from g where id < 4)
select id from g
-- Note: HyperSQL has a hard coded recursion depth of 260. Therefore this method can only generate series of up to 260 values only.




Checking UNKNOWN:

Oracle:

with
t (a) as (
  select 0 from dual
  union all select 1 from dual
  union all select null from dual
)
select a, case when a = 1 then 'TRUE' when not (a = 1) then 'FALSE' else 'UNKNOWN' end as p from t

DB2:

with
t (a) as (
  select 0 from sysibm.sysdummy1
  union all select 1 from sysibm.sysdummy1
  union all select null from sysibm.sysdummy1
)
select a, case when a = 1 then 'TRUE' when not (a = 1) then 'FALSE' else 'UNKNOWN' end as p from t

PostgreSQL:

with
t (a) as (
  select 0
  union all select 1
  union all select null
)
select a, a = 1 from t;

SQL Server (does not work?)

with
t (a) as (
  select 0
  union all select 1
--  union all select null
)
select * from t;

MariaDB:

with
t (a) as (
  select 0
  union all select 1
  union all select null
)
select a, (a = 1) is unknown from t;

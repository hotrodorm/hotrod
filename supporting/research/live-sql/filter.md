# Filter

This is the aggregation FILTER clause.

## Oracle

Supported in Oracle 26 and newer:

```sql
with t as (
  select 3 as n from dual
  union all select 5 from dual
  union all select 7 from dual
)
select null,
  sum(n) filter(where n > 4) as total
from t
union all
select
  sum(n) filter(where n > 4) over(order by n) as partial,
  sum(n) filter(where n > 4) over(order by n desc) as partiald
from t
order by 1, 2;
```

Functions:

- MIN
- MAX
- AVG
- SUM
- COUNT

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

In Oracle 23ai or older:

```sql
with t as (
  select 3 as n from dual
  union all select 5 from dual
  union all select 7 from dual
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## DB2 LUW

In version 11.1, 11.5, and 12.1:

```sql
with t as (
  select 3 as n from sysibm.sysdummy1
  union all select 5 from sysibm.sysdummy1
  union all select 7 from sysibm.sysdummy1
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## PostgreSQL

Supported in PostgreSQL 9.4 and newer:

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(n) filter(where n > 4) as total
from t
union all
select
  sum(n) filter(where n > 4) over(order by n) as partial,
  sum(n) filter(where n > 4) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

In PostgreSQL 9.3 or older:

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## SQL Server


SQL Server does not support FILTER:

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## MySQL

MySQL does not support FILTER.

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## MariaDB

MariaDB does not support FILTER.

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## Sybase ASE

Sybase ASE does not support FILTER.

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(case when n > 4 then n end) as total
from t
union all
select
  sum(case when n > 4 then n end) over(order by n) as partial,
  sum(case when n > 4 then n end) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
5     12
12    7
null  12
null  12
```

## H2

H2 1.x and 2.x suports FILTER:

```sql
with t as (
  select 3 as n
  union all select 5
  union all select 7
)
select null,
  sum(n) filter(where n > 4) as total
from t
union all
select
  sum(n) filter(where n > 4) over(order by n) as partial,
  sum(n) filter(where n > 4) over(order by n desc) as partiald
from t
order by 1, 2;
```

Result:

```
NULL  TOTAL
----  -----
null  12
null  12
5     12
12    7
```
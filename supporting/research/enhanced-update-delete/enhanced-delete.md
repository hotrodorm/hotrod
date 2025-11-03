# Enhanced DELETE Syntax

Beyond the SQL-92 standard syntax, many databases have implemented enhanced forms for the DELETE
statement to make use of the inner and outer join functionalities between tables.

The DELETE statement can take four main different forms. Each database -- particularly high end ones includes several extra variations for very specific cases, that are not included here:

| Database   | Form #1<br/>SQL-92 | Form #2<br/>FROM | Form #3<br/>[LEFT] JOIN | Form #4<br/>Subquery | Form #5<br/>CTE |
| -- |:--:|:--:|:--:|:--:|:--:|
| Oracle     | Yes                | 23c               | --                 | 12c1                 | --              |
| DB2 LUW    | Yes                | --                | --                 | 10.5                 | --              |
| PostgreSQL | Yes                | 9.3*              | --                 | --                   | 9.3*            |
| SQL Server | Yes                | --                | 2014*              | --                   | --              |
| MySQL      | Yes**              | --                | 5.5*               | --                   | 8.0             |
| MariaDB    | Yes**              | --                | 10.0*              | --                   | --              |
| Sybase ASE | Yes                | 16*<br/>(no subqueries) | --                 | --                   | --              |
| H2         | Yes                | --                | --                 | --                   | --              |
| HyperSQL   | Yes                | --                | --                 | --                   | --              |
| Derby      | Yes                | --                | --                 | --                   | --              |

*This version or older.

*Supported as long as the table is not referenced in subqueries. If it needs
to be referenced, use Form #3.

***In form #3 a LEFT JOIN can be used to implement anti-join deletion (delete non-matching rows).


## Oracle

```sql
create table dealership (
  id number(6) primary key not null,
  main_type varchar2(10),
  name varchar2(10)
);

insert into dealership (id, main_type, name) values (1, 'VIP', 'Luxy');
insert into dealership (id, main_type, name) values (2, 'ORG', 'Maans');

create table vehicle (
  id number(6) primary key not null,
  value number(6),
  type varchar2(10),
  dealership_id number(6) references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values (10, 500, 'VIP', 1);
insert into vehicle (id, value, type, dealership_id) values (11, 200, 'ORG', 1);
insert into vehicle (id, value, type, dealership_id) values (12, 400, 'VIP', 1);
insert into vehicle (id, value, type, dealership_id) values (13, 600, 'ORG', 2);
insert into vehicle (id, value, type, dealership_id) values (14, 150, 'VIP', 2);
insert into vehicle (id, value, type, dealership_id) values (15, 800, 'ORG', 2);
insert into vehicle (id, value, type, dealership_id) values (16, 510, 'VIP', 1);
```

### Oracle Form #2 (https://dbfiddle.uk/rwIywNL2)

```sql
delete vehicle v
from ( -- first FROM, then JOINs
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x
where v.dealership_id = x.id
  and v.value < x.avg_value
  and v.type <> x.main_type;
```

### Oracle Form #3 -- Not Supported

### Oracle Form #4 (https://dbfiddle.uk/gURwZg0q and https://dbfiddle.uk/EpRafw9D)

```sql
delete
from (
  select * from vehicle v
  join (
    select d.id as did, avg(o.value) as avg_value, max(d.main_type) as main_type
    from dealership d
    join vehicle o on o.dealership_id = d.id
    group by d.id
  ) x on v.dealership_id = x.did
)
where value < avg_value
  and type <> main_type;
```

### Oracle Form #5 -- Not Supported


## DB2

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
);

insert into dealership (id, main_type, name) values
  (1, 'VIP', 'Luxy'), (2, 'ORG', 'Maans');

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values
  (10, 500, 'VIP', 1),
  (11, 200, 'ORG', 1),
  (12, 400, 'VIP', 1),
  (13, 600, 'ORG', 2),
  (14, 150, 'VIP', 2),
  (15, 800, 'ORG', 2),
  (16, 510, 'VIP', 1);
```

### DB2 Form #2 -- Not Supported

### DB2 Form #3 -- Not Supported

### DB2 Form #4 (https://dbfiddle.uk/iE3QgJoW)

Delete vehicles keeping only the cheapest one in each dealership:

```sql
delete from (
  select row_number() over(partition by dealership_id order by value) as rn
  from vehicle
)
where rn > 1
```

### DB2 Form #5 -- Not Supported


## PostgreSQL

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
);

insert into dealership (id, main_type, name) values
  (1, 'VIP', 'Luxy'), (2, 'ORG', 'Maans');

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values
  (10, 500, 'VIP', 1),
  (11, 200, 'ORG', 1),
  (12, 400, 'VIP', 1),
  (13, 600, 'ORG', 2),
  (14, 150, 'VIP', 2),
  (15, 800, 'ORG', 2),
  (16, 510, 'VIP', 1);
```

### PostgreSQL Form #2 (https://dbfiddle.uk/AdSbu4w1)

```sql
delete from vehicle v
using (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x
where v.dealership_id = x.id
  and v.value < x.avg_value
  and v.type <> x.main_type;
```

### PostgreSQL Form #3 -- Not Supported

### PostgreSQL Form #4 -- Not Supported

### PostgreSQL Form #5 (https://dbfiddle.uk/1k96wTrS)

```sql
with
x as (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
)
delete from vehicle v
using x
where v.dealership_id = x.id
  and v.value < x.avg_value
  and v.type <> x.main_type;
```

## SQL Server

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
);

insert into dealership (id, main_type, name) values
  (1, 'VIP', 'Luxy'), (2, 'ORG', 'Maans');

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values
  (10, 500, 'VIP', 1),
  (11, 200, 'ORG', 1),
  (12, 400, 'VIP', 1),
  (13, 600, 'ORG', 2),
  (14, 150, 'VIP', 2),
  (15, 800, 'ORG', 2),
  (16, 510, 'VIP', 1);
```

### SQL Server Form #2 -- Not Supported

### SQL Server Form #3 (https://dbfiddle.uk/hBMm_3fG)

```sql
delete v
from vehicle v
join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x on v.dealership_id = x.id and v.value < x.avg_value
where v.type <> x.main_type;
```

Delete with anti-join (delete the non-matching rows):

```sql
delete v
from vehicle v
left join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x on v.dealership_id = x.id and v.value < x.avg_value and v.type <> x.main_type
where x.id is null;
```

### SQL Server Form #4 -- Not Supported

### SQL Server Form #5 -- Not Supported

## MySQL

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
);

insert into dealership (id, main_type, name) values
  (1, 'VIP', 'Luxy'), (2, 'ORG', 'Maans');

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values
  (10, 500, 'VIP', 1),
  (11, 200, 'ORG', 1),
  (12, 400, 'VIP', 1),
  (13, 600, 'ORG', 2),
  (14, 150, 'VIP', 2),
  (15, 800, 'ORG', 2),
  (16, 510, 'VIP', 1);
```

### MySQL Form #1

Form #1 is supported as long as the table being deleted cannot be referenced in a subquery. If it needs
to be referenced, use Form #3.

### MySQL Form #2 -- Not Supported

### MySQL Form #3 (https://dbfiddle.uk/wsl_wGGH)

```sql
delete vehicle
from vehicle
join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x
where vehicle.dealership_id = x.id
  and vehicle.value < x.avg_value
  and vehicle.type <> x.main_type;
```
Delete with anti-join (delete the non-matching rows):

```sql
delete vehicle
from vehicle
left join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x on vehicle.dealership_id = x.id
   and vehicle.value < x.avg_value
where x.id is null
```

### MySQL Form #4 -- Not Supported

### MySQL Form #5 (https://dbfiddle.uk/IbeSNgQn)

```sql
with
x as (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
)
delete vehicle
from vehicle
join x
where vehicle.dealership_id = x.id
  and vehicle.value < x.avg_value
  and vehicle.type <> x.main_type;
```

## MariaDB

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
);

insert into dealership (id, main_type, name) values
  (1, 'VIP', 'Luxy'), (2, 'ORG', 'Maans');

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
);

insert into vehicle (id, value, type, dealership_id) values
  (10, 500, 'VIP', 1),
  (11, 200, 'ORG', 1),
  (12, 400, 'VIP', 1),
  (13, 600, 'ORG', 2),
  (14, 150, 'VIP', 2),
  (15, 800, 'ORG', 2),
  (16, 510, 'VIP', 1);
```

### MariaDB Form #1

Form #1 is supported as long as the table being deleted cannot be referenced in a subquery. If it needs
to be referenced, use Form #3.

### MariaDB Form #2 -- Not Supported

### MariaDB Form #3 (https://dbfiddle.uk/M0qvo8Sc)

```sql
delete vehicle
from vehicle
join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x
where vehicle.dealership_id = x.id
  and vehicle.value < x.avg_value
  and vehicle.type <> x.main_type;
```

Delete with anti-join (delete the non-matching rows):

```sql
delete vehicle
from vehicle
left join (
  select d.id, avg(o.value) as avg_value, max(d.main_type) as main_type
  from dealership d
  join vehicle o on o.dealership_id = d.id
  group by d.id
) x on vehicle.dealership_id = x.id
   and vehicle.value < x.avg_value
where x.id is null
```

### MariaDB Form #4 -- Not Supported

### MariaDB Form #5 -- Not Supported

## Sybase ASE

```sql
create table dealership (
  id int primary key not null,
  main_type varchar(10),
  name varchar(10)
)

insert into dealership (id, main_type, name) values (1, 'VIP', 'Luxy')

insert into dealership (id, main_type, name) values (2, 'ORG', 'Maans')

create table vehicle (
  id int primary key not null,
  value int,
  type varchar(10),
  dealership_id int references dealership (id)
)

insert into vehicle (id, value, type, dealership_id) values (10, 500, 'VIP', 1)

insert into vehicle (id, value, type, dealership_id) values (11, 200, 'ORG', 1)

insert into vehicle (id, value, type, dealership_id) values (12, 400, 'VIP', 1)

insert into vehicle (id, value, type, dealership_id) values (13, 600, 'ORG', 2)

insert into vehicle (id, value, type, dealership_id) values (14, 150, 'VIP', 2)

insert into vehicle (id, value, type, dealership_id) values (15, 800, 'ORG', 2)

insert into vehicle (id, value, type, dealership_id) values (16, 510, 'VIP', 1)
```

### Sybase ASE Form #2

```sql
delete from vehicle
from vehicle v, dealership d
where v.dealership_id = d.id
  and d.main_type = 'VIP'
```

Notes:

- No subqueries are allowed.
- Only inner joins can be achieved.

### Sybase ASE Form #3 -- Not Supported

### Sybase ASE Form #4 -- Not Supported

### Sybase ASE Form #5 -- Not Supported


## H2, HyperSQL, Derby

Only Form #1 is supported by these databases.

# DELETE

Form #1: SQL-92
Form #2: Joins
Form #3: Delete from Subquery
Form #3: CTEs

| Database   | Form #1<br/>SQL-92 | Form #2<br/>Joins | Form #3<br/>Subquery | Form #4<br/>CTE |
| -- |:--:|:--:|:--:|:--:|
| Oracle     | Yes                |                   |                      |                 |
| DB2        | Yes                | --                | Yes                  |                 |
| PostgreSQL | Yes                | Yes               | --                   | Yes             |
| SQL Server | Yes                |                   |                      |                 |
| MySQL      | Yes                | Yes               | --                   | Yes             |
| MariaDB    | Yes                |                   |                      |                 |
| Sybase ASE | Yes                |                   |                      |                 |
| H2         | Yes                |                   |                      |                 |
| HyperSQL   | Yes                |                   |                      |                 |
| Derby      | Yes                |                   |                      |                 |


## Oracle (from 23c)

Only deletes from T1:

```sql
delete t1 a
from   t2 b
where  a.id = b.id
and    b.id <= 5;
```

...with more tables we can use `JOIN`:

```sql
delete t1 a
from   t2 b
join   t3 c on b.id = c.id
where  a.id = b.id
and    b.id <= 5;
```

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

### DB2 Form #3 (https://dbfiddle.uk/iE3QgJoW)

Delete vehicles keeping only the cheapest one in each dealership:

```sql
delete from (
  select row_number() over(partition by dealership_id order by value) as rn
  from vehicle
)
where rn > 1
```

### DB2 Form #4 -- Not Supported


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

### PostgreSQL Form #4 (https://dbfiddle.uk/1k96wTrS)

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
to be referenced, use Form #2.

### MySQL Form #2 (https://dbfiddle.uk/wsl_wGGH)

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

LEFT JOIN form (11, 12, 14):

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

### MySQL Form #3 -- Not Supported

### MySQL Form #4 (https://dbfiddle.uk/IbeSNgQn)

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
```

### MariaDB Form #2 (https://dbfiddle.uk/M0qvo8Sc)

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

LEFT JOIN form (11, 12, 14):

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

### MariaDB Form #3 -- Not Supported

### MariaDB Form #4 -- Not Supported



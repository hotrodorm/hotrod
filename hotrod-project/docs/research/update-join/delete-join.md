# DELETE with JOIN

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
```

## PostgreSQL   

```sql
DELETE 
FROM m_productprice B  
     USING m_product C 
WHERE B.m_product_id = C.m_product_id AND
      C.upc = '7094' AND                 
      B.m_pricelist_version_id='1000020';
```

## SQL Server

```sql
```

## MySQL

- Special case: deleting with subquery on the same table (https://dbfiddle.uk/o5VHCZl8):

```sql
delete cars
from cars
join ( -- left join also available
  select model_name, brand, min(model_id) as mid
  from cars
  group by model_name, brand
) x on cars.model_name = x.model_name
   and cars.brand = x.brand
   and cars.model_id <> x.mid;
```

## MariaDB

```sql
```




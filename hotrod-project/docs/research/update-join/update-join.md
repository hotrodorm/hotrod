# Update With Join(s)

## Oracle

## DB2

## PostgreSQL

```sql
UPDATE vehicles_vehicle v 
SET price = s.price_per_vehicle,
    tax = s.tax_on_vehicle
FROM shipments_shipment AS s
WHERE v.shipment_id = s.id 
```

For many tables:

```sql
UPDATE table_1 t1
SET foo = t2.foo,
    bar = t3.bar
FROM table_2 t2
JOIN table_3 t3 ON t3.id = t2.t3_id
WHERE t2.id = t1.t2_id
  AND t3.bar = true;
```

## SQL Server

## MySQL

## MariaDB


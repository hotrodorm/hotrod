# The UPDATE Statement

UPDATE statements update existing data in a table. 

They can specify the entirety of columns and/or rows to be modified or just subsets of them.


## Examples

The query shown below updates one columns in all rows of the table:

```sql
update product
set price = price * 1.15;
```

It can be written in LiveSQL as:

```java
ProductTable p = ProductDAO.newTable();
sql.update(p)
   .set(p.price, p.price.mult(1.15))
   .execute();
```

The next query updates several columns in a subset of the rows:

```sql
update product
set promotion = 'BOGO', sale_price = price * 0.90, sales_code = 704
where catalog_id = 1015 and type <> 'VIP';
```

It can be written as:

```java
ProductTable p = ProductDAO.newTable();
sql.update(p)
   .set(p.promotion, "BOGO")
   .set(p.sale_price, p.price.mult(0.90))
   .set(p.sales_code, 704)
   .where(p.catalog.eq(1015).and(p.type.ne("VIP")))
   .execute();
```

Note that the second parameter of the `set()` method accepts typed LiveSQL Expressions such as `p.price.mult(0.90)` (of type `NumberExpression`) or Java literals such as `704` (an Integer). The type of the second parameter is bound according to the type of the first parameter.

The filtering predicate in the WHERE clause can be as simple or as complex as needed according to the Expression Language.



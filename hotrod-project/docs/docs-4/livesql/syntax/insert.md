# The INSERT Statement

INSERT statements insert new data into a table or view.

The only return of the INSERT statement (as well as for the UPDATE and DELETE statements) is the number of inserted rows.



## Examples

The inserted values can be provided as direct values in the INSERT query itself or can be provided by a 
select query. The following examples illustrate both cases:

```sql
insert into product (id, name, price) values (1007, 'Seiko KL-101', 399.95);

insert into product (id, name, price) 
select id, name, price from new_catalog where stock > 0
```

## Inserting Using VALUES

LiveSQL can insert a single row using the `VALUES` variation of the INSERT statement. 

The follwowing INSERT:

```sql
insert into product (id, name, price) values (1007, 'Seiko KL-101', 399.95);
```

Can be written in LiveSQL as:

```java
ProductTable p = ProductDAO.newTable();
int count = sql.insert(p)
               .columns(p.id, p.name, p.price)
               .values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95))
               .execute();
```

Not all columns of the may participate in the INSERT. In that case the columns that are not mentioned will be inserted as nulls
or will use a DEFAULT value, as specified by the table constraints.

In this case the `count` will always be 1.


## Inserting From a SELECT

LiveSQL can also execute inserts combined with a SELECT statement. 

The following SQL statement:

```sql
insert into product (id, name, price) 
select id, name, price from new_catalog where stock > 0
```

Can be written in LiveSQL as:

```java
ProductTable p = ProductDAO.newTable();
NewCatalogTable c = NewCatalogDAO.newTable();
int count = sql.insert(p)
               .columns(p.id, p.name, p.price)
               .select(sql.select(c.id, c.name, c.price).from(c).where(c.stock.gt(0)))
               .execute();
```

The SELECT statement is a general SQL select that can take any complexity as needed including joins, search predicates and the 
full expression language.

Receiving the count of inserted rows is optional and can be discarded. Some applications, however,
can benefit from the counts for debugging purposes.


## Inserting Through Views

LiveSQL can also insert rows through views. From the point of view of LiveSQL there's no difference between inserting directly
into a table or through a view.

Let's consider the following view and an insertion on it:

```sql
create view vip_product as
select id, name, price, product_type from product where product_type = 'VIP';

insert into vip_product (id, name, price, product_type) values (2514, 'Tohoku AB', 14.50, 'VIP');
```

In LiveSQL the insertion could take the form:

```java
VipProductView p = VipProductDAO.newView();
sql.insert(p)
   .columns(p.id, p.name, p.price, p.productType)
   .values(sql.val(2514), sql.val("Tohoku AB"), sql.val(14.50), sql.val("VIP"))
   .execute();
```

Note, however, that not all views can be used to insert rows in the underlying tables. Each database engine imposes different
rules and limitations on insertion through views. Consult your database documentation to learn if the engine allows it for 
each specific view. The general rules across many database engines seem to be:
- A view must have a *driving table*. This driving table produces 1 row at most in the view.
- The primary key of the driving table should be available in the view.
- The rows being inserted through the view must be valid according to the filtering predicate of the view definition. In the case
above, this means the row being inserted should evaluate the predicate `product_type = 'VIP'` as `true`.

Finally, it's also possible to insert into view using SELECT queries, as in:

```sql
insert into vip_product (id, name, price, product_type) 
select id, name, price, product_type from new_catalog where stock > 0;
```

In LiveSQL the insertion could take the form:

```java
VipProductView p = VipProductDAO.newView();
NewCatalogTable c = NewCatalogDAO.newTable();
sql.insert(p)
   .columns(p.id, p.name, p.price, p.productType)
   .select(sql.select(c.id, c.name, c.price, c.productType).from(c).where(c.stock.gt(0)))
   .execute();
```

Consider that the insertion rules above do matter. All the rows that are being inserted should match the predicate `product_type = 'VIP'`.


## Column Names

Though the SQL Standard allows INSERT statements not to include the column names of the table where rows are being inserted, it's highly recommended 
to include the list of columns of it. If only a subset of columns participate in the INSERT any column not mentioned in the INSERT statement will 
be filled out with default values or nulls, according to the table constraints.

Even if all columns are included in the query, the engine may have reordered them due to maintenance tasks or other DDL statements. It's better 
to include the column names to ensure the correct execution of the INSERT statement all the time.

If a query provides the rows to be inserted, any valid LiveSQL SELECT query can be used to select and produce a result set for insertion.

In short, an INSERT should include all the column names that participate in it, as in:

```java
ProductTable p = ProductDAO.newTable();
sql.insert(p)
   .columns(p.id, p.name, p.price)
   .values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95))
   .execute();
```

It's also possible &ndash; but not recommended &ndash; to omit these columns, as in:

```java
ProductTable p = ProductDAO.newTable();
sql.insert(p)
   .values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95))
   .execute();
```


# The INSERT Statement

INSERT statements insert new data into a table from literal values included in the statement itself or from a select query.

The following examples illustrate both cases:

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
sql.insert(p).columns(p.id, p.name, p.price).values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95)).execute();
```

Not all columns of the may participate in the INSERT. In that case the columns that are not mentioned will be inserted as nulls
or will use a DEFAULT value, as specified by the table constraints.


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
sql.insert(p)
   .columns(p.id, p.name, p.price)
   .select(sql.select(c.id, c.name, c.price).from(c).where(c.stock.gt(0)))
   .execute();
```

The SELECT statement is a general SQL select that can take any complexity as needed including joins, search predicates and the 
full expression language.


## Inserting Through Views

LiveSQL can also insert rows through views. From the point of view of LiveSQL there's no difference between inserting directly
into a table or through a view.

```sql
create view vip_product as
select id, name, price, product_type from product where product_type = 'VIP'
```

In LiveSQL the insertion could take the form:

```java
VipProductView p = VipProductDAO.newView();
sql.insert(p).columns(p.id, p.name, p.price).values(
  sql.val(2514), sql.val("Tohoku AB"), sql.val(14.50), sql.val("VIP")
).execute();
```

Note, however, that not all views can be used to insert rows in the underlying tables. Each database engine imposes different
rules and limitations on insertion through views. Consult your database documentation to learn if the engine allows it for 
each specific view. The general rules seem to be:
- A view must have a *driving table*. This driving table produces 1 row at most in the view.
- The primary key of the driving table should be available in the view.
- The rows being inserted through the view must be valid according to the filtering predicate of the view definition. In the case
above, this means the row being inserted should evaluate the predicate `product_type = 'VIP'` as `true`.


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
sql.insert(p).columns(p.id, p.name, p.price).values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95)).execute();
```

It's also possible &ndah; but not recommended &ndah; to omit these columns, as in:

```java
ProductTable p = ProductDAO.newTable();
sql.insert(p).values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95)).execute();
```


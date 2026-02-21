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

## Inserting Using VALUES without Generated Keys

LiveSQL can insert a single row using the `VALUES` variation of the INSERT statement. 

The follwowing INSERT:

```sql
INSERT INTO product (id, name, price) VALUES (1007, 'Seiko KL-101', 399.95);
```

Can be written in LiveSQL as:

```java
ProductTable p = this.productDAO.newTable();
int count = sql.insert(p)
               .columns(p.id, p.name, p.price)
               .values(sql.val(1007), sql.val("Seiko KL-101"), sql.val(399.95))
               .execute();
```

Not all columns of the may participate in the INSERT. In that case the columns that are not mentioned will be inserted as nulls
or will use a DEFAULT value, as specified by the table constraints.

In this case the `count` will always be 1.

## Inserting Using VALUES with Generated Keys

If the table implements key generation, either with the use of an IDENTITY or a SEQUENCE LiveSQL will retrieve the generated key automatically.

The follwing example assumes the table EMPLOYEE has an IDENTITY primary key. If we assume its type as BIGINT, then the corresponding type in your app will be `Long`. Note that the primary key column (`id` in this case) is omitted in the LiveSQL query, althought it's added automatically behind the scenes when running the actual SQL query:

```java
EmployeeTable e = this.employeeDAO.newTable();
Long id = sql.insert(e)
             .columns(e.firstName, e.lastName)
             .values(sql.val("Clara"), sql.val("Clayton"))
             .execute();
```


## Inserting Using a SELECT without Generated Keys

LiveSQL can also execute inserts combined with a SELECT statement.

The following SQL statement:

```sql
INSERT INTO product (id, name, price)
SELECT id, name, price
FROM new_catalog
WHERE stock > 0
```

Can be written in LiveSQL as:

```java
ProductTable p = ProductDAO.newTable();
NewCatalogTable c = NewCatalogDAO.newTable();
int count = sql
    .insert(p)
    .columns(p.id, p.name, p.price)
    .select(sql.select(c.id, c.name, c.price).from(c).where(c.stock.gt(0)))
    .execute();
```

The SELECT statement is a general SQL select that can take any complexity as needed including joins, search predicates and the full expression language.

Since the table does not implement any key generation strategy the INSERT query returns the count of inserted rows.

## Inserting Using a SELECT with Generated Keys

If the table implements a key generation strategy, either with the use of an IDENTITY or a SEQUENCE LiveSQL will retrieve the count of inserted rows along with the list of generated keys.

```java
EmployeeTable e = this.employeeDAO.newTable();
CandidateTable c = this.candidateDAO.newTable();
InsertResult r id = sql
    .insert(e)
    .columns(e.firstName, e.lastName)
    .select(sql.val("Clara"), sql.val("Clayton"))
    .select(sql
        .select(c.fName, c.lName)
        .from(c)
        .where(c.approved.eq(1)
    )
    .execute();
int count = r.getCount();
List<Integer> keys = r.getKeys();
```

The SELECT statement is a general SQL select that can take any complexity as needed including joins, search predicates and the full expression language.

Since the table does implement a key generation strategy the INSERT query returns an InsertResult object that includes the count of inserted rows, and a list of generated keys. The type of the keys depend on the specific column type of the primary key of the table.

- [INSERT Database Support](#appendix-a-insert-database-support) 
- [INSERT Database Support](#appendix-a--insert-database-support) 
- [INSERT Database Support](#appendix-a---insert-database-support) 

See the [INSERT Database Support](#appendix-a---insert-database-support) section below for the list of databases that support this functionality.


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

## Appendix A - Insert Database Support

This appendix describes the available database support for the INSERT variations.

First, the SQL Standard defines that the INSERT clauses can take two main forms:

- Using a VALUES clause with the values to insert a single row
- Using a SELECT clause to insert the result of a query. This form can insert zero to many rows

Second, when it comes to auto generate primary key values databases implement two main solutions for it:

- Using SEQUENCES. This is how it was done in the early days
- Using IDENTITIES. This is the new solution that links the key generation to each table automatically. It's simpler to use, although less flexible that the original one. Nevertheless, it's probably the right fit for 99% of the cases compared to SEQUENCES

Considering both aspects there are four possible combinations of cases for INSERTs that include key generation. They are depicted below:

| Database   | Sequences &amp; VALUES | Sequences &amp; SELECT | Identities &amp; VALUES | Identities &amp; SELECT |
| :--------- | :---: | :---: | :---: | :---: |
| Oracle     | Yes | No *1 | Yes | No *2    |
| DB2        | Yes | Yes | Yes | Yes |
| PostgreSQL | Yes | Yes | Yes | Yes |
| SQL Server | Yes | Yes *3 | Yes | No *4 |
| MySQL      | --  | --  | Yes | Yes |
| MariaDB    | No *5  | No *5  | Yes | Yes |
| Sybase ASE | --  | --  | Yes | No  |
| H2         | Yes | Yes | Yes | Yes |
| HyperSQL   | No  | No  | Yes | Yes |
| Derby      | Yes | No*6 | Yes | No*7 |


*1 Oracle does not integrate SEQUENCES in INSERT-SELECT correctly. It works for special cases only, but not for the general case. Maybe new research will need to be done for this case.

*2 Oracle does not integrate IDENTITIES in INSERT-SELECT correctly. It shows the error "ORA-00933: SQL command not properly ended", even thought the SQL query is correct and runs in the editor. Maybe new research will need to be done for this case.

*3 SQL Server correctly reports the inserted keys, but does not report the count of inserted rows. Nonetheless, this can be deduced from the size of the list of keys.

*4 SQL Server reports the correct count of inserted rows for INSERT-SELECT with IDENTITIES, but only returns the last key (identity value) that was used.

*5 LiveSQL does not currently support SEQUENCEs (available since version 10.3) since this requires pending research.

*6 Apache Derby does not integrate SEQUENCES in INSERT-SELECT correctly.

*7 Apache Derby reports the correct count of inserted rows for INSERT-SELECT with IDENTITIES, but only returns the last key (identity value) that was used.


# Select Children by Foreign Key

CRUD implements methods to navigate by foreign keys.

Each foreign key constraint that includes the table &mdash; either as a parent
or a child table &mdash; produces a new variation in the methods to navigate foreign
keys. If the table does not participate in any foreign key constraint the navigation methods
are not generated.

The code generation logic is carefully crafted to support all variations of foreign keys.
It considers:

- Navigating to parent and to children tables.
- Simple and composite foreign keys.
- Reflexive foreign keys (navigable in both directions).
- Multiple foreign keys between each pair of table.
- Duplicate foreign key contraints.
- Column naming collisions, resolved with seams.


## Example

Consider the tables `product` and `product_price` related by a foreign key as:

```sql
create table product (
  id int primary key not null,
  name varchar(20),
  price int
);

create table historic_price (
  product_id int not null product_id) references product (id),
  from_date date not null,
  price int not null,
  primary key (product_id, from_date)
);
```

The following app excerpt finds one product and then retrieves all historic prices of it:

```java
ProductVO p = this.productDAO.selectByPK(123);

List<HistoricPriceVO> h = this.productDAO.selectChildrenHistoricPriceOf(p).fromId().toProductId();
```

Since there can be many foreign key constraints between each pair of tables the foreign key navigation
method ensures it's using the right foreign key by clearly stating:

- The direction of the relationship (`Children`) and the related table (`HistoricPridce`) it's : `.selectChildrenHistoricPriceOf(p)`.
- The local columns (`Id`) the foreign key includes: `.fromId()`.
- The foreign columns (`ProductId`) the foreign key constraint includes: `.toProductId()`.


## Column Seam

In rare cases DAO method names may produce name collisions. This happens when two or more methods
end up having the exact same name for different functionalities. In these cases it's possible to
differentiate them by defining an explicit [Column Seam](./column-seam.md) to resolve the name collision.

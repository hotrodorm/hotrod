# Select Parent by Foreign Key

CRUD implements methods to navigate by foreign keys.


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

The following app excerpt finds a product price and then retrieves the related parent product of it:

```java
Integer productId = 123;
LocalDate priceDate = LocalDate.parse("2022-11-15");
HistoricPriceVO h = this.historicPriceDAO.selectByPK(productId, priceDate);

ProductVO p = this.historicPriceDAO.selectParentProductOf(h).fromProductId().toId();
```

Since there can be many foreign key constraints between each pair of tables the foreign key navigation
method ensures it's using the right foreign key by clearly stating:

- The direction of the relationship (`Parent`) and the related table (`Product`) it's targeting: `.selectParentProductOf(h)`.
- The local columns (`Id`) the foreign key includes: `.fromId()`.
- The foreign columns (`ProductId`) the foreign key constraint includes: `.toProductId()`.


## Column Seam

In rare cases DAO method names may produce name collisions. This happens when two or more methods
end up having the exact same name for different functionalities. In these cases it's possible to
differentiate them by defining an explicit column seam. See [Column Seam](./select-by-unique-index.md#column-seam) and
[Table](../config/tags/table.md).


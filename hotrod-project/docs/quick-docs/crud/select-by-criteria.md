# Select by Criteria

CRUD combines [LiveSQL](../livesql/livesql.md) functionality to enhance table searches in the method
`selectByCriteria()`.


## Example

If the table `products` has the following data:

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 1 | TV | ELECT | true | 499 |
| 2 | Beach Shorts | CLOTH | true | 49 |
| 3 | Mill Tie | CLOTH | false | 29 | 
| 4 | HK Jeans | CLOTH | true | 79 |
| 5 | Blue Socks | CLOTH | true | 5 |
| 6 | Lipstick 305 | COSME | true | 8 |
| 7 | Sofa | HOME | false | 299 |
| 8 | Golf Clubs | SPORT | true | 450 |
| 9 | Tennis Shoes | SPORT | false | 99 |
| 10 | HP Socks | SPORT | true | 34 |


```java
ProductTable p = ProductDAO.newTable();

List<ProductVO> cheapNonSportsProducts = this.productDAO
  .selectByCriteria(p, p.price.le(100).andNot(p.type.eq("SPORT")).and(p.active))
  .orderBy(a.price.asc())
  .execute();
```

Will return the list:

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 5 | Blue Socks | CLOTH | true | 5 |
| 6 | Lipstick 305 | COSME | true | 8 |
| 3 | Mill Tie | CLOTH | false | 29 | 
| 2 | Beach Shorts | CLOTH | true | 49 |
| 4 | HK Jeans | CLOTH | true | 79 |

See also [LiveSQL and CRUD](../livesql/livesql-and-crud.md).


## Filter

The criteria to filter rows is assembled using LiveSQL syntax and can be any valid predicate. That is,
any expression that evaluates to a boolean.

## Ordering

The ordering can include simple columns (with `.asc()` or `.desc()` qualifiers) or can be any
simple or complex expression that uses LiveSQL syntax.


## Offset &amp; Limiting

It's also possible to limit the result set to a maximum number using `.limit()` and to exclude the
initial part of the result set using `.offset()` as in:

```java
ProductTable p = ProductDAO.newTable();

List<ProductVO> cheapNonSportsProducts = this.productDAO
  .selectByCriteria(p, p.price.le(100).andNot(p.type.eq("SPORT")).and(p.active))
  .orderBy(a.price.asc())
  -offset(80)
  .limit(20)
  .execute();
```

## Cursors

To avoid loading the entire result set in memory the result set can be iterated over (only once) using
a cursor. The search can be changed to:

Cursor<ProductVO> cheapNonSportsProducts = this.productDAO
  .selectByCriteria(p, p.price.le(100).andNot(p.type.eq("SPORT")).and(p.active))
  .orderBy(a.price.asc())
  -offset(80)
  .limit(20)
  .executeCursoe();
```




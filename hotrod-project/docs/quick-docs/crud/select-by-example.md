# Select by Example

The table and view DAOs include a `selectByExample()` method that helps with simple row selections.

This CRUD method provides simple out-of-the-box searching capabilities:
- **Filtering**: This method receives a VO that is used as a pattern for the search. All its non-null 
properties make up the search criteria.
- **Sorting**: The rows returned can also be sorted in ascending or descending order by adding an 
optional list of sorting columns to the parameter list. If not specified, rows are returned in any
order, and this order may change over time without notice.

For more advanced search criteria and sorting it's possible to combine LiveSQL features by using the
[Select By Criteria](./select-by-criteria.md) method. For full control over the search query see
[Nitro Queries](../nitro/nitro.md).


## Example

Consider the table `product` with the following data:

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

The following app searches for products with type `CLOTH` that are active:

```java
@Autowired
private ProductDAO productDAO;

...

ProductVO filter = new ProductVO();
filter.setType("CLOTH");
filter.setActive(true);

List<ProductVO> l = this.productDAO.selectByExample(filter);
```

It finds (rows in any order):

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 2 | Beach Shorts | CLOTH | true | 49 |
| 4 | HK Jeans | CLOTH | true | 79 |
| 5 | Blue Socks | CLOTH | true | 5 |

If we want to sort the rows by price the search can be changed to:

```java
List<ProductVO> l = this.productDAO.selectByExample(filter, ProductOrderBy.PRICE);
```

It now returns:

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 5 | Blue Socks | CLOTH | true | 5 |
| 2 | Beach Shorts | CLOTH | true | 49 |
| 4 | HK Jeans | CLOTH | true | 79 |

It's also possible to sort by price in descending order. The search changes to:

```java
List<ProductVO> l = this.productDAO.selectByExample(filter, ProductOrderBy.PRICE$DESC);
```

It now returns:

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 4 | HK Jeans | CLOTH | true | 79 |
| 2 | Beach Shorts | CLOTH | true | 49 |
| 5 | Blue Socks | CLOTH | true | 5 |

Finally, it's also possible to sort by multiple columns by adding more ordering columns to the 
parameter list.



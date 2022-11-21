# Delete By Example

All table and view DAOs include an `deleteByExample()` method that helps with simple delete
operations.

This method uses an *example VO* to search for rows to delete in the same way
[Select By Example](./select-by-example.md) does.

This basic functionality can be useful in many cases, but it's not suitable for complex
delete operations. In these cases use
[Nitro General Purpose Queries](../nitro/nitro-general-purpose.md) instead.


## Example

Consider the table `product` with the following data:

| id | name | type | active | price |
| -- | -- | -- | :--: | --: |
| 1 | TV | ELECT | true | 499 |
| 2 | Beach Shorts | CLOTH | true | 49 |
| 3 | Mill Tie | CLOTH | false | 29 | 
| 4 | HK Jeans | CLOTH | true | 79 |
| 5 | Blue Socks | CLOTH | false | 5 |
| 6 | Lipstick 305 | COSME | true | 8 |
| 7 | Sofa | HOME | false | 299 |
| 8 | Golf Clubs | SPORT | true | 450 |
| 9 | Tennis Shoes | SPORT | false | 99 |
| 10 | HP Socks | SPORT | true | 34 |

The following code deletes all inactive products in the `CLOTH` section:

```java
@Autowired
private ProductDAO productDAO;

...

ProductVO example = new ProductVO();
example.setType("CLOTH");
example.setActive(false);

int rowCount = this.productDAO.deleteByExample(example);
```

The resulting SQL stement is:

```sql
DELETE FROM product
WHERE type = 'CLOTH' and active = false
```

## The Row Count

The Delete By Example method returns the row count of deleted rows. 

In the example above it will return `2` since two rows where deleted (id = `3` and id = `5`).







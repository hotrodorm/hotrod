# Update by Example

All table and view DAOs include an `updateByExample()` method that helps with simple update
operations.

This method uses two VOs. It uses the *example VO* to search rows by the example in the same
way the [Select By Example](./select-by-example.md) method does. Then it uses the *values VO*
to update the selected rows; only non-null values in the latter are used, so it's possible
to perform partial updates.

This basic functionality can be useful in many cases, but it's not suitable for complex
updates. In these cases use
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

The following code activates all inactive products in the `CLOTH` section:

```java
@Autowired
private ProductDAO productDAO;

...

ProductVO example = new ProductVO();
example.setType("CLOTH");
example.setActive(false);

ProductVO newValues = new ProductVO();
newValues.setActive(true);

int rowCount = this.productDAO.updateByExample(example, newValues);
```

The resulting SQL stement is:

```sql
UPDATE product
SET active = true
WHERE type = 'CLOTH' and active = false
```

## The Row Count

The Update By Example method returns the row count of affected rows. In the
example above it will return `2` since two rows where updated (id = 3 and id = 5).







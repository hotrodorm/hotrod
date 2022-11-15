# LiveSQL &amp; CRUD

LiveSQL enhances the `selectByExample()` CRUD simple searches with `selectByCriteria()` searches.
Both searches return typed lists of domain VOs but the second one offers more options. 

The `selectByCriteria` search:
- Can specify a complex search criteria using [LiveSQL expressions](./syntax/expressions.md).
- Can use LiveSQL expression to order rows.
- Can add offset and limit functionality to the search.
- Can return cursor of VOs instead of a list, to optimize the processing of large result sets.


## Example

The following query searches for accounts with a more elaborated filter:

```java
AccountTable a = AccountDAO.newTable();

List<AccountVO> accounts = this.accountDAO.selectByCriteria(a, 
    a.active.eq(1).or(a.type.eq("CHK").andNot(a.currentBalance.lt(100))))
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM account
WHERE active = 1 OR (type = 'CHK' AND NOT current_balance < 100)
```



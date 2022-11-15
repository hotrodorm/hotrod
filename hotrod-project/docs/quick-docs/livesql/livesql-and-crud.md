# LiveSQL and CRUD

LiveSQL enhances CRUD's `selectByExample()` simple searches with the more advanced `selectByCriteria()` searches.

Both searches return typed domain VOs but `selectByCriteria()` adds the following features:

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



# LiveSQL and CRUD

LiveSQL enhances CRUD's `selectByExample()` simple searches with the more advanced `selectByCriteria()` searches.

Both searches return typed domain VOs but `selectByCriteria()` adds the following features:

- Can specify a complex search criteria using [LiveSQL expressions](./syntax/expressions.md).
- Can use LiveSQL expression to order rows.
- Can add offset and limit functionality to the search.
- Can return cursor of VOs instead of a list, to optimize the processing of large result sets.


## Example

The following query searches for accounts with a more elaborated condition:

```java
AccountTable a = AccountDAO.newTable();

List<AccountVO> accounts = this.accountDAO
  .selectByCriteria(a, a.active.eq(1).or(a.type.eq("CHK").andNot(a.currentBalance.lt(100))))
  .execute();
```

The resulting query is:

```sql
SELECT *
FROM account
WHERE active = 1 OR (type = 'CHK' AND NOT current_balance < 100)
```

## Adding Custom Ordering, Offset, Limit, and Reading with a Cursor

The following example depicts the usage of a custom ordering, offset, limit, and reading VOs with
a cursor:

```java
AccountTable a = AccountDAO.newTable();

Cursor<AccountVO> accounts = this.accountDAO
  .selectByCriteria(a, a.name.like("CHK%"))
  .orderBy(sql.caseWhen(a.type.eq("VIP"), 1).when(a.currentBalance.ge(10_000), 2).elseValue(3).end().asc(), a.id.desc())
  .offset(200)
  .limit(50)
  .executeCursor();
```

The resulting query is:

```sql
SELECT *
FROM account a
WHERE a.name like 'CHK%'
ORDER BY CASE WHEN a.type = 'VIP' THEN 1 
              WHEN a.current_balance >= 10000 THEN 2
              ELSE 3
         END ASC,
         a.id DESC
OFFSET 200
LIMIT 50
```

## Previewing SQL

Since `selectByCriteria()` searches are implemented by the LiveSQL engine, the generated native SQL queries can be previewed
in the same way other LiveSQL queries are. See [Previewing LiveSQL](previewing-livesql.md) for details.



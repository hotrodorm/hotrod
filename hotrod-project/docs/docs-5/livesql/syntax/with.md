# The WITH Clause

A SELECT query can use Common Table Expressions (CTEs) prepending the WITH clause to it. This clause
includes a comma-separated list of CTEs, each one defining a table expression that can be used multiple times by subsequent CTEs and in the main SELECT query.

Generally speaking, Common Table Expressions are subqueries defined separately before the main query, so they can be used in the main query one or more times. Moreover, any CTE can use any previously defined CTEs, in the order they are defined effectively reusing them, something that a traditional table expression cannot do.

For example, the query above can also be written with CTEs. It takes the form:

```sql
WITH
x AS (
  SELECT b.is_vip, a.id, a.branch_id
  FROM branch b
  JOIN account a ON a.branch_id = b.id
),
y (aid) AS (
  SELECT i.account_id
  FROM invoice i
  JOIN invoice_line l ON l.invoice_id = i.id
  JOIN product p ON p.id = l.product_id
  WHERE p.shipping = 0
)
SELECT x.is_vip, x.id, x.branch_id
FROM x
LEFT JOIN y ON y.aid = x.id
WHERE y.aid is null
```

It can be written in LiveSQL as:

```java
BranchTable b = BranchDAO.newTable("b");
AccountTable a = AccountDAO.newTable("a");
InvoiceTable i = InvoiceDAO.newTable("i");
InvoiceLineTable l = InvoiceLineDAO.newTable("l");
ProductTable p = ProductDAO.newTable("p");

CTE x = sql.cte("x",
    sql.select(b.isVip, a.star())
        .from(b)
        .join(a, a.branchId.eq(b.id))
);
CTE y = sql.cte("y", "aid").as(sql.select(i.accountId)
    .from(i)
    .join(l, l.invoiceId.eq(i.id))
    .join(p, p.id.eq(l.productId))
    .where(p.shipping.eq(0)));
List<Row> rows = sql.with(x, y)
    .select(x.star())
    .from(x)
    .leftJoin(y, y.num("aid").eq(x.num("id")))
    .where(y.num("aid").isNull())
    .execute();
```

Note that the first CTE does not alias its columns, but the second one does. That means that the first one assumes the column names directly from the subquery.

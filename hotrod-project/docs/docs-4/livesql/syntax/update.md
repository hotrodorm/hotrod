# The UPDATE Statement

UPDATE statements in SQL update existing data in a table. They can operate directly on a table and sometimes also through a database view.

They can specify the entire list of columns and/or rows to be modified or just subsets of them.

The only return of the UPDATE statement (as well as for the INSERT an DELETE statements) is the number of updated rows.


## Examples

The query shown below updates one columns in all rows of the table:

```sql
update product
set price = price * 1.15;
```

It can be written in LiveSQL as:

```java
ProductTable p = ProductDAO.newTable();
sql.update(p)
   .set(p.price, p.price.mult(1.15))
   .execute();
```

The next query updates several columns in a subset of the rows:

```sql
update product
set promotion = 'BOGO', sale_price = price * 0.90, sales_code = 704
where catalog_id = 1015 and type <> 'VIP';
```

It can be written as:

```java
ProductTable p = ProductDAO.newTable();
int count = sql.update(p)
               .set(p.promotion, "BOGO")
               .set(p.salePrice, p.price.mult(0.90))
               .set(p.salesCode, 704)
               .where(p.catalogId.eq(1015).and(p.type.ne("VIP")))
               .execute();
```

Note that the second parameter of the `set()` method accepts typed LiveSQL Expressions such as `p.price.mult(0.90)` (of type `NumberExpression`) or Java literals such as `704` (an Integer). The type of the second parameter is bound according to the type of the first parameter.

The filtering predicate in the WHERE clause can be as simple or as complex as needed according to the Expression Language.

The UPDATE functionality returns the number of affected rows by the update query. This can be used to implement [Optimistic Locking](../optimistic-locking.md).


## Parameterized Values vs Literal Values

As in any other LiveSQL query, Java literals are by default parameterized. This typically helps with
database optimization, as explained in [Literals - Performance Side Effects](./literals). However, sometimes it could be beneficial to render them as plain SQL literals instead. For example, in this case the line:

```java
               .set(p.promotion, "BOGO")              // Generates: p.promotion = ?
```

can be written as:

```java
               .set(p.promotion, sql.literal("BOGO")) // Generates: p.promotion = 'BOGO'
```

Use SQL literals sparely, when you consider it makes sense to do it.


## Setting Nulls

For each of the six main data types the `set()` method of the builder accepts two variations. For example, to set a String value you can set the value
as an expression (variation #1), as in:

```java
   .set(c.address, c.location)
   .set(c.fullName, c.firstName.concat(" ").concat(c.lastName))
```

or as a Java String literal (variation #2):

```java
   .set(c.address, "Oak Street 1450")
   .set(c.fullName, "Julia Smith")
```

However, setting a literal null can be problematic since the Java compiler won't know which variation of the method you are trying to use:

```java
   .set(c.address, null) // Invalid -- does not compile
```

You can set a literal null:

```java
   .set(c.address, sql.NULL) // Valid (SQL literal NULL)
```

or a parameterized one:

```java
   .set(c.address, (String) null) // Valid (parameterized NULL)
```

The latter has the same effect as:

```java
   String a = null;
   ...

   .set(c.address, a) // Valid (parameterized NULL)
```

The same rule applies to set nulls for numeric columns, date/time, boolean, binary, and object ones.


## Updating Through a View

From the LiveSQL perspective there's no difference in syntax to update rows through a view instead of directly on a table.

Nevertheless, each database engine imposes different rules about updating rows through a view. Depending on each the view 
definition, some views may be appropriate to update rows through them while others may not be. Consult your database 
documentation to find out if each one can be used for this functionality.



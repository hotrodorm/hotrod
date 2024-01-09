# The DELETE Statement

DELETE statements delete rows from a table or view. They can delete the entirety of rows of the table or just subsets of them.

Most business-grade application never delete rows from a table, but they only phase them out. It's common, however, to delete
rows from non-fact tables or from temporary ones.

The only return of the DELETE statement (as well as for the INSERT an UPDATE statements) is the number of updated rows.


## Examples

The query shown below deletes **all the rows** (use with caution) from a table:

```sql
delete from daily_summary;
```

It can be written in LiveSQL as:

```java
DailySummaryTable s = DailySummaryDAO.newTable();
sql.delete(s).execute();   
```

The next query deletes a subset of rows of a table according to a specific search criteria:

```sql
delete from promotion
where status = 'DRAFT' and department = 'GARDEN';
```

It can be written as:

```java
PromotionTable p = PromotionDAO.newTable();
int count = sql.delete(p)
               .where(p.status.eq("DRAFT").and(p.department.eq("GARDEN")))
               .execute();
```

The filtering predicate in the WHERE clause can be as simple or as complex as needed according to the Expression Language.

The UPDATE method return the number of deleted rows.

## Parameterized Values vs Literal Values

As in any other LiveSQL query, Java literals are by default parameterized. This typically helps with
database optimization, as explained in [Literals - Performance Side Effects](./literals). However, sometimes
it could be beneficial to render them as plain SQL literals instead. For example, in this case the line:

```java
  .where(p.status.eq("DRAFT").and(p.department.eq("GARDEN")))
  // Generates: p.status = ? and p.department = ?
```

can be written as:

```java
  .where(p.status.eq(sql.literal("DRAFT")).and(p.department.eq(sql.literal("GARDEN"))))
  // Generates: p.status = 'DRAFT' and p.department = 'GARDEN'
```

Use SQL literals sparely, when you consider it makes sense to do it. 

In the case shown above it could probably make sense
to use a literal in the first case (DRAFT) since there are probably few values related to it and that could help with
optimization. However, it's could probably detrimental to use literals in the second case, since the value GARDEN is
just one of many, that is even subject to change or to be deleted. Using a literal in this case could prevent database
query caching, leading to more resource consumption in the database.
 

## Deleting Through a View

From the LiveSQL perspective there's no difference in syntax to delete rows through a view instead of directly from a table.

Nevertheless, each database engine imposes different rules about deleting rows through a view. Depending on each the view 
definition, some views may be appropriate to delete rows through them while others may not be. Consult your database 
documentation to find out if each one can be used for this functionality.


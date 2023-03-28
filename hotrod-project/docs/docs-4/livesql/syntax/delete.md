# The DELETE Statement

DELETE statements delete rows from a table or view. They can delete the entirety of rows of the table or just subsets of them.

Most business-grade application never delete rows from a table, but they only phase them out. It's common, however, to delete
rows from non-fact tables or from temporary ones.


## Examples

The query shown below deletes **all the rows** (use with caution) from a temporary table:

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
sql.delete(p)
   .where(p.status.eq("DRAFT").and(p.department.eq("GARDEN")))
   .execute();
```

The filtering predicate in the WHERE clause can be as simple or as complex as needed according to the Expression Language.



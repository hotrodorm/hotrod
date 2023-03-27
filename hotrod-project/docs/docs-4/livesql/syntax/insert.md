# The INSERT Statement

INSERT statements insert new data into a table from literal values included in the statement itself or from a select query.

It's highly recommended to include the list of columns that are to be inserted. If the insert. Any column not mentioned in the INSERT statement will be filled out with default values or nulls, according to the table constraint. Even if all columns are included in the query, the engine may have reordered them due to maintenance or other DDL statements, so it's better to include them to ensure the correct execution of the INSERT statement. 

If a query provides the rows to be inserted, any valid LiveSQL SELECT query can be used to select and produce a result set for insertion.


## Inserting Using Literal `VALUES`

Consider the table:

```sql
create table stock (
  sku varchar(10),
  name varchar(40),
  quantity int
);
```

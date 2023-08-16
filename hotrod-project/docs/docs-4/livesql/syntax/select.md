# The SELECT Statement

SELECT statements retrieve data from a table or view in the database.

## A Basic Example

The following example retrieves all employees whose name starts with the letter A:

```java
@Autowired
private LiveSQL sql;

private void searching() {

  EmployeeTable e = EmployeeDAO.newTable();

  List<Row> rows = this.sql
    .select()
    .from(e)
    .where(e.name.like("A%"))
    .execute();

  for (Row r : rows) {
    System.out.println("employee: " + r);
  }

}
```

The syntax includes the SELECT clauses with search criteria, joins, etc.

## The SELECT Clauses

The SELECT statement has several clauses that are described separately:

- [The SELECT List](./select-list.md)
- [The FROM and JOIN Clauses](./from-and-joins.md)
- [Selecting Without A FROM Clause](./selecting-without-a-from-clause.md)
- [The WHERE Clause](./where.md)
- [The GROUP BY Clause](./group-by.md)
- [The HAVING Clause](./having.md)
- [The ORDER BY Clause](./order-by.md)
- [The OFFSET Clause](./offset.md)
- [The LIMIT Clause](./limit.md)

## Subqueries

A SELECT can participate as a top clause or as a subquery. As a top clause it returns rows 
directly to the calling application. As a subquery it computes rows that are received by
an upper level query that processes them accordingly; as subqueries SELECT clauses can 
be nested in multiple levels as needed.



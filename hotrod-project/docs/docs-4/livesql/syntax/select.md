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
- [The FOR UPDATE Clause](./for-update.md)
- [The UNION/INTERSECT/EXCEPT [ALL] Clauses](./set-operators.md)


## Subqueries

A SELECT can participate as a top clause or as a [subquery](./subqueries.md). As a top clause it returns rows 
directly to the calling application. As a subquery it computes rows that are received by
an upper level query that processes them accordingly; as subqueries SELECT clauses can 
be nested in multiple levels as needed.


## Return a List, a Single Row, or a Cursor

The LiveSQL example above ends with the `.execute()` method that executes
the query to return a list of rows (or entities in the case of the Select by Criteria).

However, a LiveSQL SELECT query can have three ways of returning its result:

- *List&lt;Row&gt; execute()*
- *Row executeOne()*
- *Cursor&lt;Row&gt; executeCursor()*

These methods have a corresponding form for entities. For example, if the `InvoiceDAO` object is 
used to handle the table INVOICE persistence, these methods are included in it as:

- *List&lt;Invoice&gt; execute()*
- *Invoice executeOne()*
- *Cursor&lt;Invoice&gt; executeCursor()*

### Returning a List

The main method `execute()` retrieves a fully materialized complete list of rows into the application memory. This is the most common use of SELECT queries
when the number of returned rows is small or medium size at the most. Materializing a big list in memory can negatively impact your application. If a query returns
a massive number of rows, consider returning a *Cursor*, as shown below.

### Returning a Single Row

The method `executeOne()` returns a single row from the database. This means that LiveSQL expects to receive
zero or one row at the most from the query. It's the developer responsibility to ensure the SELECT query does
not return more than one row.

If the query returns two or more rows, a `TooManyResultsException` exception is thrown.

## Returning a Cursor

The `executeCursor()` method avoids materializing the whole result set at once into a `java.util.List` and instead
uses buffering to read rows one at a time. This strategy can drastically reduce the memory consumption of 
the application.

A LiveSQL cursor is a read-only, forward-only iterable object that can return rows one by one.

Internally the JDBC driver and the database automatically determine a buffer size to accommodate the rows
and populate and clean the buffer automatically behind the scenes. The application does not need to handle
the low-level details of the cursor.

Using a cursor, the example above takes the form:

```java
@Autowired
private LiveSQL sql;

private void searching() {
  EmployeeTable e = EmployeeDAO.newTable();

  Cursor<Row> rows = this.sql
    .select()
    .from(e)
    .where(e.name.like("A%"))
    .executeCursor();
    
  for (Row r : rows) {
    // do something
  }
  
}
```

**Important Note**: Cursors live only inside the database transaction. This means that as soon as the outermost
Spring method annotated with `@Transactional` ends, the cursor is automatically closed and cannot be read
anymore. You need to keep this in mind in case a method returns a `Cursor<Row>`, so it's always returned to
an enclosing method within the boundaries of the database transaction.


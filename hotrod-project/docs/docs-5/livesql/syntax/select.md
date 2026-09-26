# The SELECT Statement

The LiveSQL SELECT statement retrieves data from a table or view in the database.

Its main features include:

- Numeric, char, date, time, boolean, and binary literals and combined expressions
- All traditional SELECT clauses such as FROM, JOIN, WHERE, GROUP BY, HAVING, ORDER BY, OFFSET, LIMIT, etc.
- Aggregation Operations, including Window Functions
- Subqueries
- Set Operators
- Predefined function and custom functions
- Common Table Expressions, including recursive ones
- Assymetric Operators
- Row Locking

## A Basic SELECT Example

The following example shows three ways of selecting the from a table according to a search criteria: employees whose name starts with the letter A.

They trade simplicity for flexibility and can be a great fit for different use cases.

### 1. General Form

The general form is the most flexible one and can combine multiple tables (or none at all), and can include the full set of LiveSQL clauses described below. All expressions are valid, simple or complex, and their type is resolved at runtime.

The result set is represented by a `Row` object:

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
    System.out.println("Employee ID: " + r.get("id"));
  }

}
```

### 2. Entities

The second form is the simplest one. It's tailored to select rows from a single table or view. It cannot include extra columns and cannot do aggregations.

The result set is represented by the exact entity classes it models, with all data types and conversions resolved according to the persistence layer.

```java
@Autowired
private LiveSQL sql;

private void searching() {

  EmployeeTable e = EmployeeDAO.newTable();

  List<Employee> emps = this.sql
    .select(e, e.name.like("A%"))
    .execute();

  for (Employee e : emps) {
    System.out.println("Employee ID: " + e.get("id"));
  }

}
```

### 3. Entity Tuples

The third form shines when joining multiple tables or views, since it's particularly good at representing the result of joins as fully separate tuples. This form can also include extra columns; it cannot do aggregations, however.

The result set is represented by a tuple of the entity classes it includes, with all data types and conversions resolved according to the persistence layer.

```java
@Autowired
private LiveSQL sql;

private void searching() {

  EmployeeTable e = EmployeeDAO.newTable();

  List<Tuple1<Employee>> tuples = this.sql
    .select()
    .tuples()
    .from(e)
    .where(e.name.like("A%"))
    .execute();

  for (Tuple1<Employee> t : tuples) {
    Employee emp = t.getA(); // The tuples are named A, B, C, etc.
    System.out.println("Employee ID: " + emp.getId());
  }

}
```

## The SELECT Clauses

The SELECT statement has several clauses that are described separately:

- [The WITH Clause](./with.md)
- [The SELECT List](./select-list.md)
- [The DISTINCT ON Clause](./distinct-on.md)
- [The FROM and JOIN Clauses](./from-and-joins.md)
- [Selecting Without A FROM Clause](./selecting-without-a-from-clause.md)
- [The WHERE Clause](./where.md)
- [The GROUP BY Clause](./group-by.md)
- [The HAVING Clause](./having.md)
- [The ORDER BY Clause](./order-by.md)
- [The OFFSET Clause](./offset.md)
- [The LIMIT Clause](./limit.md)
- [The FOR UPDATE &amp; FOR SHARE Clauses](./pessimistic-locking.md)
- [The UNION/INTERSECT/EXCEPT [ALL] Clauses](./set-operators.md)

## Selecting Tuples

LiveSQL can also select separate tuples for tables and views included in a SELECT query. For example, the following query retrieves multiple model objects per row, in addition to on-the-fly extra columns:

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql
    .select(e.star(), b.star(),
      sql.caseWhen(b.region.eq("main"), "Senior ").elseValue("").end()
         .concat(b.title).as("title")
    )
    .tuples() // here the magic starts
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.name.like("%Anne%"))
    .execute();
for (Tuple2<Employee, Branch> r : rows) {
  Employee account = r.getA(); // Model tuples are separated
  Branch branch = r.getB();
  String title = r.get("title", String.class);
  System.out.println("Employee: " + account);
  System.out.println("Branch: " + branch);
  System.out.println("Title: " + title);
  }
}
```

See [Tuples](./tuples.md) for details.

## Subqueries

A SELECT can participate as a top clause or as a [subquery](./subqueries.md). As a top clause it returns rows 
directly to the calling application. As a subquery it computes rows that are received by
an upper level query that processes them accordingly; as subqueries SELECT clauses can 
be nested in multiple levels as needed.


## Return a List, a Single Row, or a Cursor

The LiveSQL example above ends with the `.execute()` method that executes the query to return a materialized list of rows.

LiveSQL can also return a single row or a stream of rows. Therefore, a generic SELECT can take three forms:

- List&lt;Row&gt; execute()
- Row executeOne()
- Cursor&lt;Row&gt; executeCursor()

Tuples SELECT have corresponding methods. For example, a join of the three tables CLIENT, PAYMENT, and BRANCH will return:

- List&lt;Tuple3&lt;Client, Payment, Branch&gt;&gt; execute()
- Tuple3&lt;Client, Payment, Branch&gt; executeOne()
- Cursor&lt;Tuple3&lt;Client, Payment, Branch&gt;&gt; executeCursor()

Finally, entity selects implement a simplified version of tuples. For example, selecting from the table INVOICE can be done with the folowing entity selects:

- List&lt;Invoice&gt; execute()
- Invoice executeOne()
- Cursor&lt;Invoice&gt; executeCursor()

### Returning a List

The main method `execute()` retrieves a fully materialized complete list of rows into the application memory. This is the most common use of SELECT queries
when the number of returned rows is small or medium size at the most. Materializing a big list in memory can negatively impact your application. If a query returns
a massive number of rows, consider returning a *Cursor*, as shown below.

### Returning a Single Row

The method `executeOne()` returns a single row from the database. This means that LiveSQL expects to receive
zero or one row at the most from the query. It's the developer responsibility to ensure the SELECT query does
not return more than one row.

If the query returns two or more rows, a `TooManyResultsException` exception is thrown.

### Returning a Cursor

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

@Transactional
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

**Important Note**: Cursors typically can be only accessed inside a database transaction. This means that as soon as the outermost
Spring method annotated with `@Transactional` ends, the cursor is automatically closed and cannot be read
anymore. You need to keep this in mind in case a method returns a `Cursor<T>`, so it's always returned to
an enclosing method within the boundaries of the database transaction.


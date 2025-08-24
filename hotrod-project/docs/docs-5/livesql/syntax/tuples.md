# LiveSQL Tuples

LiveSQL Tuples select model objects for the tables and views used in a SELECT query. This can be particularly useful when a query joins multiple tables and view, and the application benefits from retrieving multiple separate objects for each retrieved row.

Tuples are available since HotRod 5.1.

The clause `.tuples()` enables the tuples form of queries.

## Example 1 &mdash; Enhanced Select by Criteria

Tuples implement an enhanced variation of the Select by Criteria, since it can also implement semi-joins, anti-joins, with CTEs and/or subqueries. Unlike the former, it also has the ability to retrieve a specific subset of columns only, as shown in example #2.

The resulting model objects use the appropriate data types, converters, etc. The following query:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql
    .select()
    .tuples() // this clause enables tuples
    .from(a)
    .where(a.balance.ge(500))
    .execute();
for (Tuple1<Account> r : rows) {
  Account account = r.getA();
  System.out.println("=== Account: " + account);
}
```

Produces rows with the form:

```txt
=== Account: app.persistence.model.Account@5eb5da12
- id=111
- name=1072
- type=CHK
- balance=500.0
- active=false
- updatedAt=2025-08-11T11:37:07.597668
- version=1
```

**Note**: Tuples can be used to SELECT from tables and views.


## Example 2 &mdash; Filtering Columns

A subset of the model object columns can be populated, instead of the full set (default). This can help  to avoid the overhead of heavy column types (blobs, clobs, long varchar, etc).

The subset can be defined by:

- Explicitly naming the columns (e.g `a.id`)
- Defining a custom rule to include or exclude columns. For example, to include all columns except the columns of type BLOB the following rule could be used:
 `a.star().filter(c -> !c.getType().equals("BINARY LARGE OBJECT"))`

The following query:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql
    .select(
      a.id,
      a.balance,
      a.star().filter(c -> !c.getType().equals("BINARY LARGE OBJECT"))
    )
    .tuples()
    .from(a)
    .where(a.balance.ge(500))
    .execute();
for (Tuple1<Account> r : rows) {
  Account account = r.getA();
  System.out.println("=== Account: " + account);
}
```

Produces rows with the form:

```txt
=== Account: app.persistence.model.Account@307e4c44
- id=111
- name=1072
- type=CHK
- balance=500.0
- active=false
- clientPhoto=null
- updatedAt=2025-08-12T10:12:04.273448
- version=1
```

## Example 3 &mdash; Tuples and Unbound Columns

The columns that belong to the tables and views are automatically populated in the corresponding tuples.

Other SQL expressions, such as renamed table or view columns, general SQL expressions, functions, or columns used in expressions become unbound columns.

The following query:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql
    .select(
      a.id, // in the tuple
      a.balance, // in the tuple
      a.name.as("accountNumber"), // does not belong to any tuple -- unbound
      a.balance.plus(150).as("score") // does not belong to any tuple -- unbound
    )
    .tuples()
    .from(a)
    .where(a.balance.ge(460))
    .execute();
for (Tuple1<Account> r : rows) {
  Account account = r.getA();
  System.out.println("=== Account: " + account);
  for (String prop : r.getUnbound().keySet()) {
    System.out.println("*** Unbound '" + prop + "': " + r.getUnbound().get(prop));
  }
}
```

Produces rows with the form:

```txt
=== Account: app.persistence.model.Account@1946384
- id=111
- name=null
- type=null
- balance=500.0
- active=null
- clientPhoto=null
- updatedAt=null
- version=null
*** Unbound 'accountNumber': 1072
*** Unbound 'score': 650
```

## Example 4 &mdash; Joins

Joining tables and views increase the tuple cardinality. For example, joining two tables produces a `Tuple2` while joining three tables produces a `Tuple3`, and so on. All join types are supported including inner joins, outer joins, lateral joins, self joins, joining subqueries and CTEs, using explicit join predicates, declaring columns with USING, and natural joins.

The following query:

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.name.like("%Anne%"))
    .execute();
for (Tuple2<Employee, Branch> r : rows) {
  Employee account = r.getA();
  Branch branch = r.getB();
  System.out.println("=== Employee: " + account);
  System.out.println("=== Branch: " + branch);
}
```

Produces rows with the form:

```txt
=== Employee: app.persistence.model.Employee@6adc5b9c
- id=30
- name=Anne
- branchId=101
=== Branch: app.persistence.model.Branch@19c1820d
- id=101
- region=N
- isVip=1
- parentBranchId=null
- createdAt=2024-01-01T12:34:56
```

## Example 5 &mdash; Self Joins

The following query:

```java
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple2<Branch, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(b)
    .join(p, p.id.eq(b.parentBranchId))
    .where(b.region.eq("NE"))
    .execute();
for (Tuple2<Branch, Branch> r : rows) {
  Branch branch = r.getA();
  Branch parentBranch = r.getB();
  System.out.println("=== Branch: " + branch);
  System.out.println("=== Parent Branch: " + parentBranch);
}
```

Produces rows with the form:

```txt
=== Branch: app.persistence.model.Branch@4b862408
- id=105
- region=NE
- isVip=0
- parentBranchId=101
- createdAt=2024-01-05T12:34:56
=== Parent Branch: app.persistence.model.Branch@6ddee60f
- id=101
- region=N
- isVip=1
- parentBranchId=null
- createdAt=2024-01-01T12:34:56
```

## Example 6 &mdash; Joining Multiple Tables and Views

A tuples join can join up to 26 tables and views.

The following query:

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple3<Employee, Branch, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(e)
    .join(b, e.branchId.eq(b.id))
    .join(p, p.id.eq(b.parentBranchId))
    .execute();
for (Tuple3<Employee, Branch, Branch> r : rows) {
  Employee employee = r.getA();
  Branch branch = r.getB();
  Branch parentBranch = r.getC();
  System.out.println("=== Employee: " + employee);
  System.out.println("=== Branch: " + branch);
  System.out.println("=== Parent Branch: " + parentBranch);
}
```

Produces rows with the form:

```txt
=== Employee: app.persistence.model.Employee@6418e39e
- id=33
- name=Malcolm
- branchId=107
=== Branch: app.persistence.model.Branch@3635099
- id=107
- region=SE
- isVip=0
- parentBranchId=102
- createdAt=2024-01-07T12:34:56
=== Parent Branch: app.persistence.model.Branch@1da1380b
- id=102
- region=S
- isVip=1
- parentBranchId=null
- createdAt=2024-01-02T12:34:56
```


## Example 7 &mdash; Semi Joins

A semi join joins a table or view in a query but does not retrieve any of the data from it. This is the case of an anti-join or a plain semi-join; when LiveSQL declares it
as such the semi-joined table is excluded from the resulting tuple.

To do this LiveSQL includes extra joining methods that prepend the word "semi" to them, as in `.semiLeftJoin()`. This variation is implemented for all types of joins, including inner joins, outer joins, lateral joins, self joins, using explicit join predicates, declaring columns with USING, and natural joins.

The following query uses an anti-join to retrieve all "leaf" branches; that is, branches that do not have any sub-branches. While the table `p` is joined in the query for the purpose of eliminating rows its data is not included in the retrieved tuple:

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(e)                                     // e is included in the tuple
    .join(b, e.branchId.eq(b.id))                // b is included in the tuple
    .semiLeftJoin(p, p.parentBranchId.eq(b.id))  // p is excluded from the tuple
    .where(p.id.isNull())
    .execute();
for (Tuple2<Employee, Branch> r : rows) {
  Employee employee = r.getA();
  Branch branch = r.getB();
  System.out.println("=== Employee: " + employee);
  System.out.println("=== Branch: " + branch);
}
```

Produces rows with the form:

```txt
=== Employee: app.persistence.model.Employee@2ec99035
- id=32
- name=Jeanne
- branchId=104
=== Branch: app.persistence.model.Branch@60743cdb
- id=104
- region=E
- isVip=0
- parentBranchId=102
- createdAt=2024-01-04T12:34:56
```

## Example 8 &mdash; Joining Subqueries and CTEs

The following query:

```java
BranchTable br = this.branchDAO.newTable();
CTE y = sql.cte("y", sql.select(sql.min(br.region).as("minRegion")).from(br));

EmployeeTable e = this.employeeDAO.newTable();
Subquery x = sql.subquery("x", sql.select(sql.currentDate().as("currentDate")));
BranchTable b = this.branchDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql
    .with(y)
    .select()
    .tuples()
    .from(e)
    .crossJoin(x) // Subqueries do not increase the cardinality of the tuple
    .crossJoin(y) // CTEs do not increase the cardinality of the tuple
    .join(b, b.id.eq(e.branchId).and(b.region.eq(y.str("minRegion"))))
    .execute();

for (Tuple2<Employee, Branch> r : rows) {
  Employee employee = r.getA();
  Branch branch = r.getB();
  System.out.println("=== Employee: " + employee);
  System.out.println("=== Branch: " + branch);
  for (String prop : r.getUnbound().keySet()) {
    System.out.println("*** Unbound '" + prop + "': " + r.getUnbound().get(prop));
  }
}
```

Produces rows with the form:

```txt
=== Employee: app.persistence.model.Employee@21de60a7
- id=32
- name=Jeanne
- branchId=104
=== Branch: app.persistence.model.Branch@73894c5a
- id=104
- region=E
- isVip=0
- parentBranchId=null
- createdAt=2024-01-04T12:34:56
*** Unbound 'currentDate': 2025-08-12
*** Unbound 'minRegion': E
```

## Example 9 &mdash; Selecting Using Cursors

The following code looks remarkably similar to using a list. However, it implements streaming of rows:

```java
AccountTable a = this.accountDAO.newTable();

Cursor<Tuple1<Account>> rows = this.sql
    .select()
    .tuples()
    .from(a)
    .where(a.balance.ge(500))
    .executeCursor();
for (Tuple1<Account> r : rows) {
  Account account = r.getA();
  System.out.println("=== Account: " + account);
}
```

Produces rows with the form:

```txt
=== Account: app.persistence.model.Account@25a94b55
- id=111
- name=1072
- type=CHK
- balance=500.0
- active=false
- updatedAt=2025-08-11T13:40:27.216758
- version=1
```

It's also possible to specify the desired fetch size using the variation `.executeCursor(<desired-fetch-size>)`. The desired fetch size is only an indication to the driver; this one, in turn, can ignore or adjust this value without notice.

## Example 10 &mdash; Selecting a Single Row

The following query:

```java
AccountTable a = this.accountDAO.newTable();

Tuple1<Account> row = this.sql
    .select()
    .tuples()
    .from(a)
    .where(a.balance.ge(450))
    .orderBy(a.updatedAt.desc())
    .limit(1)
    .executeOne();
Account account = row.getA();
System.out.println("=== Account: " + account);
```

Produces a row with the form:

```txt
=== Account: app.persistence.model.Account@526e8108
- id=111
- name=1072
- type=CHK
- balance=500.0
- active=false
- clientPhoto=[B@4dcbae55
- updatedAt=2025-08-12T11:57:51.662793
- version=1
```

If no rows are found then it produces a null. If more than a single row is found the method throws an exception.

**Note**: Make sure the query returns one row at the most &ndash; e.g. by the use of `.limit(1)` as in this example, or by other means &ndash; to prevent the query to throw an exception.




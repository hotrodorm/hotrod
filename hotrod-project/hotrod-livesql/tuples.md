# Feature Preview -- Tuples in LiveSQL

This is a feature preview for the Tuples functionality targeted for HotRod 5.1. This new feature can
selectg tuples for tables and views by themselves or while using joins.

The clause `.tuples()` enables the tuples form of queries.

## Example 1 -- Equivalent to Select by Criteria

In addition of covering all forms of the Select By Criteria queries, this form can also implement semi-joins, anti-joins, with CTEs and/or subqueries. As shown in Example #2, it has the ability to retrieve a subset of the columns of the table (or view).

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

## Example 2 -- Filtering Columns

A subset of the model object columns can be populated, instead of the full set (default). This can help  to avoid the overhead of heavy column types (blobs, clobs, long varchar, etc).

The subset can be defined by:

- Explicitly naming the columns (e.g `a.id`)
- Defining a rule to include columns (eg `a.star().filter(c -> c.getType().equals("TIMESTAMP"))`)

The following query:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql
    .select(
      a.id,
      a.balance,
      a.star().filter(c -> c.getType().equals("TIMESTAMP"))
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
=== Account: app.persistence.model.Account@3635099
- id=111
- name=null
- type=null
- balance=500.0
- active=null
- updatedAt=2025-08-11T14:24:04.933058
- version=null
```

## Example 3 -- Tuples and Unbound Columns

The columns that belong to the tables and views are automatically populated in the corresponding tuples.

Other SQL expressions, such as renamed table or view columns, general SQL expressions, functions, or columns used in expressions become unbound columns.

The following query:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql
    .select(
      a.id, // in tuple
      a.balance, // in tuple
      a.balance.mult(1.22).as("score"), // outside the tuple -- unbound
      a.name.as("accountNumber") // outside the tuple -- unbound
    )
    .tuples()
    .from(a)
    .where(a.balance.ge(500))
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
=== Account: app.persistence.model.Account@25a94b55
- id=111
- name=null
- type=null
- balance=500.0
- active=null
- updatedAt=null
- version=null
*** Unbound 'score': 500
*** Unbound 'accountNumber': 1072
```

## Example 4 -- Joins

The following query:

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(e)
    .join(b, b.id.eq(e.branchId)).where(e.name.like("%Anne%"))
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

## Example 5 -- Self Joins

The following query:

```java
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple2<Branch, Branch>> rows = this.sql
    .select()
    .tuples()
    .from(b)
    .join(p, p.id.eq(b.parentBranchId))
    .where(b.region.eq("NE")).execute();
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

## Example 6 -- Joining Multiple Tables and Views

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

## Example 7 -- Joining Subqueries and CTEs

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
    .crossJoin(x)
    .crossJoin(y)
    .join(b, b.id.eq(e.branchId).and(b.region.eq(y.str("minRegion"))))
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
=== Employee: app.persistence.model.Employee@cfd1075
- id=32
- name=Jeanne
- branchId=104
=== Branch: app.persistence.model.Branch@45117dd
- id=104
- region=E
- isVip=0
- parentBranchId=null
- createdAt=2024-01-04T12:34:56
```

## Example 8 -- Selecting Using Cursors

The following code looks remarkably similar to using a list. However, it implements streaming of rows:

```java
AccountTable a = this.accountDAO.newTable();

Cursor<Tuple1<Account>> rows = this.sql.select().tuples() //
    .from(a) //
    .where(a.balance.ge(500)) //
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

## Example 9 -- Selecting a Single Row

The following query:

```java
AccountTable a = this.accountDAO.newTable();

Tuple1<Account> row = this.sql.select().tuples() //
    .from(a) //
    .where(a.balance.ge(500)) //
    .executeOne();
Account account = row.getA();
System.out.println("=== Account: " + account);
```

Produces a row with the form:

```txt
=== Account: app.persistence.model.Account@6d672bd4
- id=111
- name=1072
- type=CHK
- balance=500.0
- active=false
- updatedAt=2025-08-11T13:22:43.222488
- version=1
```

If no rows are found then it produces a null. If more than a single row is found the method throws an exception.

# Feature Preview -- Tuples in LiveSQL

## Example 1 -- Equivalent to Select by Criteria

The resulting model objects use the correct data types, converters, etc. The code:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql.select().tuples()
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

## Example 2 -- Filtering Columns

A subset of model object columns can be populated to avoid the overhead of heavy column types (blobs, clobs, long varchar, etc).

The code:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql.select(
    a.id,
    a.balance,
    a.star().filter(c -> c.getType().equals("timestamp"))
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
=== Account: app.persistence.model.Account@6418e39e
- id=111
- name=null
- type=null
- balance=500.0
- active=null
- updatedAt=null
- version=null
```

## Example 3 -- Tuple and Non-tuple Columns

Direct table or view columns are retrieved in the corresponding tuples.

Other expression -- including table or view columns renamed or used in expressions become non-tuple columns, aka "unbound columns".

The code:

```java
AccountTable a = this.accountDAO.newTable();

List<Tuple1<Account>> rows = this.sql.select(
    a.id, // tuple
    a.balance, // tuple
    a.balance.mult(1.22).as("score"), // non-tuple
    a.name.as("altName") // non-tuple
).tuples()
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
=== Account: app.persistence.model.Account@472d0f4
- id=111
- name=null
- type=null
- balance=500.0
- active=null
- updatedAt=null
- version=null
*** Unbound 'score': 500
```

## Example 4 -- Joining Tables and Views

The code:

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql.select().tuples()
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

The code:

```java
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple2<Branch, Branch>> rows = this.sql.select().tuples()
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

## Example 6 -- Joining Multiple Tables and Views (up to 26)

The code:

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple3<Employee, Branch, Branch>> rows = this.sql.select().tuples()
    .from(e).join(b, e.branchId.eq(b.id))
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

The code:

```java
BranchTable br = this.branchDAO.newTable();
CTE y = sql.cte("y", sql.select(sql.min(br.region).as("minRegion")).from(br));

EmployeeTable e = this.employeeDAO.newTable();
Subquery x = sql.subquery("x", sql.select(sql.currentDate().as("currentDate")));
BranchTable b = this.branchDAO.newTable();

List<Tuple2<Employee, Branch>> rows = this.sql.with(y).select().tuples()
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

## Example 8 -- Select Using Cursors

TBD

## Example 9 -- Select a Single Row

TBD

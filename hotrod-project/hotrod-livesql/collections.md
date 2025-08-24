# Feature Preview -- LiveSQL Collections

Bundles can assemble retrieved data rows from SELECT queries into more complex data structures combining collections and associations.

In short:

- A bundle includes one or more model classes for tables or views
- It can also include one or more classess for *associated* tables and views
- Finally, a bundle can include a single collection. The collection itself is also a bundle and, as such, can nest another collection

## Example 1 -- A Bundle with a Collection

The following query includes one base class for branches, each one containing a collection of employees.

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Bundle<Branch, Employee>> rows = this.sql
    .select(
        b.star(),
        e.collection()
    )
    .collections()
    .from(b)
    .semiJoin(e, e.branchId.eq(b.id))
    .orderBy(b.id)
    .execute();

for (Bundle<Branch, Employee> r : rows) {
  Branch branch = r.getA();
  System.out.println("=== Branch: " + branch);
  for (Employee e : r.getCollection()) {
    System.out.println("=== - Employee: " + e);
  }
}
```

## Example 2 -- A Bundle with an Association

The following query includes one base class for employees, each one containing an associated branch.

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();

List<Bundle<Employee, Branch>> rows = this.sql
    .select(
        e.star(),
        b.association()
    )
    .collections()
    .from(e)
    .semiJoin(b, e.branchId.eq(b.id))
    .execute();

for (Bundle<Employee, Branch> r : rows) {
  Employee employee= r.getA();
  Branch branch = r.getB();
  System.out.println("=== Employee: " + employee);
  System.out.println("=== Branch: " + branch);
}
```

## Example 3 -- A Bundle with a Collection and an Association

The following query includes one base class for branches, each one containing a collection of employees.

```java
BranchTable b = this.branchDAO.newTable();
CountryTable c = this.countryDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();

List<Bundle<Branch, Country, Employee>> rows = this.sql
    .select(
        b.star(),
        c.association(),
        e.collection()
    )
    .collections()
    .from(b)
    .semiJoin(c, c.id.eq(b.countryId))
    .semiJoin(e, e.branchId.eq(b.id))
    .orderBy(b.id)
    .execute();

for (Bundle<Branch, Country, Employee> r : rows) {
  Branch branch = r.getA();
  Country country = r.getB();
  System.out.println("=== Branch: " + branch);
  System.out.println("=== Country: " + country);
  for (Employee e : r.getCollection()) {
    System.out.println("=== - Employee: " + e);
  }
}
```

## Example 4 -- Nesting Collections

```java
BranchTable b = this.branchDAO.newTable();
EmployeeTable e = this.employeeDAO.newTable();
AccountTable a = this.accountDAO.newTable();

List<Bundle<Branch, List<Bundle<Employee, Account>>>> rows = this.sql
    .select(
        b.star(),
        e.collection(
            e.star(),
            a.collection()
        )
    )
    .collections()
    .from(b)
    .semiJoin(b, e.branchId.eq(b.id))
    .semiJoin(a, a.id.eq(e.accountId))
    .orderBy(b.id, e.id)
    .execute();

for (Bundle<Branch, List<Bundle<Employee, Account>>> r : rows) {
  Branch branch = r.getA();
  for (Bundle<Employee, Account> b : r.getCollection()) {
    Employee employee = b.getA();
    System.out.println("=== - Employee: " + employee);
    for (Account account : b.getCollection()) {
      System.out.println("=== - Account: " + account);
    }
  }
}
```


## Filtering Columns

## Synthetic Columns

## Indirect Relationships (the 'over' clause)

## Single Bundle

## Cursors

## Example X

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple1<Employee>> rows = this.sql
    .select(
        e.star(),
        e.star().filter(...),

        sql.collection("parentBranches").columns(b.createdAt, p.isVip),
        sql.collection("parentBranches").over(e.name).columns(b.createdAt, p.isVip),

        b.association("currentBranch").columns(b.id, b.region),
        b.collection("parentBranches").over(e.name),
        b.collection("parentBranches").over(e.name).columns(b.id, b.region),

        b.collection("parentBranches").over(e.name).columns(b.id, b.region,
            p.association("parents"),
            sql.collection("items").over(b.id).columns(e.name.concat(p.region).as("code"))
        )

    )
    .collections()
    .from(e)
    .semiJoin(b, e.branchId.eq(b.id))
    .semiJoin(p, p.id.eq(b.parentBranchId))
    .orderBy(e.name, b.id)
    .execute();

for (Tuple1<Employee> r : rows) {
  Employee employee = r.getA();
  System.out.println("=== Employee: " + employee);
}
```

## Model

```
BranchLayout
    ^
    |
BranchModel
    ^
    |
BranchExtended (+ unbound)
```



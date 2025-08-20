# Feature Preview -- Collections


## Example X

```java
EmployeeTable e = this.employeeDAO.newTable();
BranchTable b = this.branchDAO.newTable();
BranchTable p = this.branchDAO.newTable();

List<Tuple1<Employee>> rows = this.sql
    .select(
        e.star(),

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
    .execute();

for (Tuple1<Employee> r : rows) {
  Employee employee = r.getA();
  System.out.println("=== Employee: " + employee);
}
```


# Subqueries

The current version of LiveSQL includes support for subqueries only to implement the `[NOT] IN` 
and `[NOT] EXISTS` operators.


## Using a Subquery in the IN Operator

The following query uses a subquery in the `IN` operator:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
CodesTable c = CodesDAO.newTable("c");

List<Map<String, Object>> rows = this.sql 
    .select()
    .from(e) 
    .where(e.type.notIn(
      this.sql.select(c.type).from(c).where(c.active.eq(1))
    ))
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM employee e
WHERE e.type NOT IN (
  select c.type from codes c where c.active = 1
)
```

## Using a Subquery in the EXISTS Operator

The following query uses a subquery in the `EXISTS` operator. This query is equivalent
to the query above. [^1]

```java
EmployeeTable e = EmployeeDAO.newTable("e");
CodesTable c = CodesDAO.newTable("c");

List<Map<String, Object>> rows = this.sql 
    .select()
    .from(e) 
    .where(this.sql.notExists(
      this.sql.select().from(c).where(c.active.eq(1).and(c.type.eq.e.type))
    ))
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM employee e
WHERE NOT EXISTS (
  select * from codes c where c.active = 1 and c.type = e.type
)
```

[^1]: This query is equivalent to the query above most of the time. In the presence of nulls, however, if may
produce a different result set.


## Tuples

Tuples are implemented using `sql.tuple(<expressions>)` and can be useful for the `IN` operator when 
comparing more than one column. For example:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
CodesTable c = CodesDAO.newTable("c");

List<Map<String, Object>> rows = this.sql 
    .select()
    .from(e) 
    .where(e.tuple(e.type, e.region).notIn(
      this.sql.select(c.type, c.region).from(c).where(c.active.eq(1))
    ))
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM employee e
WHERE (e.type, e.region) NOT IN (
  select c.type, c.region from codes c where c.active = 1
)
```
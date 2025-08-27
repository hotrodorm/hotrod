# HotRod ORM

HotRod 5 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

HotRod brings:

- [CRUD](./hotrod-project/docs/docs-5/crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./hotrod-project/docs/docs-5/livesql/README.md) &mdash; Flexible SQL querying directly from your code
- [Nitro](./hotrod-project/docs/docs-5/nitro/README.md) &mdash; All the power of [Dynamic SQL](./hotrod-project/docs/docs-5/nitro/nitro-dynamicsql.md) and Native SQL when you need it
- [Torcs](./hotrod-project/docs/docs-5/torcs/README.md) &mdash; Detect slow queries at runtime and analyze them

All this functionality is available for any of the [supported databases](./hotrod-project/docs/docs-5/config/supported-databases.md).

See [What's New](./hotrod-project/docs/docs-5/whats-new.md) in HotRod 5, [version history](./hotrod-project/docs/version-history.md),
and the [HotRod 5 Documentation](./hotrod-project/docs/docs-5/README.md).

For documentation on the previous versions see [HotRod 4 Documentation](./hotrod-project/docs/docs-4/README.md) and [HotRod 3 Documentation](./hotrod-project/docs/docs-3/README.md).

See HotRod in action with the [Hello World Example](./hotrod-project/docs/docs-5/guides/hello-world.md).


## LiveSQL &mdash; At a Glance

LiveSQL allows you to write and run queries directly from your application code. LiveSQL's inline syntax only allow valid SQL clauses and expressions.

LiveSQL can run SELECT, UPDATE, DELETE, and INSERT queries from the most basic syntax to advanced queries. The syntax can include complex predicates, subqueries, CTEs, arithmetic operators, functions, as well as standard SQL constructs such as ordering limiting, aggregation, window functions, union, for update (locking), etc.

A basic select with a simple condition can look like:

```java
  List<Employee> employees = this.employeeDAO
    .select(e, e.salary.plus(e.bonus).ge(40000).and(e.title.substring(4, 2).eq("TE")))
    .orderBy(e.hiringDate.desc())
    .execute();
```

Runs the query (in PostgreSQL) as:

```sql
  SELECT *
  FROM employee
  WHERE salary + bonus >= 40000 AND subtring(title, 4, 2) = "TE"
  ORDER BY hiring_date DESC;
```

Behind the scenes LiveSQL automatically adapts the SQL syntax to the specific database.

A more complex query can include joins, CTEs, and much more:

```java
CTE x = sql.cte("x",
    sql.select(b.isVip, a.star())
       .from(b)
       .join(a, a.branchId.eq(b.id)));
CTE y = sql.cte("y", "aid").as(
    sql.select(i.accountId)
       .from(i)
       .join(l, l.invoiceId.eq(i.id))
       .join(p, p.id.eq(l.productId))
       .where(p.shipping.eq(0)));
List<Row> rows = sql
    .with(x, y)
    .select(x.star())
    .from(x)
    .leftJoin(y, y.num("aid").eq(x.num("id")))
    .where(y.num("aid").isNull())
    .execute();
```


## CRUD &mdash; At a Glance

CRUD can access rows by primary keys or by example to execute SELECT, UPDATE, INSERT, and DELETE queries on the tables and view of the schema(s).

Inserting a payment while retrieving the new primary key can be done as:

```java
  Payment p = new Payment();
  p.setClientId(1205);
  p.setPaidAt(LocalDateTime.now());
  p.setAmount(100.00);
  Long id = this.paymentDAO.insert(p);
```

To find an employee by primary key:

```java
  Employee emp = this.employeeDAO.select(134081);
```

CRUD can use complex predicates to find all employees on departments 101 and 120, hired after January 15, 2024, with last names that end with 'SMITH':

```java
  List<Employee> emps = this.employeeDAO.select(e, 
      e.deptId.in(101, 120)
      .and(e.hiredDate.gt(LocalDate.of(2025, 1, 15)))
      .and(e.lastName.upper().like('%SMITH'))
    .execute();
```

Updating the status of an invoice is also simple:

```java
  Invoice inv = this.invoiceDAO.select(5470);
  inv.setStatus("PAID");
  this.invoiceDAO.update(inv);
```


## Nitro &mdash; At a Glance

Nitro excels when the application requires complex, non-trivial queries that go beyond the scope of LiveSQL and CRUD, or for queries that benefit from:

- [Dynamic SQL](./hotrod-project/docs/docs-5/nitro/nitro-dynamicsql.md) logic to dynamically assemble queries based on runtime parameters
- Native SQL extensions available in the specific database

Nitro can be used to gain access to all the features of a database, as well as to squeeze performance from it by tweaking queries. All these features can be combined into any SELECT, UPDATE, INSERT, or DELETE, or in any other valid database query (TRUNCATE, CREATE, ALTER, DROP, etc.).

The following query uses Dynamic SQL to assemble the query dynamically and to apply parameter values to it. It also uses a piece of Native SQL (an optimizer hint):

```xml
<select method="searchVehicles" vo="Vehicle">
  <parameter name="brandName" java-type="String" />
  <parameter name="minYear" java-type="Integer" />
  <parameter name="ordering" java-type="Integer" />
  SELECT /*+ FIRST_ROWS(10) */ *
  FROM vehicle
  where brand like = '%' || #{brandName} || '%'
    <if test="minYear != null">AND year >= #{minYear}</if>
  <choose>
    <if test="ordering == 1">ORDER BY price</if>
    <if test="ordering == 2">ORDER BY price DESC</if>
    <if test="ordering == 3">ORDER BY avg_reviews DESC</if>
  </choose>
</select>
```

Nitro makes this query available in your application as the method:

```java
  List<Vehicle> searchVehicles(String brandName, Integer minYear, Integer ordering)
```

## Torcs &mdash; At a Glance

Torcs discovers slow queries at runtime by providing rankings by impact, slowest response time, high frequency, and others. The visibility that Torcs offers about the actual running application can provide critical information to understand the bottlenecks of the application that need to be addressed.

For example, the ranking by highest response time starts automatically when Torcs is added to the application and can provide a ranking of queries like:

| Rank | Execs | Errors | Avg Time (ms) | Observed Time (ms) | Impact (ms) | Data Source | SQL |
| :--: | --:| --:| --:| --:| --:| :--: | :-- |
| #1 | 4    |   0 |       47 | 38-55      | 188         | #0 | SELECT amount FROM invoice WHERE client_id = ? ORDER BY created_at |
| #2 | 12   |   0 |        6 | 4-10        | 87         | #0 | SELECT status FROM client WHERE id = ? |
| #3 | 5    |   1 |        8 | 7-9         | 42         | #0 | SELECT name FROM branch WHERE id = ? |

The following rankings are built-in in Torcs and can be activated programmatically. When multiple rankings are active, each one keeps its own separate data:

- By Highest Response Time (active by default)
- By Highest Impact
- By Highest Frequency
- Initial Queries
- Latest Queries

Torcs can also retrieve execution plans programmatically in a variety of formats for specific queries that require attention.



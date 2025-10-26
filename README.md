# HotRod ORM

HotRod 5 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

HotRod brings:

- [CRUD](./hotrod-project/docs/docs-5/crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./hotrod-project/docs/docs-5/livesql/README.md) &mdash; Flexible SQL querying from your code to retrieve plain values and also full table entities
- [Nitro](./hotrod-project/docs/docs-5/nitro/README.md) &mdash; All the power of Dynamic SQL and Native SQL when you need it
- [Torcs](./hotrod-project/docs/docs-5/torcs/README.md) &mdash; Detect slow queries at runtime and analyze them

Get started with the [Hello World](./hotrod-project/docs/docs-5/guides/hello-world.md) example and other [Hello World Examples](./hotrod-project/docs/docs-5/README.md#examples).

See [What's New](./hotrod-project/docs/docs-5/whats-new.md) in HotRod 5, [version history](./hotrod-project/docs/version-history.md),
[roadmap](./hotrod-project/docs/roadmap.md),
and the [HotRod 5 Documentation](./hotrod-project/docs/docs-5/README.md). 
All this functionality is available for any of the [supported databases](./hotrod-project/docs/docs-5/config/supported-databases.md).

For documentation on the previous versions see the
[HotRod 4 Documentation](./hotrod-project/docs/docs-4/README.md) and the [HotRod 3 Documentation](./hotrod-project/docs/docs-3/README.md).



## LiveSQL &mdash; At a Glance

[LiveSQL](./hotrod-project/docs/docs-5/livesql/README.md) allows you to write and run queries directly from your application code. LiveSQL's inline syntax only allow valid SQL clauses and expressions.

LiveSQL can run SELECT, UPDATE, DELETE, and INSERT queries from the most basic syntax to advanced queries. The syntax can include complex predicates, subqueries, CTEs, arithmetic operators, functions, as well as standard SQL constructs such as ordering limiting, aggregation, window functions, union, for update (locking), etc.

A simple SELECT query to compute the expression `3 * 7` in the database can be written as:

```java
Row row = sql.select(sql.val(3).mult(7).as("total")).executeOne();
System.out.println("total=" + row.get("total")); // total=21
```

Selecting from a table can be done as:

```java
List<Tuple1<Product>> products = this.sql
  .select(p.star(), p.shipping.plus(p.tax).as("totalCost"))
  .tuples()
  .from(p)
  .where(p.type.eq("SPORTS").and(p.promotion.eq("BOGO")))
  .orderBy(p.shipping.desc())
  .limit(50)
  .execute();
```

Joining multiple tables and views can be done as:

```java
List<Tuple2<Invoice, Client>> rows = sql
  .select(i.star(), c.star(), i.amount.mult(c.discount).as("appliedDiscount"))
  .tuples()
  .from(i)
  .join(c, c.id.eq(i.clientId))
  .where(c.branchName.upper().like("%SOUTH%"))
  .orderBy(c.id, i.purchaseDate.desc())
  .execute();

for (Tuple2<Invoice, Client> r : rows) {
  Invoice inv = r.getA() // all columns correctly named, cast, typed, and/or converted here
  Client cli = r.getB(); // same here
  System.out.println("Invoice: " + inv);
  System.out.println("Client: " + cli);
  System.out.println("Applied Discount: " + r.getUnbound().get("appliedDiscount"));
}
```

Behind the scenes LiveSQL automatically adapts the SQL syntax to the specific database.

Any query can include joins, CTEs, subqueries, and other syntax, as shown below:

```java
CTE x = sql.cte("x",
    sql.select(b.isVip, a.star())
       .from(b)
       .join(a, a.branchId.eq(b.id))
    );
Subquery y = sql.subquery("y",
    sql.select(i.accountId.as("aid"))
       .from(i)
       .join(l, l.invoiceId.eq(i.id))
       .join(p, p.id.eq(l.productId))
       .where(p.shipping.eq(0))
    );
List<Row> rows = sql
    .with(x)
    .select(x.star())
    .from(x)
    .leftJoin(y, y.num("aid").eq(x.num("id")))
    .where(y.num("aid").isNull())
    .execute();
```


## CRUD &mdash; At a Glance

[CRUD](./hotrod-project/docs/docs-5/crud/README.md) provides a straightforward repertoire of database access methods that can access rows by primary keys, by example, or by predicates to execute SELECT, UPDATE, INSERT, and DELETE queries on the tables and view of the schema(s).

Inserting a payment while retrieving the new primary key (in the column ID) can be done as:

```java
Payment p = new Payment();
p.setClientId(1205);
p.setPaidAt(LocalDateTime.now());
p.setAmount(100.00);
Payment inserted = this.paymentDAO.insert(p);
System.out.println("ID: " + inserted.getId()); // generated ID
```

To find an employee by primary key:

```java
Employee emp = this.employeeDAO.select(134081);
```

CRUD can use custom predicates, for example, to find all employees on departments 101 and 120, hired after January 15, 2024, with last names that end with 'SMITH':

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

[Nitro](./hotrod-project/docs/docs-5/nitro/README.md) excels when the application requires complex, non-trivial queries that go beyond the scope of LiveSQL and CRUD, or for queries that benefit from:

- [Nitro Dynamic SQL](./hotrod-project/docs/docs-5/nitro/nitro-dynamicsql.md) logic to dynamically assemble queries based on runtime parameters
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

[Torcs](./hotrod-project/docs/docs-5/torcs/README.md) discovers slow queries at runtime by computing rankings by impact, response time, execution frequency, and others. The visibility that Torcs offers about the actual running application can provide critical information to understand the bottlenecks of the application that need to be addressed.

For example, the ranking by highest response time starts automatically when Torcs is added to the application and can provide a ranking of queries in the form:

| Rank | Execs | Errors | Min Time (ms) | Avg Time (ms) | Max Time (ms) | Impact (ms) | Data Source | SQL |
| :--: | --:| --:| --:| --:| --:| --:| :--: | :-- |
| #1 | 4    |   0 | 38 |       47 | 55      | 188         | #0 | SELECT amount FROM invoice WHERE client_id = ? ORDER BY created_at |
| #2 | 12   |   0 | 4 |        6 | 10        | 87         | #0 | SELECT status FROM client WHERE id = ? |
| #3 | 5    |   1 | 7 |        8 | 9         | 42         | #0 | SELECT name FROM branch WHERE id = ? |

In this example the entries are sorted by max time in descending order.

The following rankings are built-in in Torcs and can be activated programmatically. When multiple rankings are active, each one keeps its own separate data:

- By Highest Response Time (active by default)
- By Highest Impact
- By Highest Frequency
- Initial Queries
- Latest Queries

Torcs can also retrieve execution plans programmatically in a variety of formats for any of the queries in the rankings.



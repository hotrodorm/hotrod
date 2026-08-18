# HotRod ORM Suite

HotRod 5 is an open-source Object-Relational Mapping (ORM) suite of products designed for Spring and Spring Boot, focused on rapid development and high-performance persistence in relational databases.

## HotRod Products

As of version 5 the HotRod Suite includes:

- [CRUD](./hotrod-project/docs/docs-5/crud/README.md) &mdash; Start prototyping your application in minutes with the straightforward persistence included in CRUD
- [LiveSQL](./hotrod-project/docs/docs-5/livesql/README.md) &mdash; Use flexible SQL querying directly from your code with live syntax validation
- [Nitro](./hotrod-project/docs/docs-5/nitro/README.md) &mdash; Harness the power of native SQL and dynamic SQL when necessary
- [Torcs](./hotrod-project/docs/docs-5/torcs/README.md) &mdash; Identify slow queries at runtime and retrieve their execution plans
- [Torcs CTP](./hotrod-project/docs/docs-5/torcs-ctp/README.md) &mdash; Generate execution plans of slow queries and visualize them in [Check The Plan](https://checktheplan.com)

Get started with the [Hello World](./hotrod-project/docs/docs-5/guides/hello-world.md) example and explore additional [Hello World Examples](./hotrod-project/docs/docs-5/README.md#examples).

The [HotRod 5 Documentation](./hotrod-project/docs/docs-5/README.md) covers all functionalities across the [Supported Databases](./hotrod-project/docs/docs-5/config/supported-databases.md). See the [Pamphlet](./hotrod-project/docs/pamphlet.md), learn about [What's New](./hotrod-project/docs/docs-5/whats-new.md) in HotRod 5, review the [Version History](./hotrod-project/docs/version-history.md), and the [Roadmap](./hotrod-project/docs/roadmap.md). 

For documentation on the previous versions see the
[HotRod 4 Documentation](./hotrod-project/docs/docs-4/README.md) and the [HotRod 3 Documentation](./hotrod-project/docs/docs-3/README.md).

## LiveSQL &mdash; At a Glance

[LiveSQL](./hotrod-project/docs/docs-5/livesql/README.md) enables you to write and execute queries directly from your application code. Its inline syntax ensures that only valid SQL clauses and expressions are permitted.

LiveSQL supports SELECT, UPDATE, DELETE, and INSERT queries, ranging from basic syntax to advanced queries. The syntax accommodates complex predicates, subqueries, Common Table Expressions (CTEs), arithmetic operators, functions, as well as standard SQL constructs such as ordering, limiting, aggregation, window functions, unions, and locking with FOR UPDATE.

A simple SELECT query to compute the expression 3 * 7 in the database can be written as follows:

```java
Row row = sql.select(sql.val(3).mult(7).as("total")).executeOne();

System.out.println("total=" + row.get("total")); // total=21
```

You can select from a table using the following syntax:

```java
List<Tuple1<Product>> rows = sql
  .select(p.star(), p.shipping.plus(p.tax).minus(p.discount).as("net"))
  .tuples()
  .from(p)
  .where(p.shipping.plus(p.tax).minus(p.discount).le(10))
  .orderBy(p.category, p.name.desc())
  .limit(50)
  .execute();

for (Tuple1<Product> r : rows) {
  Product prod = r.getA(); // all columns correctly named, cast, typed, and/or converted here
  System.out.println("Product: " + prod);
  System.out.println("Net: " + r.get("net"));
}
```

You can join multiple tables, views, subqueries, and/or Common Table Expressions (CTEs). For example, to join the tables INVOICE and CLIENT in a SELECT query &mdash; and get separate tuples for each table &mdash; you can do:

```java
List<Tuple2<Invoice, Client>> rows = sql
  .select(i.star(), c.star(), i.amount.mult(c.discountPct).as("appliedDiscount"))
  .tuples()
  .from(i)
  .join(c, c.id.eq(i.clientId))
  .where(c.branchName.upper().like("%SOUTH%"))
  .orderBy(c.id, i.purchaseDate.desc())
  .execute();

for (Tuple2<Invoice, Client> r : rows) {
  Invoice inv = r.getA(); // all columns correctly named, cast, typed, and/or converted here
  Client cli = r.getB();  // same here
  System.out.println("Invoice: " + inv);
  System.out.println("Client: " + cli);
  System.out.println("Applied Discount: " + r.get("appliedDiscount"));
}
```

Behind the scenes LiveSQL automatically adapts the SQL syntax to the specific database.

Any SELECT query can incorporate joins, Common Table Expressions (CTEs), subqueries, and other syntax, as illustrated below:

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

[CRUD](./hotrod-project/docs/docs-5/crud/README.md) offers a straightforward set of database access methods, allowing you to access rows by primary keys, examples, or predicates. This enables the execution of SELECT, UPDATE, INSERT, and DELETE queries on the tables and views in the database schemas.

To find an employee by primary key, you can use:


```java
Employee emp = this.employeeDAO.select(134081);
```

Inserting a payment while retrieving the new primary key from the ID column can be accomplished using the following syntax:

```java
Payment p = new Payment();
p.setClientId(1205);
p.setPaidAt(LocalDateTime.now());
p.setAmount(100.00);
Payment inserted = this.paymentDAO.insert(p);
System.out.println("ID: " + inserted.getId()); // generated ID
```

CRUD can also utilize custom predicates. For instance, you can find all employees in departments 101 and 120 who were hired after January 15, 2024, and have last names ending with "SMITH" using the following query:


```java
List<Employee> emps = this.employeeDAO.select(e,
    e.deptId.in(101, 120)
    .and(e.hiredDate.gt(LocalDate.of(2024, 1, 15)))
    .and(e.lastName.upper().like('%SMITH'))
  .execute();
```

Updating the status of an invoice is straightforward:

```java
Invoice inv = this.invoiceDAO.select(5470);
inv.setStatus("PAID");
int count = this.invoiceDAO.update(inv);
```


## Nitro &mdash; At a Glance

[Nitro](./hotrod-project/docs/docs-5/nitro/README.md) excels when an application requires complex, non-trivial queries that surpass the capabilities of LiveSQL and CRUD. That is, when you need to:

- Run any SQL statement beyond SELECT, INSERT, UPDATE, and DELETE
- Use [Dynamic SQL](./hotrod-project/docs/docs-5/nitro/nitro-dynamicsql.md) to include or exclude query sections according to the runtime parameters
- Use native SQL extensions available in the specific database, such as custom functions, full text search, rollups, optimizer hints, regular expression matching, etc
- Expose long, complex, and tedious queries as simple methods in your app
- Use pre-existent and well-tested queries "as is" from your application

Nitro can be used to gain access to all the features available in the specific SQL dialect offered by the database, as well as to squeeze performance from it by tweaking queries. These features can be combined in any SELECT, UPDATE, INSERT, or DELETE query, as well as in other valid database query such as CREATE, ALTER, or DROP.

A basic query without parameters can be specified as follows:

```xml
<query method="initializeBatchProcess">
  TRUNCATE tmp_accounting
</query>
```

This query becomes available in the persistence layer as the bean method:

```java
int initializeBatchProcess()
```

The following query has parameters and uses Dynamic SQL to assemble the query dynamically. Depending on the specific runtime values, the query will take on a different structure each time. It also incorporates a piece of Native SQL &mdash; an optimizer hint:

```xml
<select method="searchVehicles" vo="Vehicle">
  <parameter name="brandName" type="String" />
  <parameter name="minYear" type="Integer" />
  <parameter name="ordering" type="Integer" />
  SELECT /*+ FIRST_ROWS(10) */ *
  FROM vehicle
  WHERE brand like = '%' || #{brandName} || '%'
  <if test="minYear != null">AND year >= #{minYear}</if>
  <choose>
    <when test="ordering == 1">ORDER BY price</when>
    <when test="ordering == 2">ORDER BY price DESC</when>
    <when test="ordering == 3">ORDER BY avg_reviews DESC</when>
    <otherwise>ORDER BY purchase_date</otherwise>
  </choose>
</select>
```

Nitro makes this query available in the persistence layer as the bean method:

```java
List<Vehicle> searchVehicles(String brandName, Integer minYear, Integer ordering)
```

## Torcs &mdash; At a Glance

[Torcs](./hotrod-project/docs/docs-5/torcs/README.md) discovers slow queries at runtime by computing rankings by impact, response time, execution frequency, and others factors. The insights that Torcs provides into the actual running application can be crucial for pinpointing bottlenecks that need to be addressed.

For example, the ranking by highest response time starts automatically when Torcs is integrated into the application, providing a ranking of queries in the following format:

| Rank | Execs | Errors | Min Time (ms) | Avg Time (ms) | Max Time (ms) | Impact (ms) | Data Source | SQL |
| :--: | --:| --:| --:| --:| --:| --:| :--: | :-- |
| #1 | 4    |   0 | 38 |       47 | 55      | 188         | #0 | SELECT amount FROM invoice WHERE client_id = ? ORDER BY created_at |
| #2 | 12   |   0 | 4 |        6 | 10        | 87         | #0 | SELECT status FROM client WHERE id = ? |
| #3 | 5    |   1 | 7 |        8 | 9         | 42         | #0 | SELECT name FROM branch WHERE id = ? |

In this example the entries are sorted by max time in descending order.

The following rankings are built-in in Torcs and can be activated programmatically. When multiple rankings are active, each one maintains its own separate statistics:

- By Highest Response Time (active by default)
- By Highest Impact
- By Highest Frequency
- Initial Queries
- Latest Queries

Torcs can also programmatically retrieve execution plans in various formats for any of the queries in the rankings. For instance, the default format in Oracle allows you to obtain the execution plan for a query of interest as follows:

```log
Plan hash value: 3305857414

----------------------------------------------------------------------------------------------
| Id  | Operation                     | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT              |              |     1 |   118 |     5  (20)| 00:00:01 |
|   1 |  SORT ORDER BY                |              |     1 |   118 |     5  (20)| 00:00:01 |
|   2 |   NESTED LOOPS                |              |     1 |   118 |     4   (0)| 00:00:01 |
|   3 |    NESTED LOOPS               |              |     1 |   118 |     4   (0)| 00:00:01 |
|*  4 |     TABLE ACCESS FULL         | INVOICE      |     1 |    84 |     3   (0)| 00:00:01 |
|*  5 |     INDEX UNIQUE SCAN         | SYS_C0016733 |     1 |       |     0   (0)| 00:00:01 |
|*  6 |    TABLE ACCESS BY INDEX ROWID| BRANCH       |     1 |    34 |     1   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   4 - filter("I"."STATUS"<>:2 AND "I"."AMOUNT">=:3)
   5 - access("B"."ID"="I"."BRANCH_ID")
   6 - filter("B"."REGION"=:1)

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
```



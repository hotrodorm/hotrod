# HotRod 5 ORM

HotRod 5 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

The persistence layer provides ready-to-use CRUD and LiveSQL functionalities to quickly start prototyping an application for
any of the world-class [supported databases](./config/supported-databases.md).

See [What's New](./whats-new.md) in HotRod 5 and the [Version History](../version-history.md).

## LiveSQL

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

A more complex search condition that includes EXISTS and a subquery, may take the following form:

```java
  List<Employee> employees = this.employeeDAO
    .select(e, e.type.ne("MANAGER").and(sql.exists(
        sql.select().from(m).where(m.branchId.ne(e.branchId).and(m.name.eq(e.name)))
      ))
    )
    .orderBy(e.branchId, e.salary.plus(e.bonus).desc())
    .execute();
```

LiveSQL extensive SQL syntax can express complex expressions (of numeric, string, date/time, boolean, and binary types), functions, joins, aggregations, window functions, ordering, limiting, multilevel subqueries, multilevel set operators, traditional and recursive CTEs, lateral queries, etc.

A simple join can look like:

```java
  List<Row> rows = sql
    .select(e.star(), b.name.as("branchName"))
    .from(e)
    .join(b, b.id.eq(e.branchId))
    .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
    .orderBy(b.name, e.lastName.desc())
    .limit(50)
    .execute();
```

While a more complex query can look like:

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


## The Simplicity of CRUD

Additionally, the out-of-the-box CRUD methods available in the DAOs can access rows by primary keys or by example. SELECT, UPDATE, INSERT, and DELETE methods are automatically included in the CRUD persistence layer.

For example, to find an employee by primary key:

```java
  Employee emp = this.employeeDAO.select(134081);
```

To update the status of an invoice:

```java
  Invoice inv = this.invoiceDAO.select(5470);
  inv.setStatus("PAID");
  this.invoiceDAO.update(inv);
```


## The Power of Nitro

Nitro queries enhance SQL capabilities with:

- [Nitro Dynamic SQL](./nitro/nitro-dynamicsql.md)
- Native SQL

Nitro can be used to gain access to all the features of a database, as well as to squeeze performance from it by tweaking queries. Any or all of these features can be combined into any SELECT, UPDATE, INSERT, or DELETE, or in any other valid database query (TRUNCATE, CREATE, ALTER, DROP, etc.).

The following query uses Dynamic SQL to assemble the query dynamically and to *apply* parameter values to it. It also uses a piece of Native SQL (an optimizer hint):

```xml
<select method="searchVehicles" vo="Vehicle">
  <parameter name="brandName" java-type="String" />
  <parameter name="minYear" java-type="Integer" />
  <parameter name="ordering" java-type="Integer" />
  select /*+ FIRST_ROWS(10) */ *
  from vehicle
  where brand like = '%' || #{brandName} || '%'
    <if test="minYear != null">and year >= #{minYear}</if>
  <choose>
    <if test="ordering == 1">order by price</if>
    <if test="ordering == 2">order by price DESC</if>
    <if test="ordering == 3">order by avg_reviews DESC</if>
  </choose>
</select>
```

Nitro makes this query available in Java as:

```java
  List<Vehicle> searchVehicles(String brandName, Integer minYear, Integer ordering)
```

## Torcs

Torcs discovers slow queries at runtime by providing rankings by impact, slowest response time, high frequency, and others. The visibility that Torcs offers about the actual running application can provide critical information to understand the bottlenecks of the application that need to be addressed.


## Hello World

See HotRod in action with the [Hello World](./guides/hello-world.md) example.


## Modules

The HotRod functionality is divided into modules that serve different purposes. From simple
out-of-the-box functionality available in CRUD and LiveSQL to advanced querying capabilities provided by Nitro and query optimization with Torcs and Torcs CTP.

It includes:

- [CRUD](./crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](./nitro/README.md) &mdash; All the power of [Nitro Dynamic SQL](./nitro/nitro-dynamicsql.md) and Native SQL when you need it
- [Torcs](./torcs/README.md) &mdash; Detect slow queries at runtime and analyze them
- [Torcs CTP](./torcs-ctp/README.md) &mdash; Generate execution plans of slow queries and visualize them in *Check The Plan*

Last but not least, the [DynamicSQL](./dynamicsql/README.md) component is used behind the scenes to define, implement, and execute Dynamic SQL queries.

## Examples

Explore the following examples to quickly see the functionality in action:

- [Hello CRUD](./guides/hello-crud.md) - Basic CRUD examples
- [Hello LiveSQL](./guides/hello-livesql.md) - The basic LiveSQL queries
- [Hello Nitro](./guides/hello-nitro.md) - The different forms of Nitro queries
- [Hello DynamicSQL](./guides/hello-dynamicsql.md) - See Dynamic SQL in action
- [Hello Torcs](./guides/hello-torcs.md) - Single out slow queries
- [Hello Type Resolution](./guides/hello-type-resolution.md) - See all cases of the full type resolution
- [Hello Identifiers](./guides/hello-identifiers.md) - Shows how to use non-alphanumeric ASCII table and column names
- [Hello Logging](./guides/hello-logging.md) - Enable query execution logs for DAOs and LiveSQL

## Reference

Even though prototyping with simple functionality works out of the box, more advanced features need to be enabled by configuration.

The reference details how to activate all features, and how to use them.

- [Configuration File Reference](./config/README.md)
- [Libraries](./config/libraries.md)
- [Maven Integration](./maven/README.md)
- [Supported Databases and Default Data Types](./config/supported-databases.md)


## Guides

These are step by step guides to use specific features or functionality in the HotRod ORM.

Getting Started:

- [Hello World!](./guides/hello-world.md)
- [Starting a Maven Project from Scratch](./guides/starting-a-maven-project-from-scratch-with-postgresql.md)

Tuning the Persistence Layer:

- [Organizing the Persistence Layer Folders](./guides/organizing-persistence-layer-folders.md)
- [Tuning Column Types](./guides/type-resolution-mechanics.md)
- [Exporting Column Properties](./guides/exporting-column-properties.md)
- [Mapping Table, Views, and Column Names](./guides/mapping-table-and-column-names.md)
- [Using Schema Discovery](./guides/schema-discovery.md)

Debugging:

- [Previewing LiveSQL](./livesql/previewing-livesql.md)
- [Enabling SQL Logging](./guides/enabling-sql-logging.md)

Advanced Features:

- [Using Multiple DataSources](./guides/using-multiple-datasources.md)
- [Extending LiveSQL Functions](./livesql/extending-livesql-functions.md)
- [Using Ant - Java 8 Only](./guides/hello-world-ant.md)



# HotRod ORM

HotRod 4 is an open source ORM for Spring and Spring Boot geared toward high performance persistence for relational databases.

The persistence layer provides CRUD and LiveSQL functionalities to quickly start prototyping an application.

See [What's New](./hotrod-project/docs/docs-4/whats-new.md) in HotRod 4, [Version History](./hotrod-project/docs/version-history.md), and the [Roadmap](./hotrod-project/docs/roadmap.md). For documentation on the previous version see [HotRod 3 Documentation](./hotrod-project/docs/docs-3.4/README.md).


## The Simplicity of CRUD

The out-of-the-box CRUD methods can access rows by primary keys, foreign keys, or by example. SELECT, UPDATE, INSERT, and DELETE methods are automatically included in the CRUD persistence layer.

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


## The Flexibility of LiveSQL

LiveSQL can express SELECT, UPDATE, DELETE, and INSERT queries. A basic select with a simple condition
can look like:

```java
  List<Employee> employees = this.employeeDAO
    .select(e, e.salary.plus(e.bonus).ge(40000).and(e.type.substring(2, 3).eq("ASC")))
    .execute();
```

The criteria can include parenthesis, complex predicates, subqueries, etc. The query can include ordering and limiting as well. A more complex search condition that includes EXISTS and a subquery could take the form:

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
        .join(a, a.branchId.eq(b.id))
);

CTE y = sql.cte("y", "aid").as(sql.select(i.accountId)
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

## The Power of Nitro

Nitro queries enhance SQL capabilities with dynamically assembled SQL queries, graph queries and access to native SQL. Any or all of these features can be combined into any SELECT, UPDATE, INSERT, or DELETE, as well as to any valid database query.

The following query uses Dynamic SQL to assemble the query dynamically and to *apply* parameter values to it. It also uses a piece of Native SQL (an optimizer hint):

```xml
  <select method="searchVehicles" vo="Vehicle">
    <parameter name="brandName" java-type="String" />
    <parameter name="minYear" java-type="Integer" />
    <parameter name="ordering" java-type="Integer" />
    select /*+ FIRST_ROWS(10) */ *
    from vehicle
    where brand like = '%' || ${brandName} || '%'
      <if test="minYear != null">and year >= #{minYear}</if>
    <choose>
      <if test="ordering == 1">order by price</if>
      <if test="ordering == 2">order by price DESC</if>
      <if test="ordering == 3">order by avg_reviews DESC</if>
    </choose>
  </select>
```

Nitro makes this query available to be called in Java as:

```java
  List<Vehicle> searchVehicles(String brandName, Integer minYear, Integer ordering)
```

Graph queries assemble the rows and columns of SELECT queries into trees of objects. For example, the following query:

```xml
  <select method="searchInvoices">
    <parameter name="customerId" java-type="Integer" />
    <parameter name="minAmount" java-type="Integer" />
    select
      <columns>
        <vo table="invoice" extended-vo="InvoiceWithLines" alias="i">
          <association table="customer" property="customer" alias="c" />
          <collection table="invoice_line" property="lines" alias="il" />
        </vo>
      </columns>
    from invoice i
    join customer c on c.id = i.customer_id
    join invoice_line il on il.invoice_id = i.id
    where i.customer_id = ${customerId}
      <if test="minAmount != null">and i.amount >= ${minAmount}</if>
    order by i.id
  </select>
```

Returns a list of `InvoiceWithLines` objects. It returns one of these first-level objects for each invoice. Each first-level object includes `customer` property for the a second-level `Customer` object (1:1 cardinality) that holds the data coming from the `customer` table. It also includes a `lines` property that includes the list of second-level `InvoiceLine` objects (1:N cardinality) with their corresponding properties.

The query is available in Java as:

```java
  List<InvoiceWithLines> searchInvoices(Integer customerId, Integer minamount)
```


## Hello World

See HotRod in action with the [Hello World Example](./hotrod-project/docs/docs-4/guides/hello-world.md). It's an example that shows the simplicity of HotRod.


## Modules

The HotRod functionality is divided into five modules serving different purposes. From simple
out-of-the-box functionality available in CRUD and LiveSQL to advanced querying capabilities provided by Nitro and query optimization with Torcs and Torcs CTP.

These modules are:

- [CRUD](./hotrod-project/docs/docs-4/crud/README.md) &mdash; Quick and simple persistence for rapid prototyping
- [LiveSQL](./hotrod-project/docs/docs-4/livesql/README.md) &mdash; Flexible querying from Java
- [Nitro](./hotrod-project/docs/docs-4/nitro/README.md) &mdash; All the power of [Dynamic SQL](./hotrod-project/docs/docs-4/nitro/nitro-dynamic-sql.md) combined with [Graph Queries](./hotrod-project/docs/docs-4/nitro/nitro-graph-selects.md) and Native SQL when you need it
- [Torcs](./hotrod-project/docs/docs-4/torcs/README.md) &mdash; Detect slow queries at runtime and analyze them
- [Torcs CTP](./hotrod-project/docs/docs-4/torcs-ctp/README.md) &mdash; Generate execution plans of slow queries and visualize them in Check The Plan

## Examples

See [Hello CRUD!](./hotrod-project/docs/docs-4/crud/hello-crud.md) for the out-of-the-box functionality and its basic setup and to run other CRUD examples. 

<!--
For the basic usage of each module and more examples of each one see:
 - [Hello CRUD]() &amp; more [CRUD Examples]().
 - [Hello LiveSQL]() &amp; more [LiveSQL Examples]().
 - [Hello Nitro]() &amp; more [Nitro Examples]().
 - [Hello Torcs]() &amp; more [Torcs Examples]().
 - [Hello Torcs CTP]() &amp; more Torcs CTP Examples]().
-->


## Reference

Even though prototyping with simple functionality works out of the box, more advanced features need to be enabled by configuration.

The reference details how to activate all features, and how to use them.

- [Configuration File Reference](./hotrod-project/docs/docs-4/config/README.md)
- [Cheat Sheet](./hotrod-project/docs/docs-4/cheat-sheet.md)
- [Libraries](./hotrod-project/docs/docs-4/config/libraries.md)
- [Maven Integration](./hotrod-project/docs/docs-4/maven/README.md)
- [Supported Databases and Default Data Types](./hotrod-project/docs/docs-4/config/supported-databases.md)


## Guides

These guides focus on specific features and show examples enabling and using them.

These guides cover:

- [Schema Discovery](./hotrod-project/docs/docs-4/guides/schema-discovery.md)
- [Using Multiple DataSources](./hotrod-project/docs/docs-4/guides/using-multiple-datasources.md)
- [Custom Column Types](./hotrod-project/docs/docs-4/guides/mapping-column-types.md)
- [Custom Table and Column Names](./hotrod-project/docs/docs-4/guides/mapping-table-and-column-names.md)
- [Extending LiveSQL Functions](./hotrod-project/docs/docs-4/livesql/extending-livesql-functions.md)
- [Previewing LiveSQL](./hotrod-project/docs/docs-4/livesql/previewing-livesql.md)
- [Starting a Spring Boot Project from Scratch using PostgreSQL](./hotrod-project/docs/docs-4/guides/starting-a-maven-project-from-scratch-with-postgresql.md)
- [Automate Project Creation with One Command](./hotrod-project/docs/docs-4/maven/maven-arquetype.md)
- [Enabling SQL Logging](./hotrod-project/docs/docs-4/guides/enabling-sql-logging.md)


## Features Overview

HotRod follows the *Database First* approach. It assumes the database already exists with all tables ready. HotRod inspects the database and produces a fully functional
Spring persistence layer.

In a nutshell HotRod's features are:

- Straightforward CRUD and LiveSQL persistence layer for quick prototyping
- Default, custom, and rule-based data types
- Default, custom, and rule-based object names
- Support for major relational databases
- Flexible querying from Java using LiveSQL
- Index-aware persistence layer. Indexes shape the persistence functionality
- Auto-discovery of tables and views that can be used by CRUD and LiveSQL off-the-shelf
- Domain objects can be extended with custom properties and custom methods
- Seamlessly applies database changes to the data model, without losing custom properties or methods
- Includes powerful Dynamic SQL for more demanding needs
- Implements Optimistic Locking for concurrency control on a per-table basis
- Full access to Native SQL when needed
- Graph queries load data in data structure trees rather than plain list of rows
- Implements cursors to process queries with minimal memory usage
- Implements enum dimension-like tables to reduce joins and database query cost
- Works seamlessly with database views
- Implements converters for a richer modeling of the data
- Fully integrated with Maven builds, Maven persistence layer generation, and Maven arquetype
- Tailored for Spring and String Boot projects to gain access to all their 
features such as transaction management and AOP


# Nitro

The Nitro module assists the developer on writing custom configuration-assisted queries.

Nitro goes beyond the CRUD functionality and allows the developer to execute native queries in the database.
These queries can be plain native queries or configuration-assisted queries that take advantage of the knowledge
the CRUD module has on the database to simplify their writing. These queries are added to the DAOs in the
persistence layer and are exposed as Java methods.

Nitro queries can be grouped into three categories:

- **General Purpose Queries**: Queries that return no data.
- **Flat Selects**: Traditional SELECTs that allow native SQL and Dynamic SQL.
- **Structured Selects**: Advanced SELECTs that return graphs of objects. Native SQL and Dynamic SQL are also fully supported.

Nitro queries' goal is to provide an option to gain access to native database performance all the while simplifying the coding.

The main features of Nitro queries are:

- Reaching for High Performance: By using all native extensions of the SQL dialect that the database implements, a developer with knowledge of SQL
optimization can create queries that can achieve high performance.
- Make the most of the CRUD model: Nitro queries make use of the CRUD model to allow configuration-assisted  queries that
can retrieve data into non-trivial data structures.
- Dynamic SQL: Dynamic SQL allows queries to enable or disable sections of the query based on runtime parameters. 
See [Dynamic SQL](nitro-dynamic-sql.md). 


## General Purpose Queries &mdash; The `<query>` Tag

A [General Purpose Query](nitro-general-purpose.md) does not return a result set and is implemented using `<query>` tag. It may return optional count of rows.

They are commonly used to perform changes in the database -- by running tailored UPDATE or DELETE statements -- but can actually run any SQL statement, including DML statements and stored procedures calls.

For example, a typical DML query could look like:

```xml
<query method="closeFullyPaidInvoices">
  update invoice
  set outstanding = 0
  where amount_paid >= amount_receivable
</query>
```

Nevertheless, any valid query can be used. For example:

```xml
<query method="initializeDailyTransactions">
  truncate daily_transactions_tbl
</query>
```

The above two queries return the number of affected rows and are exposed to the developer as Java methods:

```java
    public int closeFullyPaidInvoices() { ... }
    
    public int initializeDailyTransactions() { ... }
```

They can include the full native SQL language available on the database, as well as hints and non-standard features. They can also accept parameters.

General queries are exposed as Java methods with parameters in the DAO where they are defined. 

## Flat Selects &mdash; The `<select>` Tag

A [Flat Select Query](nitro-flat-selects.md) executes a SQL `SELECT` statement and models the resulting rows as value objects with fully defined property names and property types. The execution of a flat select typically returns a `java.util.List` of these value objects.

For example:

```xml
<select method="findActiveAccountsWithClient" vo="AccountClientVO">
  <parameter name="regionId" java-type="Integer" />
  select a.*, c.name, c.type as "client_type"
  from account a
  join client c on c.id = a.client_id
  where a.active = 1 and c.region_id = #{regionId}
</select>
```

The above query returns a list value object and is exposed to the developer as the Java method:

```java
public List<AccountClientVO> findActiveAccountsWithClient(Integer regionId) { ... }
```

## Structured Selects &mdash; The `<select>` Tag, Again

A [Structured Select Query](nitro-structured-selects.md) executes a `SELECT` statement and models the resulting rows not as a 
traditional flat tabular structure, but as tuples of objects or as trees of objects.

Structured Selects can take several forms, depending on the data structure you want to model out of a `SELECT` query.

The difference between a Flat Select and a Structured Select is that instead of a flat list of columns in the `SELECT` clause, the Structured Select 
includes a `<columns>` tag that defines the hierarchical data structure using `<collection>` and `<association>` tags. These tags can be nested in multiple levels.

For example:

```xml
<select method="retrieveOrderProducts">
  <parameter name="categoryId" java-type="Integer" />
  select
    <columns>
      <vo table="orders" extended-vo="OrderWithProductsVO" alias="o">
        <collection table="product" property="products" alias="p" />
        <association table="region" property="region" alias="r" />
      </vo>
    </columns>
  from orders o
  join region r on o.region_id = r.id
  join order_product op on op.order_id = o.id
  join product p on p.product_id = op.product_id
  where p.category_id = #{categoryId}
  order by o.id
</select>
```

The above query returns a list of a newly created value object `OrderWithProductsVO`. This value object itself properties that reuse already existing value objects to gather data as `List<ProductVO>` and a `RegionVO`. The Java method generated by this query looks like:

```java
public List<OrderWithProductsVO> retrieveOrderProducts(Integer categoryId) { ... }
```

# Nitro

Nitro allows you to combines native SQL with Dynamic SQL to access all SQL features available in the SQL dialect.

Nitro queries can be useful to:

- Run any SQL statement beyond SELECT, INSERT, UPDATE, and DELETE available in CRUD and LiveSQL
- Use [Dynamic SQL](./nitro-dynamicsql.md) to dynamically and iteratively include query sections into your query according to the runtime parameters
- Use native SQL extensions available in the specific database, such as custom functions, regular expression matching, full text search, rollups, optimizer hints, etc.
- Expose long, complex, and tedious queries as simple methods in your app
- Use pre-existent and well-tested queries "as is" from your application

Get started with the [Hello Nitro](../guides/hello-nitro.md) example.


## 1. Example

The following query uses native DB2 features and Dynamic SQL:

```xml
<select method="findActiveAccountsWithClient" vo="AccountClient">
  <parameter name="regionId" java-type="Integer" />
  SELECT a.*, c.name, c.type as "client_type"
  FROM account a
  JOIN client c ON c.id = a.client_id
  WHERE a.balance >= 1000.00 SELECTIVITY 0.0013 
  <if test="regionId != null">
    AND c.region_id = #{regionId} SELECTIVITY 0.07
  </if>
</select>
```

Depending on the value of the `regionId` parameter the query changes form. Note that the `SELECTIVITY` clauses are a part of the DB2 dialect
that allows the developer to tune query performance.

Nitro implement two variations of queries: the ones aimed at producing changes in the database that don't return database data &mdash; the *update* queries &mdash; and the ones that retrieve data from the database &mdash; the *select* queries.

## 2. In a Nutshell

The following list summarizes the main features of the Nitro Queries:

- Nitro allows you to add any valid standard or native SQL query to your application
- All Nitro queries are defined in the XML configuration file of the persistence layer, inside &lt;table>, &lt;view>, or &lt;dao> tags
- Nitro queries are exposed to your application as methods, with or without parameters, in the corresponding DAO classes
- Nitro defines Update queries using the &lt;query> tag and Select queries using the &lt;select> tag
- Entity Selects are defined in the &lt;table> and &lt;view> tags and can only return model objects corresponding to the table or view they belong to
- Free Selects are defined in a &lt;dao> tag and are free to return any type of model objects
- The return type of Nitro Selects &mdash; entity or free &mdash; is always a pair of Layout &amp; Model classes
- Nitro queries can include one or more of the Dynamic SQL tags &lt;if>, &lt;choose>, &lt;foreach>, &lt;bind>, &lt;trim>, &lt;where>, and &lt;set>. They combine query sections dynamically at runtime, by evaluating expressions that use the parameters of each query execution
- Dynamic SQL tags can be nested in many levels as needed. This includes the iterative tag &lt;foreach> that can be used in tandem with the &lt;bind> tag to produce complex iterative query assembling
- Nitro queries can accept parameters using the &lt;parameter> tag, and these parameters can be used in any Nitro expression. These expressions are written using the JEXL syntax
- Nitro expressions can be used by any Dynamic SQL tag when assembling the query
- Nitro expressions can be *applied* to the query using #{expression} or *injected* to it using $SQLINJECTION{expression}
- Due to security considerations Nitro injection is disabled by default and must be enabled in each query separately, and only when strictly needed
- Even though by default Nitro Selects return a fully materialized List, they can also return a Cursor. This can be helpful to reduce the memory footprint of the application, especially for queries that return a large number of rows


## 3. Update Queries

Nitro Update Queries execute a SQL query and do not return data. They are aimed at queries that produce changes in the database.

These queries are defined inside a &lt;table>, &lt;view>, or &lt;dao> tags using the &lt;query> tag. They return an integer value that indicates the number of affected database rows for DML queries (INSERT, UPDATE, or DELETE) or the value zero for other queries.

The following Update query is used to create a table:

```xml
<query method="createTableAccount">
  CREATE TABLE account (
    id INTEGER,
    balance DECIMAL(12, 2)
  )
</query>
```

It's exposed to your application code as:

```java
int createTableAccount()
```

As any other Nitro query, Update queries can declare parameters using the &lt;parameter> tag. The following example defines two parameters, and uses them three times in the query.

```xml
<query method="computeInvoiceTaxes">
  <parameter name="minDate" type="java.time.LocalDate" />
  <parameter name="status" type="String" />
  UPDATE invoice
    SET local_tax = purchase_price * local_pct_tax / 100.0,
        national_tax = purchase_price * national_pct_tax / 100.0
  WHERE purchase_date = #{minDate}
     OR purchase_date > #{minDate} AND invoice_status = #{status}
</query>
```

It's exposed to your application code as:

```java
int computeInvoiceTaxes(java.time.LocalDate minDate, String status)
```

If you need to implement SQL Injection &mdash; by concatenating character parameters &mdash; we could do:

```xml
<query method="createProjectSequence" sql-injection-enabled="true">
  <parameter name="suffix" type="String" />
  CREATE SEQUENCE seq_project_$SQLINJECTION{suffix}
</query>
```

This update query will be available in your app as the method:

```java
int createProjectSequence(String suffix)
```

**Note**: SQL Injection can create severe security vulnerabilities in your application. Use with extreme care, and only when there are no other options.


## 4. Entity Select Queries

Entity Select Queries return data from the database for a table or view defined in the persistence layer.

The Entity Select Queries are defined inside &lt;table> or &lt;view> tags. They can only return model objects for the corresponding table and view. Any extra column that the query select and that doesn't match the entity columns is silently discarded.

Entity Selects are a simple way of selecting data for the specific tables and views defined in the persistence layer using any non-trivial predicate.

One benefit of the Entity Selects is that they don't define extra model classes &mdash; and thus, don't pollute the persistence layer unnecessarily &mdash; since they reuse the model and layout classes of the already existing entities.

## 5. Free Select Queries

Free Select Queries are free to return any type of data from the database. This data can or cannot be related to tables, can combine multiple tables or none at all.

Free Selects are defined inside a &lt;dao> tags. Nitro models the return data with separate model classes tailored separately to each Free Select. Thus, Free Selects require the query declaration to define a name for the model and layout classes by the use of the `vo` attribute. 

Nitro automatically discovers the columns of the query and generates Layout and Model classes accordingly. If, later on, the underlying database tables, views, or query expressions change, the persistence layer will discover them again and will update the Layout class' properties accordingly.

### 5.1 Layout and Model Classes

Free Selects return their result in their own brand new model & layout class defined by the `vo` attribute in the select definition. This is the main modelling pattern described as [Database Row Modeling](../crud/database-row-modeling.md).


For example, the following query defines the VO `ClientAccount`.

```xml
<select method="findActiveAccountsWithClient" vo="ClientAccount">
  <parameter name="clientId" type="Integer" />
  SELECT a.*, c.name, c.type as "client_type"
  FROM account a
  JOIN client c ON c.id = a.client_id
  WHERE c.client_id = #{clientId}
    AND a.active = 1
</select>
```

This query will be available in your app as the method:

```java
List<ClientAccount> findActiveAccountsWithClient(Integer clientId)
```

The default configuration will produce:

- A Layout class named `ClientAccountLayout` that includes all query columns as properties
- A Model class named `ClientAccount` that inherits from the previous one, that you can customize with extra properties and methods
- The names of these classes can be customized in the layer configuration by adding, removing, or superseding the default prefixes and suffixes. See the [&lt;layout>](../config/tags/jdbc-layout.md) and [&lt;model>](../config/tags/jdbc-model.md) included in the [&lt;jdbc>](../config/tags/jdbc.md) tag
- The Layout class will be rewritten if the persistence layer is generated again, later on, to ensure it reflects the latest query structure


### 5.2 Property Names

By default the names of the properties in the Layout classes are generated from the SELECT query column names.

The default naming strategy can be customized by adding Name Solver rules using the [&lt;name-solver>](../config/tags/name-solver.md) tag.

Finally, the property name can be directly superseses &mdash; ignoring the Name Solver &mdash; by explicitly declaring it using a [&lt;column>](../config/tags/column.md) tag.

The following example illustrates these three cases:
 
```xml
<select method="findActiveAccountsWithClient" vo="ClientAccount">
  <column name="local_price" property="currentPrice" >
  SELECT product_id, activation_timestamp, local_price
  FROM product
  WHERE name like '%THEREMIN%'
</select>
```

If a Name Solver rule was declared as shown below:

```xml
<name-solver>
  <name value="^(.+)_timestamp$" replace="$1" scope="column" />
</name-solver>
```

The resulting Layout class will have the following properties:

```java
public class ClientAccountLayout {

  protected Integer productId; // automatically generated from the query column
  protected java.time.LocalDateTime activation; // the name solver removed the suffix
  protected Double currentPrice; // designated by the <column> tag

  ...
}
```


### 5.3 Property Types

The list of column and their types for each Free Select is retrieved when the persistence layer is generated or refreshed. This metadata is fed to the static [Type Resolution Mechanics](../guides/type-resolution-mechanics.md) to decide the type of the propoerty that represents each specific column.

This type resolution is *static*, since it fully happens at generation time. The types of these properties are set in stone and cannot change at runtime. They can be changed, thouhg, by changing the rules and generating the persistence layer again.

The mechanics include sensible default types for most columns in the supported databases, and are also highly configurable by the use of rules that follow a specific precedence. See the documentation above, as well as the [Hello Type Resolution Example](../guides/hello-type-resolution.md) for details.

### 5.4 Fetch Mode

Free Selects can retrieve database data in three modes: list, cursor, and single-row.

#### a) The List Mode

The `list` mode is the default mode and returns a `List<Model>` (formally a `java.util.List`). 

This mode reads all the rows returned by the query into memory, assembles a list, and returns this list
to the caller.

**Note**: This strategy can use high amount of memory if the query returns a large number of rows; in cases
like this the `cursor` mode could be a better fit.

Example:

```xml
<select method="findOldAccounts" vo="OldAccount">
  ...
</select>
```

Equivalent to:

```xml
<select method="findOldAccounts" vo="OldAccount" mode="list">
  ...
</select>
```

This example produces a method like:

```java
List<OldAccount> findOldAccounts()
```

#### b) The Cursor Mode

The `cursor` mode returns a `Cursor<Model>` (formally a `org.hotrod.dynamicsql.Cursor`).

The `Cursor` object is a forward-only, iterable object that uses a buffer 
to receive a limited number of rows at a time from the query, thus limiting the amount of allocated memory at any given time.
Cursors can be a memory-efficient solution for processes that handle large number of rows that need to be
read, processed, and discarded immediately.

Example:

```xml
<select method="findOldAccounts" vo="OldAccount" mode="cursor">
  ...
</select>
```

This example produces a method like:

```java
Cursor<OldAccount> findOldAccounts()
```

#### c) The Single-Row Mode

The `single-row` mode is suitable for queries that return zero or one row at the most. It returns a single Model object.

It's the responsibility of the developer to ensure the query returns at most one row. If at runtime the query
returns more than a single row, an exception is thrown with a message such as:

> Expected one result (or null) to be returned by SELECT, but found: 3

Example:

```xml
<select method="getHighestPayedEmployee" vo="Employee" mode="single-row">
  SELECT * FROM employee ORDER BY salary DESC LIMIT 1
</select>
```

This example produces a Java method like:

```java
Employee getHighestPayedEmployee()
```


### 5.5 Foundation &amp; Complement

In order to discover the actual columns of Free Selects, HotRod needs to prepare it &mdash; not to execute it &mdash; when the persistence layer is being gegenerated.

However, this is not striaghtforward all the time. The query definition can include extra non-SQL sections such as Dynamic SQL tags, parameter an column definitions, injected and applied expressions, items that make the query non valid for the database interpreter.

To deal with this issue the query is divided into the Foundation and Complement:

- The Foundation is the query section that is necessary at generation time to discover query columns. Sections such as WHERE clauses or ORDER BY clause can be included in the foundation but are not necessary.
- A Complement section is a section that is not necessary at generation time to discover query columns. Sections such as WHERE clauses and ORDER BY clause can be enclosed in &lt;complement> to remove them from the foundation.

Nevertheless, the distinction of foundation and complement is only relevant at generation time for the purpose of discovering the query columns. At runtime, the full query is always executed.

## 6. Native SQL

Since the beginning Nitro was geared towards using all bells &amp; whistles a database
engine has to offer. This allows the developer to include the full SQL syntax the database accepts
so the query can benefit from all database-specific tricks. This includes any special addition, such as hints, extra clauses, quirky syntax, etc.

For example, DB2 enhances parameterized filtering predicates with the `SELECTIVITY` clause as shown below:

```xml
<select method="getMostExpensiveWidget" vo="ExpensiveWidget" mode="single-row">
  <parameter name="minPrice" type="Double" />
  <parameter name="maxPrice" type="Double" />
  <parameter name="packagingType" type="String" />
  SELECT *
  FROM widget
  WHERE packaging_type = #{packagingType} SELECTIVITY 0.07
    AND price between #{minPrice} AND #{maxPrice} SELECTIVITY 0.00002
  FETCH FIRST 1 ROW ONLY
</select>
```

By using the `SELECTIVITY` clause the developer is informing the DB2 optimizer that the second filtering predicate `price between #{minPrice} and #{maxPrice}` has a much better selectivity than the first one `packaging_type = #{packagingType}`, something that can seem counterintuitive; when performance comes into play this can be crucial for optimization purposes. Without the hint the database engine may guess it incorrectly (just by analyzing the query and the table histogram) and may end up using the wrong index.

Typically database engines support the most important definitions and clauses specified by the SQL Standard and purposedly neglect the more obscure ones; no database engine implements the SQL Standard in its entirety. On the flip side, engines implement enhanced *non-standard* features that can be very useful in specific scenarios.

Notwithstanding their benefits, the usage of SQL extensions can limit the possibilities of migrating to a different database engine should the application owner decide to pursue this avenue in the future.


## 7. Dynamic SQL

Nitro Dynamic SQL offers you the ability to add query sections conditionally at runtime. Thus, a dynamic query can take many forms at runtime, depending on the parameter values on each execution.

Dynamic SQL includes iterative constructs and binding operators that can help generating complex SQL syntax structures. All these constructs can be applied in plain or nested form to tackle any specific case.

Dynamic SQL also includes expression evaluation to access non-trivial data structures such as nested objects, list and arrays, as well as access to methods and object properties. All, with the use of JEXL syntax.

See [Dynamic SQL](./nitro-dynamicsql.md) for details.

## 8. Parameters

All Nitro queries can have parameters that can be directly applied or injected in the query and that can also be used to control the DynamicSQL logic. From the application's perspective these parameters become parameters in the DAO method that executes the query.

See [Nitro Parameters](./nitro-parameters.md) for details.




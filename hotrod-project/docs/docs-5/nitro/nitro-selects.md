## Nitro Selects

A Nitro SELECT executes a SQL `SELECT` statement and models the resulting result set rows as
objects with properties.

The names of the properties are automatically discovered by Nitro and used to model the model and layout classes. The property types are decided according to the Type Solver rules.

Selects can be parameterized, can include native SQL, and also Dynamic SQL.


## Example

The following query returns rows with values from two tables that are joined. A new model class is automatically created to gather the values of the rows:

```xml
<dao name="AccountingDAO">

  <select method="findActiveAccountsWithClient" vo="ClientAccount">
    <parameter name="clientId" java-type="Integer" />
    SELECT a.*, c.name, c.type as "client_type"
    FROM account a
    JOIN client c ON c.id = a.client_id
    WHERE c.client_id = #{clientId}
      AND a.active = 1
  </select>

</dao>
```

In this example we can see:

 - A new `AccountingDAO` class will be generated.
 - The DAO includes the method `findActiveAccountsWithClient(Integer clientId)` to execute this query.
 - A new Model class `ClientAccount` is generated for this query.
 - A new Layout class `ClientAccountLayout` is generated for this query.
 - The method returns a `List<ClientAccount>`.

The Layout class `ClientAccountLayout` includes all the resulting SELECT column as properties in the class.
In this case, it includes three columns from the table ACCOUNT and two extra columns defined in
the query. That is:

```java
public class ClientAccountLayout {

  protected Integer id;
  protected Long amount;
  protected Integer clientId;
  protected String name;
  protected Short clientType;

  // getters and setters omitted for brevity

}
```

The Model class `Client Account` specializes the Layout class, so the developer can add extra properties and behavior as needed:

```java
@Component
public class ClientAccount extends ClientAccountLayout {

  // Add custom properties and behavior here

}
```

Finally, the DAO class provides the actual method to execute the query:

```java
@Component
public class AccountingDAO implements Serializable, ApplicationContextAware {

  public List<ClientAccount> findActiveAccountsWithClient(Integer clientId) {
    ...
  }

}
```

## Usage

Nitro SELECTs are fully defined in the configuration file. The options are described in [Configuration File](../config/README.md).

### 1. Parameters

Nitro SELECTs can have parameters that can be directly applied or injected in the query and that
can also be used to control the DynamicSQL logic. From the application's perspective these parameters
are added to the DAO method that executes the query.

For details on the definition and usage of parameters see [Nitro Parameters](./nitro-parameters.md).


### 2. Value Object Modeling

HotRod models the rows coming from SELECT queries using two classes: a Layout class and a Model class. See [Value Object Modeling](../crud/value-object-modeling.md) for details.


### 3. Property Names

The resulting layout class includes one property for each column of the result set.

The property names are automatically discovered and given a property form, but can also be superseded by the global [Name Solver](../config/tags/name-solver.md), or by a `<column>` tag added to the query. They are processed in order:

1. If a `<column>` tag includes a `java-name` property, this one decides the property name, and no further processing is performed.
2. If a `<name-solver>` rule matches the column, then it modifies the column name.
3. The discovery process generates a default property name.


### 4. Property Types

The resulting layout class includes one property for each column of the result set.

The property types are automatically discovered and generated based on the specifics of each database. Nevertheless, they can be affected by the global [Type Solver](../config/tags/type-solver.md), or by a `<column>` tag added to the query. They are processed in order:

1. If a `<column>` tag includes a `java-type` property, this one decides the property type, and no further processing is performed.
2. If a `<column>` tag includes a `converter` property, this one produces the property type according to its rules, and no further processing is performed.
3. If a `<type-solver>` rule matches the column, then it decides the property type, and o further processing is performed.
4. Finally, if none of the above rules decides the property type, the discovery process produces a default type based on the column type and the specific database engine. See [Supported Databases](../config/supported-databases.md) for details on your database.


### 5. The `<complement>` Tag

The `<complement>` is used wqhen discovering the resulting columns of a SELECT query and is
used to remove complex dynamic sections of the query, only during the discovery process. This doesn't affect the actual executed query at runtime.

Use `<complement> invalid SQL query section </complement>` to surround query sections that do
not produce valid SQL content sections. These typically correspond to the content of DynamicSQL sections that are meant to be fully processed at runtime.



### 6. Select Fetch Mode

Flat Selects can work with three fetch modes: list, cursor, and single-row.

#### The `list` mode

The `list` mode is the default mode and returns a `List<Model>` (formally a `java.util.List`). 

This mode reads all the rows returned by the query into memory, assembles a list, and returns this list
to the caller.

**Note**: This strategy can use high amount of memory if the query returns a large number of rows; in cases
like this the `cursor` mode can be of use.

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

This example produces a Java method like:

```java
public List<OldAccount> findOldAccounts() {
  ...
}
```

#### The `cursor` mode

The `cursor` mode returns a `Cursor<Model>` (formally a `org.hotrod.dynamicsql.Cursor`).

The `Cursor` object is a forward-only, iterable object that uses a buffer 
to receive a limited number of rows at a time from the query, thus limiting the amount of allocated memory at any given time.
Cursors can be a memory-efficient solution for processes that handle large number of rows that need to be
**read, processed, and discarded immediately**.

Example:

```xml
<select method="findOldAccounts" vo="OldAccount" mode="cursor">
  ...
</select>
```

This example produces a Java method like:

```java
public Cursor<OldAccount> findOldAccounts() {
  ...
}
```

#### The `single-row` mode

The `single-row` mode is suitable for queries that return zero or one row at the most. It returns a single Model object.

**Note**: It's the responsibility of the developer to ensure the query returns at most one row. If at runtime the query
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
public Employee getHighestPayedEmployee() {
  ...
}
```


### 7. Native SQL

Since the beginning Nitro SELECTs were geared towards using all bells &amp; whistles a database
engine has to offer. This allows the developer to include the full SQL syntax the database accepts
so the query can benefit from all database-specific tricks. This includes any special addition, such as hints, extra clauses, quirky syntax, etc.

For example, DB2 enhances parameterized filtering predicates with the `SELECTIVITY` clause as shown below:

```xml
<select method="getMostExpensiveWidget" vo="ExpensiveWidget" mode="single-row">
  <parameter name="minPrice" java-type="Double" />
  <parameter name="maxPrice" java-type="Double" />
  <parameter name="packagingType" java-type="String" />
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


### 8. Dynamic SQL

Nitro Select can use the full DynamicSQL functionality. See [Dynamic SQL](./nitro-dynamicsql.md) for more details.



























 

  


# The `<converter>` Tag

Sometimes modeling a database column *as is* is not convenient for the application.

It may be something as simple as the database not implementing boolean types natively or that 
the application needing to model statuses as Java enums rather than plain Strings.

In other cases the reason can be technical rather than conceptual. Sometimes is much faster to read BLOBs or CLOBs as
`java.io.InputStream`s that could potentially be opened (or not at all to avoid cost when not needed) rather than reading 
the whole LOBs every time for every single row. In other cases the database or driver may use one way of reading a value but a different way to 
write it. 

In cases like the ones above, a converter can resolve the problem. In short, a converter reads a database value and converts
it on the fly to another type. For example, the `VARCHAR` status column with values `'CRE'`, `'ACC'`, `'REJ'` can be modeled as a Java enum with
values `{CREATED, ACCEPTED, REJECTED}` instead of a simple String. This can simplify the coding, debugging, and testing activities.


## Attributes

This tag can include the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The converter name. It's used to reference a converter | Required |
| `java-type` | The application property type that VOs have. This is the converted value | Required |
| `java-raw-type` | The unconverted raw Java type used by the converter class to read or write values to and from the database | Required |
| `class` | The full Java class name that implements the converter logic | Required |


## Changes in Version 4

Version 4 includes a couple of changes in the converter. Namely:

- The attribute `java-intermediate-type` was renamed to `java-raw-type`.
- The `decode()` and `encode()` methods now include the extra parameter `Connection conn`. This is necessary for some JDBC drivers that require the connection information to process values coming and going to the database. For example, PostgreSQL requires it when sending data to a column of type array.


## Usage

The `<converter>` tag defines a converter logic that can be used by multiple database columns. It defines a logic to read values from the 
database (to *decode*) and to write values to the database (to *encode*).

Once a converter has been defined, it can be applied to one or more database column to read and write values. It's applied by using the
[`<column>`](./column.md) tag.

A converter can be applied to any column that is read or written to the database. Apart from tables, it can also be applied to columns of views 
and also to column of Nitro queries.


## Java Converter Class

The Java converter class is a POJO that implements the `org.hotrod.runtime.converter.TypeConverter<R, A>` interface. This interface is shown below:

```java
public interface TypeConverter<R, A> {

  A decode(R raw, Connection conn) throws SQLException; // used when reading from the database

  R encode(A value, Connection conn) throws SQLException; // used when writing to the database

}
```


## Example 1 &ndash; Boolean Stored as a Number

Let's consider the case of an Oracle database column `DECIMAL(4)` that is used to represent a boolean value &mdash; a type that 
Oracle does not support. The column considers the numeric values zero (0) as `false` and one (1) as `true`. The table could be 
created as:

```sql
create table patient (
  id decimal(15) primary key not null,
  name varchar2(20),
  active decimal(4) not null check (active = 0 or active = 1),
  recurring decimal(4) not null check (recurring = 0 or recurring = 1)
)
```

Now, when reading this database column the converter reads it first as the raw type `java.lang.Short`. Then, it converts into a 
`java.lang.Boolean`. In this case:

- `DECIMAL(4)`: the database column type.
- `java.lang.Short`: the raw type for the intermediate value. This value is used briefly during the conversion and
is not available to the application.
- `java.lang.Boolean`: the property type in the application. This is the value that the application sees.

In this example the converter Java class can look like:

```java
package com.ctac.converters;

import java.sql.Connection;
import org.hotrod.runtime.converter.TypeConverter;

public class ShortBooleanConverter implements TypeConverter<Short, Boolean> {

  private static final Short FALSE = 0;
  private static final Short TRUE = 1;

  // Decoding is used when reading from the database

  @Override
  public Boolean decode(Short raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return !FALSE.equals(raw); // Anything read that is different from zero is considered true
  }

  // Encoding is used when writing to the database

  @Override
  public Short encode(Boolean value, Connection conn) {
    if (value == null) {
      return null;
    }
    return value ? TRUE : FALSE;
  }

}
```

This converter can be defined in the configuration file with the `<converter>` tag as in:

```xml
  <converter name="boolean_stored_as_decimal"
    java-type="java.lang.Boolean"
    java-raw-type="java.lang.Short"
    class="com.ctac.converters.ShortBooleanConverter"
  />
```

Once this converter is defined, it can be used in the `<column>`tag as:

```xml
  <table name="patient">
    <column name="active" converter="boolean_stored_as_decimal" />
    <column name="recurring" converter="boolean_stored_as_decimal" />
  </table>
```

**Note**: Instead of defining the converter for each column, a more general approach is to use `<type-solver>`s. Type Solvers 
can automatically apply converters to columns according to the logic defined by the developer.

When look at the generated persistence code we can see the configured VO properties are available
 as a `Boolean` type, not as numeric anymore:

```java
package com.ctac.daos.primitives;

public class PatientVO implements Serializable {

  // VO Properties (table columns)

  protected java.lang.Integer id;
  protected java.lang.String name;
  protected java.lang.Boolean active;
  protected java.lang.Boolean recurring;

  // Getters & Setters omitted for brevity

}
```


## Example 2 &ndash; Using an Array of Integers in PostgreSQL

Let's consider the case of a PostgreSQL database column `INT[]` that is used to represent the list of cards each player holds.
The table could be created as:

```sql
create table player (
  id int primary key not null,
  cards int[]
);
```

In this example the converter Java class can look like:

```java
package app;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

import org.hotrod.runtime.converter.TypeConverter;

public class IntegerArrayConverter implements TypeConverter<java.sql.Array, Integer[]> {

  @Override
  public Integer[] decode(Array raw, Connection conn) throws SQLException {
    if (raw == null)
      return null;
    return (Integer[]) raw.getArray();
  }

  @Override
  public Array encode(Integer[] value, Connection conn) throws SQLException {
    return conn.createArrayOf("integer", value);
  }

}
```

This converter can be defined in the configuration file with the `<converter>` tag as in:

```xml
  <converter name="integer_array_converter"
    java-type="Integer[]"
    java-raw-type="java.sql.Array"
    class="app.IntegerArrayConverter"
  />
```

Once this converter is defined, it can be used in the `<column>`tag as:

```xml
  <table name="player">
    <column name="cards" converter="integer_array_converter" />
  </table>
```

**Note**: Instead of defining the converter for each column, a more general approach is to use `<type-solver>`s. Type Solvers 
can automatically apply converters to columns according to the logic defined by the developer.

Then, retriving and saving data to the database using the property `cards` in the PlayerVO is trivial, since it's 
a traditional `java.lang.Integer[]`. For example, the application code could look like:

```java
  PlayerVO p = this.playerDAO.select(101);
  Integer[] cards = p.getCards();
  p.setCards(new Integer[] { 7, 8, 8, 9 });
  this.playerDAO.update(p);
```







# The `<converter>` Tag

Sometimes modeling a database column *as is* is not convenient for the application.

It may be something as simple as the database not implementing boolean types natively or that 
the application could benefit from modeling statuses as Java enums rather than plain Strings.

In other cases the reason can be more technical. Sometimes is much faster to read BLOBs or CLOBs as `java.io.InputStream`s that could potentially
be opened (or not at all to avoid cost when not needed) rather than reading the whole LOB every time. In other cases the database or driver may
use one way of reading a value but a different one to write it. There reasons vary but the key aspect is that the data is stored in the database 
and the application sees it differently.

In cases like the ones above, a Converter can resolve the problem. In short, a converter reads from a database value and converts
it on the fly to another one. For example, the VARCHAR status column with values `'CRE'`, `'ACC'`, `'REJ'` can be modeled as a Java enum with
values `{CREATED, ACCEPTED, REJECTED}` instead of a simple String. This can simplify the coding, debugging, and testing activities.


## Attributes

This tag can include the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The converter name. It's used to reference a converter | Required |
| `java-type` | The application property type that VOs have. This is the converted value | Required |
| `java-intermediate-type` | The unconverted raw Java type used by the converter class to read or write values to and from the database | Required |
| `class` | The full Java class name that implements the converter logic | Required |


## Usage

The `<converter>` tag defines a converter logic that can be used by multiple database columns. It defines a logic to read values from the 
database (to *decode*) and to write values to the database (to *encode*).

Once a converter has been defined, it can be applied to one or more database column to read and write values. It's applied by using the
[`<column>`](./column.md) tag.

A converter can be applied to any column read or write to the database. It can be applied to columns of views and also to columns of Nitro queries.


## Java Converter Class

The Java converter class is a POJO that implements the `org.hotrod.runtime.converter.TypeConverter<T, A>` interface. This is a very 
simple interface, shown below:

```java
public interface TypeConverter<T, A> {

  A decode(T intermediateValue);

  T encode(A applicationValue);

}
```


## Example

For example, an Oracle database column `DECIMAL(4)` can be used to represent a boolean value, that Oracle does not support natively.
It will consider the numeric values zero (0) as false and one (1) as true. 

Now, when reading this database column the converter reads it first as a `java.lang.Short`. Then, it converts into a 
`java.lang.Boolean`. In this case:

- `DECIMAL(4)`: the database column type.
- `java.lang.Short`: the raw type for the intermediate value. This value is used briefly and on the fly during the conversion and
is not available to the application.
- `java.lang.Boolean`: the property type in the application. This is the value that the application sees.

In this example the converter Java class can look like:

```java
import org.hotrod.runtime.converter.TypeConverter;

public class ShortBooleanConverter implements TypeConverter<Short, Boolean> {

  private static final Short FALSE = 0;
  private static final Short TRUE = 1;

  @Override
  public Boolean decode(Short value) {
    if (value == null) {
      return null;
    }
    return !FALSE.equals(value); // Anything read that is different from zero is considered true
  }

  @Override
  public Short encode(Boolean value) {
    if (value == null) {
      return null;
    }
    return value ? TRUE : FALSE;
  }

}
```


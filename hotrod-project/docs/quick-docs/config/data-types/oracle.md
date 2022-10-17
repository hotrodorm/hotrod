# Oracle Data Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each Oracle type.
In some cases the concrete class for a Java type may be different from the abstract class presented by the value object. The actual type is shown in parenthesis, if different.

**Note**: The Java types for the Oracle columns may vary depending on the specific version and variant of the RDBMS, the operating system where the database engine is running, and the JDBC driver version.

| Oracle Column Type | Default Java Type |
| -- | -- |
| `NUMBER(p,s)`,<br/>`DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)`,<br/>`NUM(p,s)` | If neither p or s are specified:<br/>- `java.math.BigDecimal`<br/>If both are specified and s is different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if p is specified and s is not specified or specified with a value of zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `SMALLINT`,<br/>`INTEGER`,<br/>`INT` | `java.math.BigInteger`<br/>**Note**: In Oracle `SMALLINT`, `INTEGER`, and `INT` are equivalent to `NUMBER(38)`. |
| `FLOAT(p)` | if p is not specified (i.e. a 126-bit float):<br/>- `java.math.BigDecimal`<br/>if p is specified:<br/>- if p <= 23: `java.lang.Float`<br/>- if 24 <= p <= 52: `java.lang.Double`<br/>- if p >=53: `java.math.BigDecimal`<br/> |
| `REAL` | `java.math.BigDecimal`<br/>**Note**: `REAL` is equivalent to `FLOAT(63)`. |
| `DOUBLE PRECISION` | `java.math.BigDecimal`<br/>**Note**: `DOUBLE PRECISION` is equivalent to `FLOAT(126)`. |
| `BINARY_FLOAT` | `java.lang.Float` |
| `BINARY_DOUBLE` | `java.lang.Double` |
| `CHAR(n)`,<br/>`VARCHAR(n)`,<br/>`VARCHAR2(n)`,<br/>`NCHAR(n)`,<br/>`NVARCHAR2(n)` | `java.lang.String` |
| `CLOB`,<br/>`NCLOB` | `java.lang.String` [^1] |
| `LONG` | No default java type [^2] |
| `RAW(n)`,<br/>`LONG RAW` | `byte[]` |
| `BLOB` | `byte[]` [^1] |
| `CLOB`,<br/>`NCLOB` | `java.lang.String` [^1] |
| `LONG` | No default java type [^2] |
| `RAW(n)`,<br/>`LONG RAW` | `byte[]` |
| `BLOB` | `byte[]` [^1] |
| `BFILE` | No default java type [^2] |
| `DATE` | `java.util.Date` |
| `TIMESTAMP` | `java.sql.Timestamp` |
| `TIMESTAMP WITH TIME ZONE` | `java.sql.Timestamp` |
| `TIMESTAMP WITH LOCAL TIME ZONE` | `java.sql.Timestamp` |
| `ROWID` | `java.lang.String`|
| `UROWID` | `java.lang.Object` (`oracle.sql.ROWID`) |
| `XMLTYPE` | No default java type [^2] |
| `URITYPE` | `java.lang.Object` (`oracle.sql.STRUCT`) |
| `INTERVAL YEAR TO MONTH` | `java.lang.Object` (`oracle.sql.INTERVALYM`) |
| `INTERVAL DAY TO SECOND` | `java.lang.Object` (`oracle.sql.INTERVALDS`) |
| `VARRAY(n)` | `java.lang.Object` (`oracle.sql.ARRAY`) |
| `STRUCT` | `java.lang.Object` (`oracle.sql.STRUCT`) |
| `REF` | `java.lang.Object` |

[^1] LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^2] It may be possible to read/write from/to these columns using a `<converter>`. This needs to be tested for each specific case separately.

To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md) for the tables, views, and selects. To override the default type add a `<column>` tag in a `<table>`, `<view>`, or `<select>` definition. Alternatively, use a `<type-solver>` rule, or a `<converter>`.


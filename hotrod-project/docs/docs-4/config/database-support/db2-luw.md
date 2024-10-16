# Support for DB2 LUW Database

The HotRod DB2-LUW adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| DB2 LUW Column Type | Default Java Type |
| -- | -- |
| `SMALLINT` | `java.lang.Short` |
| `INTEGER`,<br/>`INT` | `java.lang.Integer` |
| `BIGINT` | `java.lang.Long` |
| `DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)`,<br/>`NUM(p,s)` | If s is specified and different from zero the Java type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`<br/>if s is not specified or it's zero:<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`<br/>&nbsp;&nbsp;&bull; if 8 < p <= 18: `java.lang.Long`<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger` |
| `DECFLOAT` | `java.math.BigDecimal` |
| `REAL` | `java.lang.Float` |
| `FLOAT`,<br/>`DOUBLE` | `java.lang.Double`
| `CHAR(n)`,<br/> `CHARACTER(n)`,<br/>`VARCHAR(n)`,<br/>`CHARACTER VARYING(n)`,<br/>`CLOB(n)`,<br/>`GRAPHIC(n)`,<br/>`NCHAR(n)`,<br/>`VARGRAPHIC(n)`,<br/>`NVARCHAR(n)`,<br/>`DBCLOB(n)`,<br/>`NCLOB(n)`,<br/>`LONG VARCHAR`,<br/>`LONG VARGRAPHIC` | `java.lang.String` [^1] |
| `VARCHAR FOR BIT DATA`,<br/>`LONG VARCHAR FOR BIT DATA`,<br/>`CHAR(n) FOR BIT DATA`,<br/>`BLOB(n)` | `byte[]` [^1] |
| `DATE` | `java.sql.Date` |
| `TIME` | `java.sql.Time` |
| `TIMESTAMP` | `java.sql.Timestamp` |
| `XML` | `java.lang.String` |
| `BOOLEAN` (pseudo type) | `java.lang.String` |
| `BINARY` | No default HotRod data type [^2] |
| `VARBINARY` | No default HotRod data type [^2] |
| `ROW` | No default HotRod data type [^2] |
| `ARRAY` | No default HotRod data type [^2] |

[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^2]: It may be possible to read/write from/to these columns using a `<converter>`. This needs to be tested for each specific case separately.

To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


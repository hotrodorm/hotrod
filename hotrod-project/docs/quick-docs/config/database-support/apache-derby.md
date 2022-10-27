# Support for Apache Derby Database

The HotRod Apache Derby adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| PostgreSQL Column Type | Default Java Type |
| -- | -- |
| `SMALLINT` | `java.lang.Short` |
| `INTEGER`,<br/>`INT` | `java.lang.Integer` |
| `BIGINT` | `java.lang.Long` |
| `NUMERIC(p,s)` | If neither p or s are specified:<br/>&nbsp;&nbsp;&bull; `java.math.BigInteger`<br/>If s is specified and different from zero the Java type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`<br/>if s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`<br/>&nbsp;&nbsp;&bull; if 8 < p <= 18: `java.lang.Long`<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger` |
| `REAL` | `java.lang.Float` |
| `FLOAT(n)` | If n is not specified, i.e. a `FLOAT(53)`:<br/>&nbsp;&nbsp;&bull; `java.lang.Double`<br/>If n is specified:<br/>&nbsp;&nbsp;&bull; If n <= 23: `java.lang.Float`<br/>&nbsp;&nbsp;&bull; If n >= 24: `java.lang.Double` |
| `DOUBLE`,<br/>`DOUBLE PRECISION`, | `java.lang.Double` |
| `CHAR(n)`,<br/>`CHARACTER(n)` | `java.lang.String` |
| `VARCHAR(n)`,<br/>`CHAR VARYING(n)`,<br/>`CHARACTER VARYING(n)`,<br/>`LONGVARCHAR`,<br/>`CLOB` | `java.lang.String` [^1] |
| `CLOB(n)`,<br/>`NCLOB(n)`,<br/>`TINYTEXT(n)`,<br/>`TEXT(n)`,<br/>`MEDIUMTEXT(n)`,<br/>`LONGTEXT(n)`,<br/>`NTEXT(n)` | `java.lang.String` [^1] |
| `DATE` | `java.sql.Date` |
| `TIME` | `java.sql.Time` |
| `TIMESTAMP`,<br/>`DATETIME`,<br/>`SMALLDATETIME` | `java.sql.Timestamp` |
| `BLOB`,<br/>`BINARY LARGE OBJECT`,<br/>`VARCHAR(n) FOR BIT DATA`,<br/>`LONG VARCHAR`,<br/>`CHAR(n) FOR BIT DATA` | `byte[]` [^1] |
| `BOOLEAN` | `java.lang.Boolean` |
| `XML` | No default HotRod data type [^2] |


[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^2]: It may be possible to read/write from/to these columns using a `<converter>`. This needs to be tested for each specific case separately.



To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


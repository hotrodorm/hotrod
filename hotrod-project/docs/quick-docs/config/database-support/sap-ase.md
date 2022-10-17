# Support for SAP ASE (ex-Sybase) Database

The HotRod SAP ASE adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| SAP ASE Column Type | Default Java Type |
| -- | -- |

| `BIT` | `java.lang.Byte` |
| `TINYINT` | `java.lang.Byte` |
| `UNSIGNED TINYINT` | `java.lang.Byte` [^2] |
| `SMALLINT` | `java.lang.Short` | |
| `UNSIGNED SMALLINT` | `java.lang.Integer` |
| `INT`,<br/>`INTEGER` | `java.lang.Integer` |
| `UNSIGNED INT`,<br/>`UNSIGNED INTEGER` | `java.lang.Long` |
| `BIGINT` | `java.lang.Long` |
| `UNSIGNED BIGINT` | `java.math.BigInteger` |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified, i.e. `DECIMAL(18,0)`:<br/>- `java.lang.Long`<br/>If s is specified and different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if s is not specified or it's zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `MONEY`,<br/>`SMALLMONEY` | `java.math.BigDecimal` |
| `FLOAT(n)`,<br/>`REAL`,<br/>`DOUBLE PRECISION` | `java.lang.Double` [^3] |
| `CHAR(n)`,<br/>`VARCHAR(n)`,<br/>`UNICHAR(n)`,<br/>`UNIVARCHAR(n)`,<br/>`NCHAR(n)`,<br/>`NVARCHAR(n)`,<br/>`TEXT`,<br/>`UNITEXT`,<br/>`SYSNAME`,<br/>`LONGSYSNAME` | `java.lang.String` [^1] |
| `DATE` | `java.sql.Date` |
| `DATETIME`,<br/>`SMALLDATETIME`,<br/>`BIGDATETIME` | `java.sql.Timestamp` |
| `TIME`,<br/>`BIGTIME` | `java.sql.Timestamp` |
| `BINARY(n)`,<br/>`VARBINARY(n\|MAX)`,<br/>`IMAGE` | `byte[]` [^1] |


[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^2]: The SAP ASE JDBC driver does not provide information to differentiate `TINYINT` from `UNSIGNED TINYINT`. If you happen to have an `UNSIGNED TINYINT` column you may want to use the custom type java.lang.Short for it, instead of the default type java.lang.Byte. Or, try to avoid using the `UNSIGNED TINYINT` type altogether, if possible.

[^3]: The SAP ASE JDBC driver does not provide information to differentiate float of different precisions. FLOAT with precision 15 or less could be treated as a Java java.lang.Float. However, since there’s no way to find out the precision of the FLOAT the default type is, regardless of their precision, java.lang.Double.



To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


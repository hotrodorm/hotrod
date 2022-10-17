# Support for MySQL Database

The HotRod MySQL adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| PostgreSQL Column Type | Default Java Type |
| -- | -- |
| `TINYINT`,<br/>`INT1` | `java.lang.Byte` |
| `TINYINT UNSIGNED`,<br/>`INT1 UNSIGNED` | `java.lang.Short` |
| `SMALLINT`,<br/>`INT2` | `java.lang.Short` |
| `SMALLINT UNSIGNED`,<br/>`INT2 UNSIGNED` | `java.lang.Integer` |
| `MEDIUMINT`,<br/>`INT3` | `java.lang.Integer` |
| `MEDIUMINT UNSIGNED`,<br/>`INT3 UNSIGNED` | `java.lang.Integer` |
| `INTEGER`,<br/>`INT4` | `java.lang.Integer` |
| `INTEGER UNSIGNED`,<br/>`INT4 UNSIGNED` | `java.lang.Long` |
| `BIGINT`,<br/>`INT8` | `java.lang.Long` |
| `BIGINT UNSIGNED`,<br/>`INT8 UNSIGNED` | `java.math.BigInteger` |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified:<br/>- `java.lang.Long`<br/>If s is specified and different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if s is not specified or specified with a value of zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 9 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `FLOAT(n)`,<br/>`FLOAT4(n)`,<br/>`FLOAT(n) UNSIGNED`,<br/>`FLOAT4(n) UNSIGNED` | If n is not specified:<br/>- `java.lang.Float`<br/>If n is specified:<br/>- if n <= 24: `java.lang.Float`<br/>- if n >= 25: `java.lang.Double` |
| `DOUBLE`,<br/>`DOUBLE PRECISION`,<br/>`REAL`,<br/>`FLOAT8`,<br/>`DOUBLE UNSIGNED`,<br/>`DOUBLE PRECISION UNSIGNED`,<br/>`REAL UNSIGNED`,<br/>`FLOAT8 UNSIGNED` | `java.lang.Double` |
| `CHAR(n)`,<br/>`VARCHAR(n)`,<br/>`TINYTEXT`,<br/>`TEXT`,<br/>`MEDIUMTEXT`,<br/>`LONGTEXT` | `java.lang.String` [^1] |
| `DATE`,<br/>`YEAR` | `java.sql.Date` |
| `TIME` | `java.sql.Time` |
| `DATETIME`,<br/>`TIMESTAMP` | `java.sql.Timestamp` |
| `TINYBLOB`,<br/>`BLOB`,<br/>`MEDIUMBLOB`,<br/>`LONGBLOB` | `byte[]` [^1] |
| `ENUM` | `java.lang.String` |
| `SET` | `java.lang.String` |

[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.


To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


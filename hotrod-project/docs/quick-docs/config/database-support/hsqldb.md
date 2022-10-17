# Support for HSQLDB (HyperSQL) Database

The HotRod HyperSQL adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| PostgreSQL Column Type | Default Java Type |
| -- | -- |
| `TINYINT` | `java.lang.Byte` |
| `SMALLINT` | `java.lang.Short` |
| `INTEGER` | `java.lang.Integer` |
| `BIGINT` | `java.lang.Long` |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified:<br/>- `java.math.BigInteger`<br/>If s is specified and different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if s is not specified or specified with a value of zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `REAL`,<br/>`FLOAT`,<br/>`DOUBLE` | `java.lang.Double` |
| `CHAR(n)`,<br/>`CHARACTER(n)`,<br/>`VARCHAR(n)`,<br/>`CHARACTER VARYING(n)`,<br/>`CHAR VARYING(n)`,<br/>`LONGVARCHAR(n)`,<br/>`CLOB(n)` | `java.lang.String` [^1] |
| `DATE` | `java.sql.Date` |
| `TIME(n)`,<br/>`TIME(n) WITH TIME ZONE` | `java.sql.Time` |
| `TIMESTAMP(n)`,<br/>`TIMESTAMP(n) WITH TIME ZONE` | `java.sql.Timestamp` |
| `BINARY(n)`,<br/>`VARBINARY(n)`,<br/>`BLOB(n)` | `byte[]` [^1] |
| `BOOLEAN` | `java.lang.Boolean` |
| `OTHER` | `java.lang.Object` |
| `<type> ARRAY` | `java.lang.Object` |
| `INTERVAL <qualifier>` | `java.lang.Object` |
| `BIT(n)`,<br/>`BIT VARYING(n)` | `java.lang.Object` |


[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.


To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


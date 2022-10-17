# Support for SQL Server Database

The HotRod SQL Server adapter automatically maps known database column types to Java types. In most cases this default Java type is well suited to handle the database values, but it can be changed as needed by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each database type.

**Note**: The Java types may vary depending on the specific version and variant of the database engine, the operating system where the database engine is running, and the JDBC driver version.

| SQL Server Column Type | Default Java Type |
| -- | -- |
| `BIT` | `java.lang.Byte` |
| `TINYINT` | `java.lang.Byte` |
| `SMALLINT` | `java.lang.Short` |
| `INT` | `java.lang.Integer` |
| `BIGINT` | `java.lang.Long` |
| `DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified, i.e. `DECIMAL(18,0)`:<br/>- `java.lang.Long`<br/>If s is specified and different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if s is not specified or it's zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `MONEY`,<br/>`SMALLMONEY` | `java.math.BigDecimal` |
| `FLOAT(n)` | If n is not specified, i.e. a `FLOAT(53)`:<br/>- `java.lang.Double`<br/>if n is specified:<br/>- if n <= 24: `java.lang.Float`<br/>- if n >= 25: `java.lang.Double` |
| `REAL` | `java.lang.Float`<br/>**Note**: `REAL` is equivalent to `FLOAT(24)`. |
| `CHAR(n)`,<br/>`CHARACTER(n)`,<br/>`VARCHAR(n  MAX)`,<br/>`CHARVARYING(n MAX)`,<br/>`CHARACTERVARYING(n MAX)`,<br/>`NCHAR(n)`,<br/>`NATIONAL CHAR(n)`,<br/>`NATIONAL CHARACTER(n)`,<br/>`NVARCHAR(n MAX)`,<br/>`NATIONAL CHAR VARYING(n MAX)`,<br/>`NATIONAL CHARACTER VARYING(n MAX)`,<br/>`TEXT`,<br/>`NTEXT` | `java.lang.String` [^1] |
| `DATE` | `java.sql.Date` |
| `DATETIME`,<br/>`SMALLDATETIME` | `java.util.Date` |
| `DATETIME2(n)`,<br/>`DATETIMEOFFSET(n)` | `java.sql.Timestamp` |
| `TIME(n)` | If n is not specified, i.e. `TIME(7)`:<br/>- `java.sql.Timestamp`<br/>If n is specified:<br/>- If n <=3: `java.sql.Time`<br/>- If n >=4: `java.sql.Timestamp` |
| `BINARY(n)`,<br/>`VARBINARY(n|MAX)`,<br/>`IMAGE` | `byte[]` [^1] |
| `HIERARCHYID` | `byte[]` |
| `ROWVERSION` | `java.lang.Object`. Cannot insert, nor update by PK. Selects and deletes work normally. Rows can be "updated by example" when excluding this column. |
| `UNIQUEIDENTIFIER` | `java.lang.String` |
| `SQL_VARIANT` | This type is not supported by the JDBC driver 4.0 provided by SQL Server. A workaround, at least to read it, is to cast this column to a different supported type (maybe using a view or a select) as in th expression: CAST(<column> AS <type>) |
| `XML` | `java.lang.String` [^3] |
| `GEOGRAPHY` | `byte[]` [^4] |
| `GEOMETRY` | `byte[]` [^4] |
| (pseudo column) `<col>` as `<expression>` | Type depends on expression type. Cannot insert, nor update by PK. Selects and deletes work normally. Rows can be "updated by example" when excluding this column. |

[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^3]: Must be a well-formed XML String. Depending on the column definition it may also need to be a valid XML String.

[^4]: These data types represent well-formed binary data as specified by the “[MS-SSCLRT]: Microsoft SQL Server CLR Types Serialization Formats” document at https://msdn.microsoft.com/en-us/library/ee320529.aspx.

To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


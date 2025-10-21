# Db2 LUW Dialect

The Db2 LUW Dialect defines Static Dialect Rules and Runtime Dialect Rules to decide the app types for the most common database column types when data is retrieved from the database using SELECT queries.

These built-in rules are enabled by default and do not require any custom configuration to work. They can be used as a convenient initial default mechanism to decide the app column types, especially when setting up a project or when building a quick prototype.

## Rules Naming

The rules are denoted using the symbol :small_orange_diamond: followed by the rule ID; for example, "D4" means "Static Dialect Rule #4". The rule IDs are indicated in the Layout classes for CRUD tables and views, for Nitro SELECT queries, and in the LiveSQL SELECT queries. They show the exact rule that was used to decide the data type for each column.

The following table includes the naming convention for rules in the dialect as well as defined in the type solvers (for completeness):

| Rule Prefix | Type |
| :-- | :-- |
| D | Static Dialect Rule; e.g. "D4" |
| T | Static Type Solver Rule; e.g. "T3" |
| RD | Runtime Dialect Rule; e.g. "RD1" |
| RT | Runtime Type Solver Rule; e.g. "RT2" |

## Static Dialect Rules

The Static Dialect Rules are used during the persistence layer generation when the type of a column is not specifically designated in the configuration file, and when no static type solver rule matches the column. See the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md) for details on how the Static Dialect Rules apply.

| Database Column Type | Static Dialect Rule |
| :-- | :-- |
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


## Runtime Dialect Rules

The Runtime Dialect Rules are used at runtme to determine the data type for a LiveSQL expression when its type is not designated in the LiveSQL query, and when no runtime type solver rule matches the column. See the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md) for details on how the Runtime Dialect Rules apply.

These rules affect only LiveSQL expression. The type of LiveSQL table or view columns &ndash; such as `a.balance` where `a` is a table or view &ndash; is not affected at runtime since their types were already computed in a static manner during the persistence layer generation.

| Database Column Type | Runtime Dialect Rule |
| :-- | :-- |


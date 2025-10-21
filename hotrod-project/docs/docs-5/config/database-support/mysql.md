# MySQL Dialect

The MySQL Dialect defines Static Dialect Rules and Runtime Dialect Rules to decide the app types for the most common database column types when data is retrieved from the database using SELECT queries.

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
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If s is specified and different from zero the Java type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:D1<br/>if s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`:small_orange_diamond:D2<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`:small_orange_diamond:D3<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:D4<br/>&nbsp;&nbsp;&bull; if 9 < p <= 18: `java.lang.Long`:small_orange_diamond:D5<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger`:small_orange_diamond:D6 |
| `TINYINT`,<br/>`INT1` | `java.lang.Byte`:small_orange_diamond:D7 |
| `TINYINT UNSIGNED`,<br/>`INT1 UNSIGNED` | `java.lang.Short`:small_orange_diamond:D8 |
| `SMALLINT`,<br/>`INT2` | `java.lang.Short`:small_orange_diamond:D9 |
| `SMALLINT UNSIGNED`,<br/>`INT2 UNSIGNED` | `java.lang.Integer`:small_orange_diamond:D10 |
| `MEDIUMINT`,<br/>`INT3` | `java.lang.Integer`:small_orange_diamond:D11 |
| `MEDIUMINT UNSIGNED`,<br/>`INT3 UNSIGNED` | `java.lang.Integer`:small_orange_diamond:D12 |
| `INTEGER`,<br/>`INT4` | `java.lang.Integer`:small_orange_diamond:D13 |
| `INTEGER UNSIGNED`,<br/>`INT4 UNSIGNED` | `java.lang.Long`:small_orange_diamond:D14 |
| `BIGINT`,<br/>`INT8` | `java.lang.Long`:small_orange_diamond:D15 |
| `BIGINT UNSIGNED`,<br/>`INT8 UNSIGNED` | `java.math.BigInteger`:small_orange_diamond:D16 |
| `FLOAT(n)`,<br/>`FLOAT4(n)`,<br/>`FLOAT(n) UNSIGNED`,<br/>`FLOAT4(n) UNSIGNED` | If n is not specified:<br/>&nbsp;&nbsp;&bull; `java.lang.Float`:small_orange_diamond:D17<br/>If n is specified:<br/>&nbsp;&nbsp;&bull; if n <= 24: `java.lang.Float`:small_orange_diamond:D17<br/>&nbsp;&nbsp;&bull; if n >= 25: `java.lang.Double`:small_orange_diamond:D18 |
| `DOUBLE`,<br/>`DOUBLE PRECISION`,<br/>`REAL`,<br/>`FLOAT8`,<br/>`DOUBLE UNSIGNED`,<br/>`DOUBLE PRECISION UNSIGNED`,<br/>`REAL UNSIGNED`,<br/>`FLOAT8 UNSIGNED` | `java.lang.Double`:small_orange_diamond:D18 |
| `CHAR(n)`, `ENUM`, `SET` | `java.lang.String`:small_orange_diamond:D19 |
| `VARCHAR(n)` | `java.lang.String`:small_orange_diamond:D20 |
| `TINYTEXT` | `java.lang.String`:small_orange_diamond:D21 |
| `TEXT`,<br/>`MEDIUMTEXT`,<br/>`LONGTEXT` | `java.lang.String`:small_orange_diamond:D22 |
| `DATE` | `java.time.LocalDate`:small_orange_diamond:D23 |
| `TIME` | `java.time.LocalTime`:small_orange_diamond:D24 |
| `DATETIME` | `java.time.LocalDateTime`:small_orange_diamond:D25 |
| `TIMESTAMP` | `java.time.OffsetDateTime`:small_orange_diamond:D26 |
| `YEAR` | `java.lang.Integer`:small_orange_diamond:D27 |
| `TINYBLOB` | `byte[]`:small_orange_diamond:D28 |
| `BLOB`,<br/>`MEDIUMBLOB`,<br/>`LONGBLOB` | `byte[]`:small_orange_diamond:D29 |

## Runtime Dialect Rules

The Runtime Dialect Rules are used at runtme to determine the data type for a LiveSQL expression when its type is not designated in the LiveSQL query, and when no runtime type solver rule matches the column. See the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md) for details on how the Runtime Dialect Rules apply.

These rules affect only LiveSQL expression. The type of LiveSQL table or view columns &ndash; such as `a.balance` where `a` is a table or view &ndash; is not affected at runtime since their types were already computed in a static manner during the persistence layer generation.

| Database Column Type | Runtime Dialect Rule |
| :-- | :-- |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If s is specified and different from zero the Java type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:RD1<br/>if s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`:small_orange_diamond:RD2<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`:small_orange_diamond:RD3<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:RD4<br/>&nbsp;&nbsp;&bull; if 9 < p <= 18: `java.lang.Long`:small_orange_diamond:RD5<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger`:small_orange_diamond:RD6 |
| `TINYINT`,<br/>`INT1` | `java.lang.Byte`:small_orange_diamond:RD7 |
| `TINYINT UNSIGNED`,<br/>`INT1 UNSIGNED` | `java.lang.Short`:small_orange_diamond:RD8 |
| `SMALLINT`,<br/>`INT2` | `java.lang.Short`:small_orange_diamond:RD9 |
| `SMALLINT UNSIGNED`,<br/>`INT2 UNSIGNED` | `java.lang.Integer`:small_orange_diamond:RD10 |
| `MEDIUMINT`,<br/>`INT3` | `java.lang.Integer`:small_orange_diamond:RD11 |
| `MEDIUMINT UNSIGNED`,<br/>`INT3 UNSIGNED` | `java.lang.Integer`:small_orange_diamond:RD12 |
| `INTEGER`,<br/>`INT4` | `java.lang.Integer`:small_orange_diamond:RD13 |
| `INTEGER UNSIGNED`,<br/>`INT4 UNSIGNED` | `java.lang.Long`:small_orange_diamond:RD14 |
| `BIGINT`,<br/>`INT8` | `java.lang.Long`:small_orange_diamond:RD15 |
| `BIGINT UNSIGNED`,<br/>`INT8 UNSIGNED` | `java.math.BigInteger`:small_orange_diamond:RD16 |
| `FLOAT(n)`,<br/>`FLOAT4(n)`,<br/>`FLOAT(n) UNSIGNED`,<br/>`FLOAT4(n) UNSIGNED` | If n is not specified:<br/>&nbsp;&nbsp;&bull; `java.lang.Float`:small_orange_diamond:RD17<br/>If n is specified:<br/>&nbsp;&nbsp;&bull; if n <= 24: `java.lang.Float`:small_orange_diamond:RD17<br/>&nbsp;&nbsp;&bull; if n >= 25: `java.lang.Double`:small_orange_diamond:RD18 |
| `DOUBLE`,<br/>`DOUBLE PRECISION`,<br/>`REAL`,<br/>`FLOAT8`,<br/>`DOUBLE UNSIGNED`,<br/>`DOUBLE PRECISION UNSIGNED`,<br/>`REAL UNSIGNED`,<br/>`FLOAT8 UNSIGNED` | `java.lang.Double`:small_orange_diamond:RD18 |
| `CHAR(n)`, `ENUM`, `SET` | `java.lang.String`:small_orange_diamond:RD19 |
| `VARCHAR(n)` | `java.lang.String`:small_orange_diamond:RD20 |
| `TINYTEXT` | `java.lang.String`:small_orange_diamond:RD21 |
| `TEXT`,<br/>`MEDIUMTEXT`,<br/>`LONGTEXT` | `java.lang.String`:small_orange_diamond:RD22 |
| `DATE` | `java.time.LocalDate`:small_orange_diamond:RD23 |
| `TIME` | `java.time.LocalTime`:small_orange_diamond:RD24 |
| `DATETIME` | `java.time.LocalDateTime`:small_orange_diamond:RD25 |
| `TIMESTAMP` | `java.time.OffsetDateTime`:small_orange_diamond:RD26 |
| `YEAR` | `java.lang.Integer`:small_orange_diamond:RD27 |
| `TINYBLOB` | `byte[]`:small_orange_diamond:RD28 |
| `BLOB`,<br/>`MEDIUMBLOB`,<br/>`LONGBLOB` | `byte[]`:small_orange_diamond:RD29 |

# Oracle Dialect

The Oracle Dialect defines Static Dialect Rules and Runtime Dialect Rules to decide the app types for the most common database column types when data is retrived from the database in SELECT queries.

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
| `NUMBER(p,s)`,<br/>`DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)`,<br/>`NUM(p,s)` | If neither p or s are specified:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:D1<br/>If both are specified and s is different from zero the type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:D2<br/>if p is specified and s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull;  if p <= 2: `java.lang.Byte`:small_orange_diamond:D3<br/>&nbsp;&nbsp;&bull;  if 2 < p <= 4: `java.lang.Short`:small_orange_diamond:D4<br/>&nbsp;&nbsp;&bull;  if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:D5<br/>&nbsp;&nbsp;&bull;  if 8 < p <= 18: `java.lang.Long`:small_orange_diamond:D6<br/>&nbsp;&nbsp;&bull;  if p > 18: `java.math.BigInteger`:small_orange_diamond:D7 |
| `SMALLINT`,<br/>`INTEGER`,<br/>`INT` | `java.math.BigInteger`<br/>**Note**: In Oracle `SMALLINT`, `INTEGER`, and `INT` are equivalent to `NUMBER(38)`. |
| `BINARY_FLOAT` | `java.lang.Float`:small_orange_diamond:D8 |
| `BINARY_DOUBLE` | `java.lang.Double`:small_orange_diamond:D9 |
| `FLOAT(p)` | if p is not specified (i.e. a 126-bit float):<br/>&nbsp;&nbsp;&bull;  `java.math.BigDecimal`:small_orange_diamond:D14<br/>if p is specified:<br/>&nbsp;&nbsp;&bull;  if p <= 23: `java.lang.Float`:small_orange_diamond:D12<br/>&nbsp;&nbsp;&bull;  if 24 <= p <= 52: `java.lang.Double`:small_orange_diamond:D13<br/>&nbsp;&nbsp;&bull;  if p >=53: `java.math.BigDecimal`:small_orange_diamond:D14 |
| `REAL` | `java.math.BigDecimal`:small_orange_diamond:D10<br/>**Note**: `REAL` is equivalent to `FLOAT(63)`. |
| `DOUBLE PRECISION` | `java.math.BigDecimal`:small_orange_diamond:D11<br/>**Note**: `DOUBLE PRECISION` is equivalent to `FLOAT(126)`. |
| `CHAR(n)` | `java.lang.String`:small_orange_diamond:D15 |
| `NCHAR(n)` | `java.lang.String`:small_orange_diamond:D16 |
| `VARCHAR2(n)` | `java.lang.String`:small_orange_diamond:D17 |
| `NVARCHAR2(n)` | `java.lang.String`:small_orange_diamond:D18 |
| `CLOB(n)` | `java.lang.String`:small_orange_diamond:D19 |
| `NCLOB(n)` | `java.lang.String`:small_orange_diamond:D20 |
| `LONG` | No dialect rule |
| `DATE` | `java.time.LocalDateTime`:small_orange_diamond:21 |
| `TIMESTAMP` | `java.time.LocalDateTime`:small_orange_diamond:22 |
| `TIMESTAMP WITH TIME ZONE` | `java.sql.Timestamp`:small_orange_diamond:23 |
| `TIMESTAMP WITH LOCAL TIME ZONE` | `java.sql.Timestamp`:small_orange_diamond:24 |
| `INTERVAL YEAR TO MONTH` | No dialect rule |
| `INTERVAL DAY TO SECOND` | No dialect rule |
| `RAW(n)` | `byte[]`:small_orange_diamond:25 |
| `LONG RAW` | `byte[]`:small_orange_diamond:26 |
| `BLOB` | `byte[]`:small_orange_diamond:27 |
| `BFILE` | No dialect rule |
| `ROWID` | `java.lang.String`:small_orange_diamond:28 |
| `UROWID` | No dialect rule |
| `XMLTYPE` | No dialect rule |
| `URITYPE` | No dialect rule |
| `VARRAY(n)` | No dialect rule |
| `STRUCT` | No dialect rule |
| `REF` | No dialect rule |

## Runtime Dialect Rules

The Runtime Dialect Rules are used at runtme to determine the data type for a LiveSQL expression when its type is not designated in the LiveSQL query, and when no runtime type solver rule matches the column. See the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md) for details on how the Runtime Dialect Rules apply.

These rules affect only LiveSQL expression. The type of LiveSQL table or view columns &ndash; such as `a.balance` where `a` is a table or view &ndash; is not affected at runtime since their types were already computed in a static manner during the persistence layer generation.

| Database Column Type | Runtime Dialect Rule |
| :-- | :-- |
| `NUMBER(p,s)`,<br/>`DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)`,<br/>`NUM(p,s)` | If neither p or s are specified, or if s is greater than zero:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:RD1<br/>If both are specified and s is different from zero the type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`<br/>if p is specified and s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull;  if p <= 2: `java.lang.Byte`:small_orange_diamond:RD2<br/>&nbsp;&nbsp;&bull;  if 2 < p <= 4: `java.lang.Short`:small_orange_diamond:RD3<br/>&nbsp;&nbsp;&bull;  if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:RD4<br/>&nbsp;&nbsp;&bull;  if 8 < p <= 18: `java.lang.Long`:small_orange_diamond:RD5<br/>&nbsp;&nbsp;&bull;  if p > 18: `java.math.BigInteger`:small_orange_diamond:RD6 |
| `SMALLINT`,<br/>`INTEGER`,<br/>`INT` | `java.math.BigInteger`:small_orange_diamond:RD6<br/>**Note**: In Oracle `SMALLINT`, `INTEGER`, and `INT` are equivalent to `NUMBER(38)`. |
| `FLOAT`,<br/>`REAL`,<br/>`DOUBLE PRECISION` | `java.lang.Double`:small_orange_diamond:RD7 |
| `BINARY_FLOAT` | `java.lang.Float`:small_orange_diamond:RD8 |
| `BINARY_DOUBLE` | `java.lang.Double`:small_orange_diamond:RD9 |
| `CHAR(n)` | `java.lang.String`:small_orange_diamond:RD10 |
| `VARCHAR2(n)`,<br/>`VARCHAR(n)` | `java.lang.String`:small_orange_diamond:RD11 |
| `NCHAR(n)` | `java.lang.String`:small_orange_diamond:RD12 |
| `NVARCHAR2(n)` | `java.lang.String`:small_orange_diamond:RD13 |
| `CLOB` | `java.lang.String`:small_orange_diamond:RD14 |
| `NCLOB` | `java.lang.String`:small_orange_diamond:RD15 |
| `LONGVARCHAR` | `java.lang.String`:small_orange_diamond:RD16 |
| `DATE`, `TIMESTAMP` | `java.time.LocalDateTime`:small_orange_diamond:RD17 |
| `TIMESTAMP WITH TIME ZONE` | `java.time.OffsetDateTime`:small_orange_diamond:RD18 |
| `TIMESTAMP WITH LOCAL TIME ZONE` | `java.time.OffsetDateTime`:small_orange_diamond:RD19 |
| `INTERVAL YEAR TO MONTH` | No dialect rule |
| `INTERVAL DAY TO SECOND` | No dialect rule |
| `RAW(n)` | `byte[]`:small_orange_diamond:RD20 |
| `LONG RAW` | `byte[]`:small_orange_diamond:RD21 |
| `BLOB` | `byte[]`:small_orange_diamond:RD22 |
| `BFILE` | `byte[]`:small_orange_diamond:RD23 |
| `ROWID`, `UROWID` | `java.lang.String`:small_orange_diamond:RD |
| `XMLTYPE` | No dialect rule |
| `URITYPE` | No dialect rule |
| `OBJECT` | No dialect rule |
| `VARRAY(n)` | No dialect rule |
| `REF` | No dialect rule |



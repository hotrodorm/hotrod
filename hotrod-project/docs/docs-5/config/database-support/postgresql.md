# PostgreSQL Dialect

The PostgreSQL Dialect defines Static Dialect Rules and Runtime Dialect Rules to decide the app types for the most common database column types when data is retrieved from the database using SELECT queries.

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
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:D1<br/>If s is specified and different from zero the type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:D1<br/>if s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull; if p == 0: `java.lang.Long`:small_orange_diamond:D21<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`:small_orange_diamond:D2<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`:small_orange_diamond:D3<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:D4<br/>&nbsp;&nbsp;&bull; if 8 < p <= 18: `java.lang.Long`:small_orange_diamond:D5<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger`:small_orange_diamond:D6 |
| `SMALLINT`,<br/>`INT2`,<br/>`SMALLSERIAL` | `java.lang.Short`:small_orange_diamond:D7 |
| `INTEGER`,<br/>`INT`,<br/>`INT4`,<br/>`SERIAL` | `java.lang.Integer`:small_orange_diamond:D8 |
| `BIGINT`,<br/>`INT8`,<br/>`BIGSERIAL` | `java.lang.Long`:small_orange_diamond:D9 |
| `REAL` | `java.lang.Float`:small_orange_diamond:D10 |
| `DOUBLE PRECISION` | `java.lang.Double`:small_orange_diamond:D11 |
| `MONEY` | `java.math.BigDecimal`:small_orange_diamond:D12 |
| `CHAR(n)`,<br/>`CHARACTER(n)` | `java.lang.String`:small_orange_diamond:D13 |
| `VARCHAR(n)`,<br/>`CHARACTER VARYING(n)` | `java.lang.String`:small_orange_diamond:D14 |
| `TEXT` | `java.lang.String`:small_orange_diamond:D15 |
| `DATE` | `java.time.LocalDate`:small_orange_diamond:D16 |
| `TIME(n)`,<br/>`TIME(n) WITHOUT TIME ZONE` | `java.time.LocalTime`:small_orange_diamond:D17 |
| `TIMETZ(n)`,<br/>`TIME(n) WITH TIME ZONE` | `java.time.OffsetTime`:small_orange_diamond:D18 |
| `TIMESTAMP(n)`,<br/>`TIMESTAMP(n) WITHOUT TIME ZONE` | `java.time.LocalDateTime`:small_orange_diamond:D19 |
| `TIMESTAMPTZ(n)`,<br/>`TIMESTAMP(n) WITH TIME ZONE` | `java.time.ZonedDateTime`:small_orange_diamond:D20 |
| `INTERVAL <fields> (n)` | No dialect rule |
| `BYTEA` | `byte[]`:small_orange_diamond:D21 |
| `BOOLEAN`,<br/>`BOOL` | `java.lang.Boolean`:small_orange_diamond:D22 |
| `XML` | No dialect rule |
| `POINT`,<br/>`LINE`,<br/>`LSEG`,<br/>`BOX`,<br/>`PATH`,<br/>`POLYGON`,<br/>`CIRCLE` | No dialect rule |
| `CIDR`,<br/>`INET`,<br/>`MACADDR` | No dialect rule |
| `BIT(n)`,<br/>`BIT VARYING(n)` | No dialect rule |
| `UUID` | No dialect rule |
| `JSON`,<br/>`JSONB` | No dialect rule |
| Arrays, such as:<br/>`INTEGER[]`,<br/>`CHAR[][]`,<br/>`INTEGER ARRAY` | No dialect rule |
| `INT4RANGE`,<br/>`INT8RANGE`,<br/>`NUMRANGE`,<br/>`TSRANGE`,<br/>`TSTZRANGE`,<br/>`DATERANGE` | No dialect rule |
| (enum data types) | No dialect rule |
| (composite types) | No dialect rule |
| `OID`,<br/>`REGPROC`,<br/>`REGPROCEDURE`,<br/>`REGOPER`,<br/>`REGOPERATOR`,<br/>`REGCLASS`,<br/>`REGTYPE`,<br/>`REGROLE`,<br/>`REGNAMESPACE`,<br/>`REGCONFIG`,<br/>`REGDICTIONARY` | No dialect rule |

## Runtime Dialect Rules

The Runtime Dialect Rules are used at runtme to determine the data type for a LiveSQL expression when its type is not designated in the LiveSQL query, and when no runtime type solver rule matches the column. See the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md) for details on how the Runtime Dialect Rules apply.

These rules affect only LiveSQL expression. The type of LiveSQL table or view columns &ndash; such as `a.balance` where `a` is a table or view &ndash; is not affected at runtime since their types were already computed in a static manner during the persistence layer generation.

| Database Column Type | Runtime Dialect Rule |
| :-- | :-- |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified:<br/>&nbsp;&nbsp;&bull; `java.lang.Integer`:small_orange_diamond:RD1<br/>If s is specified and different from zero the type is:<br/>&nbsp;&nbsp;&bull; `java.math.BigDecimal`:small_orange_diamond:RD2<br/>if s is not specified or specified with a value of zero:<br/>&nbsp;&nbsp;&bull; if p <= 2: `java.lang.Byte`:small_orange_diamond:RD3<br/>&nbsp;&nbsp;&bull; if 2 < p <= 4: `java.lang.Short`<br/>&nbsp;&nbsp;&bull; if 4 < p <= 9: `java.lang.Integer`:small_orange_diamond:RD4<br/>&nbsp;&nbsp;&bull; if 8 < p <= 18: `java.lang.Long`:small_orange_diamond:RD5<br/>&nbsp;&nbsp;&bull; if p > 18: `java.math.BigInteger`:small_orange_diamond:RD6 |
| `SMALLINT`,<br/>`INT2`,<br/>`SMALLSERIAL` | `java.lang.Short`:small_orange_diamond:RD7 |
| `INTEGER`,<br/>`INT`,<br/>`INT4`,<br/>`SERIAL` | `java.lang.Integer`:small_orange_diamond:RD8 |
| `BIGINT`,<br/>`INT8`,<br/>`BIGSERIAL` | `java.lang.Long`:small_orange_diamond:RD9 |
| `REAL` | `java.lang.Float`:small_orange_diamond:RD10 |
| `DOUBLE PRECISION` | `java.lang.Double`:small_orange_diamond:RD11 |
| `CHAR(n)` | `java.lang.String`:small_orange_diamond:RD12 |
| `VARCHAR(n)`,<br/>`CHARACTER VARYING(n)`,<br/>`TEXT` | `java.lang.String`:small_orange_diamond:RD13 |
| `DATE` | `java.time.LocalDate`:small_orange_diamond:RD14 |
| `TIMESTAMP(n)`,<br/>`TIMESTAMP(n) WITHOUT TIME ZONE` | `java.time.LocalDateTime`:small_orange_diamond:RD15 |
| `TIMESTAMPTZ(n)`,<br/>`TIMESTAMP(n) WITH TIME ZONE` | `java.time.OffsetDateTime`:small_orange_diamond:RD16 |
| `TIME(n)`,<br/>`TIME(n) WITHOUT TIME ZONE` | `java.time.LocalTime`:small_orange_diamond:RD17 |
| `TIMETZ(n)`,<br/>`TIME(n) WITH TIME ZONE` | `java.time.OffsetTime`:small_orange_diamond:RD18 |
| `INTERVAL <fields> (n)` | No dialect rule |
| `BOOLEAN`,<br/>`BOOL` | `java.lang.Boolean`:small_orange_diamond:RD19 |
| `BYTEA` | `byte[]`:small_orange_diamond:RD20 |
| `INT4RANGE`,<br/>`INT8RANGE`,<br/>`NUMRANGE`,<br/>`TSRANGE`,<br/>`TSTZRANGE`,<br/>`DATERANGE` | No dialect rule |
| `POINT`,<br/>`LINE`,<br/>`LSEG`,<br/>`BOX`,<br/>`PATH`,<br/>`POLYGON`,<br/>`CIRCLE` | No dialect rule |
| `CIDR`,<br/>`INET`,<br/>`MACADDR` | No dialect rule |
| `UUID` | No dialect rule |
| `JSON`,<br/>`JSONB` | No dialect rule |
| `[] (array)` | No dialect rule |
| `TYPE (struct)` | No dialect rule |




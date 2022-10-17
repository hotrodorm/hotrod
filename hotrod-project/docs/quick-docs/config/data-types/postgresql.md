# Support for PostgreSQL database

The HotRod PostgreSQL adapter automatically maps known database column types to Java types. In most of the cases this default Java type is well suited to handle the database values, but it can be tailored by the developer.

## Default Java Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each Oracle type.
In some cases the concrete class for a Java type may be different from the abstract class presented by the value object. The actual type is shown in parenthesis, if different.

**Note**: The Java types for the Oracle columns may vary depending on the specific version and variant of the RDBMS, the operating system where the database engine is running, and the JDBC driver version.

| PostgreSQL Column Type | Default Java Type |
| -- | -- |
| `SMALLINT`,<br/>`INT2`,<br/>`SMALLSERIAL` | `java.lang.Short` |
| `INTEGER`,<br/>`INT`,<br/>`INT4`,<br/>`SERIAL` | `java.lang.Integer` |
| `BIGINT`,<br/>`INT8`,<br/>`BIGSERIAL` | `java.lang.Long` |
| `DECIMAL(p,s)`,<br/>`NUMERIC(p,s)` | If neither p or s are specified:<br/>- `java.math.BigDecimal`<br/>If s is specified and different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if s is not specified or specified with a value of zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `REAL` | `java.lang.Float` |
| `DOUBLE PRECISION` | `java.lang.Double` |
| `MONEY` | `java.math.BigDecimal` |
| `CHAR(n)`,<br/>`CHARACTER(n)`,<br/>`VARCHAR(n)`,<br/>`CHARACTER VARYING(n)` | `java.lang.String` |
| `TEXT` | `java.lang.String` [^1] |
| `BYTEA` | `byte[]` [^1] |
| `DATE` | `java.sql.Date` |
| `TIMESTAMP(n)`,<br/>`TIMESTAMP(n) WITHOUT TIME ZONE`,<br/>`TIMESTAMPTZ(n)`,<br/>`TIMESTAMP(n) WITH TIME ZONE` | `java.sql.Timestamp` |
| `TIME(n)`,<br/>`TIME(n) WITHOUT TIME ZONE`,<br/>`TIMETZ(n)`,<br/>`TIME(n) WITH TIME ZONE` | `java.sql.Timestamp` [^3] |
| `BOOLEAN`,<br/>`BOOL` | `java.lang.Boolean` |
| `INTERVAL <fields> (n)` | No default HotRod data type [^2] |
| `XML` | No default HotRod data type [^2] |
| `POINT`,<br/>`LINE`,<br/>`LSEG`,<br/>`BOX`,<br/>`PATH`,<br/>`POLYGON`,<br/>`CIRCLE` | No default HotRod data type [^2] |
| `CIDR`,<br/>`INET`,<br/>`MACADDR` | No default HotRod data type [^2] |
| `BIT(n)`,<br/>`BIT VARYING(n)` | No default HotRod data type [^2] |
| `UUID` | `java.lang.Object` [^4] |
| `JSON`,<br/>`JSONB` | No default HotRod data type [^2] |
| (arrays, such as)<br/>`INTEGER[]`,<br/>`CHAR[][]`,<br/>`INTEGER ARRAY` | No default HotRod data type [^2] |
| `INT4RANGE`,<br/>`INT8RANGE`,<br/>`NUMRANGE`,<br/>`TSRANGE`,<br/>`TSTZRANGE`,<br/>`DATERANGE` | No default HotRod data type [^2] |
| (enum data types) | No default HotRod data type [^2] |
| (composite types) | No default HotRod data type [^2] |
| `OID`,<br/>`REGPROC`,<br/>`REGPROCEDURE`,<br/>`REGOPER`,<br/>`REGOPERATOR`,<br/>`REGCLASS`,<br/>`REGTYPE`,<br/>`REGROLE`,<br/>`REGNAMESPACE`,<br/>`REGCONFIG`,<br/>`REGDICTIONARY` | No default HotRod data type [^2] |

[^1]: LOB types are by default read all at once into memory as byte arrays. They can also be read/written using streaming instead of loading them all at once. To do this you’ll need to write a `<converter>`.

[^2]: It may be possible to read/write from/to these columns using a `<converter>`. This needs to be tested for each specific case separately.

[^3]: In the special case of a precision of zero, a  java.sql.Time type would be enough to store any time of the day without fractional seconds. However, since the majority of cases will have a different precision this type defaults to java.sql.Timestamp in all cases; this type can handle up to 9 decimal places.

[^4]: Even though, the java.util.UUID type is able to save a value into the database, apparently it cannot read from the database into a Java program. Therefore, the java.lang.Object type is safer, but you'll need to cast it after retrieving a value.

To override the default Java type see the [Configuration File Reference](../configuration-file-structure.md). You can add a `<column>` tag to a `<table>`, `<view>`, or `<select>` definition. Alternatively, you can define a `<type-solver>` rule or implement a `<converter>`.


# Oracle Data Types

If a custom Java type is not specified HotRod will use the following rules to decide which Java type to use for each Oracle column.

**Note**: In parenthesis the actual object type returned by the Oracle JDBC driver that, on occasions, may be different.

**Note**: The Java types for the Oracle columns may vary depending on the specific version and variant of the RDBMS, the operating system where the database engine is running, and the JDBC driver version.

| Oracle Column Type | Default Java Type |
| -- | -- |
| `NUMBER(p,s)`,<br/>`DECIMAL(p,s)`,<br/>`DEC(p,s)`,<br/>`NUMERIC(p,s)`,<br/>`NUM(p,s)` | If neither p or s are specified:<br/>- `java.math.BigDecimal`<br/>If both are specified and s is different from zero the Java type is:<br/>- `java.math.BigDecimal`<br/>if p is specified and s is not specified or specified with a value of zero:<br/>- if p <= 2: `java.lang.Byte`<br/>- if 2 < p <= 4: `java.lang.Short`<br/>- if 4 < p <= 9: `java.lang.Integer`<br/>- if 8 < p <= 18: `java.lang.Long`<br/>- if p > 18: `java.math.BigInteger` |
| `SMALLINT`, `INTEGER`, `INT` | `java.math.BigInteger`<br/>**Note**: In Oracle `SMALLINT`, `INTEGER`, and `INT` are equivalent to `NUMBER(38)`. |
| `FLOAT(p)` | if p is not specified (i.e. a 126-bit float):<br/>- `java.math.BigDecimal`<br/>if p is specified:<br/>- if p <= 23: `java.lang.Float`<br/>- if 24 <= p <= 52: `java.lang.Double`<br/>- if p >=53: `java.math.BigDecimal`<br/> |
| `REAL` | `java.math.BigDecimal`<br/>**Note**: `REAL` is equivalent to `FLOAT(63)`. |
| `DOUBLE PRECISION` | `java.math.BigDecimal`<br/>**Note**: `DOUBLE PRECISION` is equivalent to `FLOAT(126)`. |
| `BINARY_FLOAT` | `java.lang.Float` |
| `BINARY_DOUBLE` | `java.lang.Double` |



# Oracle Data Types


| Oracle Column Type | Default Java Type |
| NUMBER(p,s),
DECIMAL(p,s),
DEC(p,s),
NUMERIC(p,s),
NUM(p,s) | If neither p or s are specified:
- `java.math.BigDecimal`
If both are specified and s is different from zero the Java type is:
- `java.math.BigDecimal`
if p is specified and s is not specified or specified with a value of zero:
- if p <= 2: `java.lang.Byte`
- if 2 < p <= 4: `java.lang.Short`
- if 4 < p <= 9: `java.lang.Integer`
- if 8 < p <= 18: `java.lang.Long`
- if p > 18: `java.math.BigInteger` |
| 
SMALLINT,
INTEGER,
INT | `java.math.BigInteger`
**Note**: SMALLINT, INTEGER, and INT are equivalent to NUMBER(38). |

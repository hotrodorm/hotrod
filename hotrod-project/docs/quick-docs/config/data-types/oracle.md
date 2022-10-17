# Oracle Data Types


| Oracle Column Type | Default Java Type |
| NUMBER(p,s), DECIMAL(p,s), DEC(p,s), NUMERIC(p,s), NUM(p,s) | If neither p or s are specified: |
| SMALLINT, INTEGER, INT | `java.math.BigInteger`<br/>**Note**: SMALLINT, INTEGER, and INT are equivalent to NUMBER(38). |

# LiveSQL Expressions

Expressions in LiveSQL are used in many places of a SQL statement and they express how 
a value is computed.

LiveSQL Expression are typically used in the select list, the `WHERE`, `GROUP BY`, `ORDER BY` and many other clauses.

An expression can be as simple as a scalar value or a column name, or arithmetic
computation. It can also include complex logic (such `CASE`), casting values
between types, boolean logic, or any other SQL logic.


## Expression Types

In LiveSQL expressions fall into 6 different types.

Namely:

| Expression Type | Represented SQL Data Types [^2]|
| -- | -- |
| Numeric Expressions |  `INTEGER`, `SMALLINT`, `DECIMAL`, `DOUBLE`, `REAL`, `FLOAT` |
| Strings Expressions | `VARCHAR`, `CHAR`, `CLOB`, `TEXT` |
| Date-Time Expressions | `DATE`, `TIME`, `TIMESTAMP`, `TIMESTAMP WITH TIME ZONE` |
| Boolean Expressions (predicates) | `BOOLEAN`, `BIT` |
| Binary Expressions | `BLOB`, `VARCHAR FOR BIT DATA` |
| Object Expressions | `OBJECT`, `RECORD`, `ARRAY` |

[^2]: The list of types is not meant to be exhaustive but to give a good idea of the represented types. Each database
implements a different subset of data types.


## Scalars

LiveSQL implements a significant subset of the most common SQL operators. The tables below are classified
by operator type.


### Boxing Scalars

Scalars can be boxed to access all LiveSQL operators and functions. These operators and functions
are then transferred to the database and they are executed there. Unboxed scalars, on the other
hand, will be processed in the Java application and only the result will be sent to the database.

| SQL Example | Description | In LiveSQL |
| -- | -- | -- |
| 123.45 | Literal numeric value | `sql.val(123.45)` |
| 'abc' | Literal string value | `sql.val("abc")` |
| TIMESTAMP '2022-10-15 12:34:56' | Literal Timestamp value | `sql.val(<java.sql.Date>)` |
| true | Literal boolean value | `sql.val(true)` |
| &lt;binary> | Literal binary value | `sql.val(byte[])` |
| OBJECT | Literal object value | `sql.val(<object>)` |

By default boxed scalars are included in the query as SQL parameters (using `?` placeholders).
This is convenient for optimization purposes, since different parameter values will still produce
the same query template; in these cases the engine is able to properly cache and optimize the
query. In some specific cases, plain literals can be useful as well, as explained in the next
section.


### Literal Scalars

Sometimes, however, it could be useful to provide literal values to the engine. In this case,
for example, a value such as `'ACTIVE'` won't be parameterize with a placeholder but will show
up *as is* in the query. This provides more information to the optimizer, while defeating engine
cache at the same time.

Literals are available for some numeric, strings, datetime and boolean values.
See [Literals](./literals.md).


## Operators

In LiveSQL all operators are implemented as functions applied to scalars, columns, or expressions.
Depending on the scalar, column, or expression the repertoire of available functions changes appropriately.

### Numeric Operators

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a + b | Addition | `<a>.plus(<b>)` |
| a - b | Subtraction | `<a>.minus(<b>)` |
| a * b | Multiplication | `<a>.mult(<b>)` |
| a / b | Division | `<a>.div(<b>)` |
| a mod b | Modulus | `<a>.remainder(<b>)` |
| log(a, b) | Logarithm of a in base b | `<a>.log(<b>)` |
| power(a, b) | Exponentiation | `<a>.pow(<b>)` |
| abs(a) | Absolute value | `<a>.abs()` |
| - a | Negation | `<a>.neg()` |
| signum(a) | Signum | `<a>.signum()` |
| round(a, n) | Rounding | `<a>.round(<n>)` |
| trunc(a, n) | Truncating | `<a>.trunc(<n>)` |


### String Operators

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a \|\| b \|\| c | String concatenation | `<a>.concat(<b>).concat(<c>)` |
| length(a) | String length | `<a>.length()` |
| locate(s, searched) | String search | `<s>.locate(<searched>)` |
| locate(s, searched, from) | String search | `<s>.locate(<searched>, <from>)` |
| substring(s, from, len) | Substring | `<s>.substring(<from>, <len>)` |
| lower(a) | Lower case transformation | `<a>.lower()` |
| upper(a) | Upper case transformation | `<a>.upper()` |
| trim(a) | Trimming blanks at both ends | `<a>.trim()` |


### Date-Time Operators and Functions

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| CURRENT DATE | Current date | `sql.currentDate()` |
| CURRENT TIME | Current time | `sql.currentTime()` |
| CURRENT DATETIME | Current timestamp | `sql.currentDateTime()` |
| DATETIME d t | Assemble timestamp | `sql.datetime(<d>, <t>)` |
| DATE a | Extract date | `<a>.date()` |
| TIME a | Extract time | `<a>.time()` |
| EXTRACT a f | Extract timestamp field | `<a>.extract(<f>)` |


### Comparison and Boolean Operators

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a = b | Equality | `<a>.eq(<b>)` |
| a <> b | Inequality | `<a>.ne(<b>)` |
| a > b | Greather Than | `<a>.gt(<b>)` |
| a < b | Less Than | `<a>.lt(<b>)` |
| a >= b | Greather or Equal to | `<a>.ge(<b>)` |
| a <= b | Less or Equal to | `<a>.le(<b>)` |
| a AND b | Boolean AND | `<a>.and(<b>)` |
| a OR b | Boolean OR | `<a>.or(<b>)` |
| NOT a | Boolean NOT | `sql.not(<a>)` |
| a AND NOT b | Boolean AND NOT | `<a>.andNot(<b>)` |
| a OR NOT b | Boolean OR NOT | `<a>.orNot(<b>)` |

### SQL Operators and Functions

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a IS NULL | IS NULL | `<a>.isNull()` |
| a IS NOT NULL | IS NOT NULL | `<a>.isNotNull()` |
| a LIKE b | LIKE | `<a>.like(<b>)` |
| a NOT LIKE b | NOT LIKE | `<a>.notLike(<b>)` |
| a BETWEEN b AND c | BETWEEN | `<a>.between(<b>, <c>)` |
| a NOT BETWEEN b AND c | NOT BETWEEN | `<a>.notBetween(<b>, <c>)` |
| COALESCE(a, b, c, ...) | COALESCE() | `sql.coalesce(<a>, <b>, <c>, ...)` |
| (a, b, c, ...) | tuple | `sql.tuple(<a>, <b>, <c>, ...)` |
| a IN (b, c, d, ...) | IN (list) | `<a>.in(<b>, <c>, <d>, ...)` |
| a NOT IN (b, c, d, ...) | NOT IN (list) | `<a>.notIn(<b>, <c>, <d>, ...)` |
| (a, b, ...) IN ((b, c), (d, e), ...) | IN (tuples list) | `sql.tuple(<a>, <b>).in(sql.tuple(<b>, <c>), sql.tuple(<d>, <e>), ...)` |
| (a, b, ...) NOT IN ((b, c), (d, e), ...) | NOT IN (tuples list) | `sql.tuple(<a>, <b>).notIn(sql.tuple(<b>, <c>), sql.tuple(<d>, <e>), ...)` |
| a IN (*subquery*) | IN (*subquery*) | `<a>.in(sql.select()...)` |
| a NOT IN (*subquery*) | NOT IN (*subquery*) | `<a>.notIn(sql.select()...)` |
| EXISTS (*subquery*) | EXISTS (subquery) | `sql.exists(sql.select()...)` |
| NOT EXISTS (*subquery*) | NOT EXISTS (subquery) | `sql.notExists(sql.select()...)` |
| CASE WHEN a THEN b END | CASE | `sql.caseWhen(<a>, <b>).end()` |
| CASE WHEN a THEN b ELSE e END | CASE | `sql.caseWhen(<a>, <b>).elseValue(e).end()` |
| CASE WHEN a THEN b WHEN c THEN d ELSE e END | CASE | `sql.caseWhen(<a>, <b>).when(<c>, <d>).elseValue(e).end()` |

### Asymmetric Operators

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a IN (*subquery*) | IN (*subquery*) | `<a>.in(sql.select()...)` |
| a NOT IN (*subquery*) | NOT IN (*subquery*) | `<a>.notIn(sql.select()...)` |
| a = ANY (*subquery*) | a = ANY (*subquery*) | `<a>.eqAny(sql.select()...)` |
| a <> ANY (*subquery*) | a <> ANY (*subquery*) | `<a>.neAny(sql.select()...)` |
| a < ANY (*subquery*) | a < ANY (*subquery*) | `<a>.ltAny(sql.select()...)` |
| a > ANY (*subquery*) | a > ANY (*subquery*) | `<a>.gtAny(sql.select()...)` |
| a <= ANY (*subquery*) | a <= ANY (*subquery*) | `<a>.leAny(sql.select()...)` |
| a >= ANY (*subquery*) | a >= ANY (*subquery*) | `<a>.geAny(sql.select()...)` |
| a = ALL (*subquery*) | a = ALL (*subquery*) | `<a>.eqAll(sql.select()...)` |
| a <> ALL (*subquery*) | a <> ALL (*subquery*) | `<a>.neAll(sql.select()...)` |
| a < ALL (*subquery*) | a < ALL (*subquery*) | `<a>.ltAll(sql.select()...)` |
| a > ALL (*subquery*) | a > ALL (*subquery*) | `<a>.gtAll(sql.select()...)` |
| a <= ALL (*subquery*) | a <= ALL (*subquery*) | `<a>.leAll(sql.select()...)` |
| a >= ALL (*subquery*) | a >= ALL (*subquery*) | `<a>.geAll(sql.select()...)` |

### Aggregate Expressions and Window Functions

[Aggregate Functions](./aggregate-functions.md) (such as `MIN()`, `MAX()`, etc.) and 
[Window Functions](./window-functions.md) (such as the `OVER()` clause) are not included 
in this table. See those sections for details on them.


[^1]: Accepts simple expressions or tuples.





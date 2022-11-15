# LiveSQL Expressions

Expressions in LiveSQL are used in many places of a SQL statement and they express how 
a value is computed.

An expression can be as simple as a scalar value or a column name, or arithmetic
computation. It can also include complex logic (such `CASE`), casting values
between types, boolean logic, or any other SQL logic.


## Expression Types

In LiveSQL expressions fall into 6 different types. Namely:
- Numeric Expressions, representing integer, bit, floating point, fixed point decimal, etc.
- Strings Expressions, representing char, varchar, CLOB, texts, etc.
- Date-Time Expressions, representing dates, times, datetimes, timestamps, etc.
- Boolean Expressions (predicates), representing the values true and false.
- Binary Expressions, representing binary data, BLOBs, bit varying, etc.
- Object Expressions, representing all other types such as arrays, records, cursors, UUIDs, etc.


## Operators

LiveSQL implements the following arithmetic operators.

In the table shown below `<a>` and `<b>` represents LiveSQL expressions.


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
| - a | Nagation | `<a>.neg()` |
| signum(a) | Signum | `<a>.signum()` |
| round(a, n) | Rounding | `<a>.round(<b>)` |
| trunc(a, n) | Truncating | `<a>.trunc(<b>)` |


### String Operators

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a \|\| b \|\| c | String concatenation | `<a>.concat(<b>).concat(<c>))` |
| length(a) | String length | `<a>.length()` |
| locate(a, b, f) | String search | `<a>.locate(<b>, <f>)` |
| substring(a, b, c) | Substring | `<a>.substring(<a>, <b>)` |
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
| a < b | Greather Than | `<a>.gt(<b>)` |
| a > b | Less Than | `<a>.lt(<b>)` |
| a >= b | Greather or Equal to | `<a>.ge(<b>)` |
| a <= b | Less or Equal to | `<a>.le(<b>)` |
| a AND b | Boolean AND | `<a>.and(<b>)` |
| a OR b | Boolean OR | `<a>.or(<b>)` |
| NOT a | Boolean NOT | `sql.not(<a>)` |

### SQL Operators and Functions

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a IS NULL | IS NULL | `<a>.isNull(<a>)` |
| a IS NOT NULL | IS NOT NULL | `<a>.isNotNull(<a>)` |
| a LIKE b | LIKE | `<a>.like(<b>)` |
| a NOT LIKE b | NOT LIKE | `<a>.notLike(<b>)` |
| a BETWEEN b AND c | BETWEEN | `<a>.between(<b>, <c>)` |
| a NOT BETWEEN b AND c | NOT BETWEEN | `<a>.notBetween(<b>, <c>)` |
| COALESCE(a, b, c, ...) | COALESCE() | `sql.coalesce(<a>, <b>, <c>, ...)` |
| (a, b, c, ...) | tuple | `sql.tuple(<a>, <b>, <c>, ...)` |
| a [^1] IN (a, b, c, ...) | IN (list) | `<a>.in(<a>, <b>, <c>, ...)` |
| a [^1] NOT IN (a, b, c, ...) | NOT IN (list) | `<a>.notIn(<a>, <b>, <c>, ...)` |
| EXISTS (subquery) | EXISTS (subquery) | `sql.exists(sql.select()...)` |
| NOT EXISTS (subquery) | NOT EXISTS (subquery) | `sql.exists(sql.select()...)` |
| CASE WHEN a THEN b END<br/>CASE WHEN a THEN b ELSE e END<br/>CASE WHEN a THEN b WHEN c THEN d ELSE e END | CASE | `sql.caseWhen(<a>, <b>).end()`<br/>`sql.caseWhen(<a>, <b>).elseValue(e).end()`<br/>`sql.caseWhen(<a>, <b>).when(<c>, <d>).elseValue(e).end()` |

**Asymmetric Operators**

| SQL Operator | Description | In LiveSQL |
| -- | -- | -- |
| a IN (subquery) | IN (subquery) | `<a>.in(sql.select()...)` |
| a NOT IN (subquery) | NOT IN (subquery) | `<a>.notIn(sql.select()...)` |
| a = ANY (subquery) | a = ANY (subquery) | `<a>.eqAny(sql.select()...)` |
| a <> ANY (subquery) | a <> ANY (subquery) | `<a>.neAny(sql.select()...)` |
| a < ANY (subquery) | a < ANY (subquery) | `<a>.ltAny(sql.select()...)` |
| a > ANY (subquery) | a > ANY (subquery) | `<a>.gtAny(sql.select()...)` |
| a <= ANY (subquery) | a <= ANY (subquery) | `<a>.leAny(sql.select()...)` |
| a >= ANY (subquery) | a >= ANY (subquery) | `<a>.geAny(sql.select()...)` |
| a = ALL (subquery) | a = ALL (subquery) | `<a>.eqAll(sql.select()...)` |
| a <> ALL (subquery) | a <> ALL (subquery) | `<a>.neAll(sql.select()...)` |
| a < ALL (subquery) | a < ALL (subquery) | `<a>.ltAll(sql.select()...)` |
| a > ALL (subquery) | a > ALL (subquery) | `<a>.gtAll(sql.select()...)` |
| a <= ALL (subquery) | a <= ALL (subquery) | `<a>.leAll(sql.select()...)` |
| a >= ALL (subquery) | a >= ALL (subquery) | `<a>.geAll(sql.select()...)` |

### Aggregate Expressions and Window Functions

[Aggregate Functions](./aggregate-functions.md) (such as `MIN()`, `MAX()`, etc.) and 
[Window Functions](./window-functions.md) (such as the `OVER()` clause) are not included 
in this table. See those sections for details on them.


[^1]: Accepts simple expressions or tuples.





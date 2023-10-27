# How to render literal values

## 1. Strings

### Oracle

By default, text literals can have 4000 chars max. With extra Oracle configuration it can be extended to 32767 chars.

String -> 'string' -- any single quote must be escaped with two single quotes.
String -> Q'dstringd' -- "d" is a character delimiter that cannot appear followed by a single quote.
                         if "d" is any of [{<( then the closing "d" must be ]}>) respectively.
                         
Examples:

    'Hello'
    'ORACLE.dbs'
    'Jackie''s raincoat'
    '09-MAR-98'
    N'nchar literal'
    
    q'!name LIKE '%DBMS_%%'!'
    q'<'So,' she said, 'It's finished.'>'
    q'{SELECT * FROM employees WHERE last_name = 'Smith';}'
    nq'ï Ÿ1234 ï'
    q'"name like '['"'  

### DB2

String -> 'string' -- any single quote must be escaped with two single quotes.

### PostgreSQL

String -> 'string' -- any single quote must be escaped with two single quotes.

### SQL Server

String -> 'string' -- any single quote must be escaped with two single quotes.

### MySQL

String -> 'string' -- any single quote must be escaped with two single quotes.

### MariaDB

String -> 'string' -- any single quote must be escaped with two single quotes.

### Sybase ASE

String -> 'string' -- any single quote must be escaped with two single quotes.

### H2

String -> 'string' -- any single quote must be escaped with two single quotes.


### HyperSQL

String -> 'string' -- any single quote must be escaped with two single quotes.

### Derby

String -> 'string' -- any single quote must be escaped with two single quotes.

## 2. Numbers

### Oracle

Integers are safe

Floating point can take:

- The decimal point is '.' by default. It can be changed to, for example comma, using:
    ALTER SESSION SET NLS_NUMERIC_CHARACTERS=',.';
- Decimal form: in this case we can just specify the decimal digits to render.
- Scientific form `N.Ne+-N`.
- For floating points valuyes, two special cases need more study: NAN (Not a number) and INFINITY.    

In scientific form, an extra "f" (or "F") suffix indicates this is a 32-bit binary float, while an extra "d" (or "D") suffix indicates it's a 64-bit binary double.

Examples:

    25
    +6.34
    0.5
    25e-03
    -1
    
    25f
    +6.34F
    0.5d
    -1D

### DB2

The numeric data types are categorized as follows:

- Exact numerics: integer and decimal
- Decimal floating-point
- Approximate numerics: floating-point

For decimal floating points values, the following special cases need more study: Infinity, Quiet NaN, Signalling NaN.    

Exact Numerics:

    0
    -32768
    +9223372036854775807

Decimal floating point examples (IEEE 754r) (max 34 digits precision):

    -5.648258283285e+6144

Floating point examples:

    -3.4028234663852886e+38    (single-precision)
    1.1754943508222875e-38     (single-precision)
    -1.7976931348623158e+308   (double-precision)
    1.7976931348623158e+308    (double-precision)

### PostgreSQL

PostgreSQL accepts the following numeric forms:

    digits
    digits.[digits][e[+-]digits]
    [digits].digits[e[+-]digits]
    digitse[+-]digits

    0xhexdigits
    0ooctdigits
    0bbindigits

The last three types are for hexa, octal, and binary integers.

### SQL Server

### MySQL

Exact-value numeric literals have an integer part or fractional part, or both. They may be signed. Examples: 1, .2, 3.4, -5, -6.78, +9.10.

Approximate-value numeric literals are represented in scientific notation with a mantissa and exponent. Either or both parts may be signed. Examples: 1.2E3, 1.2E-3, -1.2E3, -1.2E-3. 

### MariaDB

    10
    +10
    -10
    
    0.1
    .1
    +0.1
    +.1
    
    0.2E3 -- 0.2 * POW(10, 3) = 200
    .2e3
    .2e+2
    1.1e-10 -- 0.00000000011
    -1.1e10 -- -11000000000

### Sybase ASE

### H2

### HyperSQL

### Derby

## 3. Dates, Timestamps, Times, Intervals

### Oracle

DATEs:

- In Oracle DATEs always include a time component. The precision of the DATE type is one second.
- DATEs without a time component can be rendered (with ISO format) as `DATE '1998-12-25'`.
- DATEs with a time component can be rendered (with ISO format) as `TO_DATE('1998-12-25 17:30:45','YYYY-MM-DD HH24:MI:SS')`.

TIMESTAMPs:

- TIMESTAMPS can have zero to 9 decimal places after the second term.
- Can be rendered in ISO format as `TIMESTAMP '1997-01-31 09:26:50.124'` with varying number of decimal places for the seconds.
- TIMESTAMP WITH TIME ZONE can be typed with time zone or time offset as:

    TIMESTAMP '1997-01-31 09:26:56.66 +02:00'
    TIMESTAMP '1999-04-15 8:00:00 -8:00'
    TIMESTAMP '1999-04-15 8:00:00 US/Pacific'
    TIMESTAMP '1999-10-29 01:30:00 US/Pacific PDT'

- There is no literal for TIMESTAMP WITH LOCAL TIME ZONE. Rather, you represent values of this data type using any of the other valid datetime literals.

INTERVALs: TBD

### DB2

DATEs:

- A date can be written in ISO format as `DATE '1998-12-15'`.

TIMESTAMPs:

- In DB2 all TIMESTAMPS are local, so there are no time zone or time offsets.
- A TIMESTAMP can be written in ISO format as `TIMESTAMP '2018-03-22 08:30:58.123456789012'`. It can include from zero to 12 decimal places.

TIMEs:

- A time can be written in ISO format as `TIME '17:15:16'`.

INTERVALs:

- DB2 does not implement INTERVALs.

### PostgreSQL

DATEs:

- A date can be written in ISO format as `DATE '1998-12-15'`.

TIMESTAMPs:

- A TIMESTAMP can be written in ISO format as `TIMESTAMP '2018-03-22 08:30:58.123456'`. It can include from zero to 6 decimal places.
- A TIMESTAMP WITH TIME ZONE can be written as `TIMESTAMP WITH TIME ZONE '2004-10-19 10:23:54+02'`.

TIMEs:

- A TIME can be written in ISO format as `TIME '17:15:16'`.
- A TIME WITH TIME ZONE can be written in ISO format as:

    TIME WITH TIME ZONE '14:05:06-08:00'
    TIME WITH TIME ZONE '14:05:06 America/New_York'

INTERVALs: TBD

### SQL Server

The ISO Format is: `YYYY-MM-DD hh:mm:ss[.nnnnnnn] [{+|-}hh:mm]`

    cast('2023-01-01' as date),
    cast('2023-01-02 12:34:56.123' as datetime), -- datetime max 3 decimals
    cast('2023-01-02 12:34:56.123456789' as datetime2), -- datetime max 7 decimals (accepts 9 but ignores the last 2)
    cast('12:34:56.123456' as time) -- time max 7 decimals


### MySQL

    DATE '2015-07-21'
    TIME '17:31:25'
    TIMESTAMP '2012-12-31 11:30:45'

TIME and TIMESTAMP can have zero to 6 decimal place for the seconds term.

### MariaDB

    DATE '2015-07-21'
    TIME '17:31:25'
    TIMESTAMP '2012-12-31 11:30:45'

TIME and TIMESTAMP can have zero to 6 decimal places for the seconds term.

### Sybase ASE

    cast('2023-01-01' as date)
    cast('2023-01-02 12:34:56.123456' as datetime) -- precision: 1/300 of a second
    cast('2023-01-02 12:34:56.123456' as bigdatetime) -- precision: microsecond
    cast('12:34:56.123456' as time) -- 00:00:00 am to 23:59:59:999 (storage size is 4 bytes)

### H2

    DATE '2004-12-31'
    TIMESTAMP '2005-12-31 23:59:59'
    TIMESTAMP WITH TIME ZONE '2005-12-31 23:59:59Z'
    TIMESTAMP WITH TIME ZONE '2005-12-31 23:59:59-10:00'
    TIMESTAMP WITH TIME ZONE '2005-12-31 23:59:59.123+05'
    TIMESTAMP WITH TIME ZONE '2005-12-31 23:59:59.123456789 Europe/London'
    TIME '23:59:59'
    TIME WITH TIME ZONE '10:15:30.334-03:30'

TIMESTAMP, TIMESTAMP WITH TIME ZONE, TIME, and TIME WITH TIME ZONE can have a fractional seconds precision between 0 and 9 digits.

### HyperSQL

    DATE '2008-08-22'
    TIMESTAMP '2008-08-08 20:08:08'
    TIMESTAMP '2008-08-08 20:08:08+8:00' -- This is the syntax for a TIMESTAMP WITH TIME ZONE literal
    TIME '20:08:08.034900'
    TIME '20:08:08.034900-8:00'          -- This is the syntax for a TIME WITH TIME ZONE literal

### Derby

    DATE('1994-02-23')
    TIMESTAMP('1962-09-23 03:23:34.234')
    TIME('15:09:02')

TIME can have zero to 3 decimal places for the seconds term.
TIMESTAMP can have zero to 9 decimal places for the seconds term.

## 4. Boolean

### Oracle

N/A

### DB2

N/A

### PostgreSQL

Booleans are defined as `true` and `false` as expected.

### SQL Server

### MySQL

### MariaDB

### Sybase ASE

### H2

### HyperSQL

### Derby

## 5. Binary

### Oracle

N/A

### DB2

N/A

### PostgreSQL

Binary data (byte arrays) literals can be written (using hexa) as: `'\xd0add81f'::bytea`.

### SQL Server

### MySQL

### MariaDB

### Sybase ASE

### H2

### HyperSQL

### Derby

## 6. Objects

For all purposes this is N/A in any database.


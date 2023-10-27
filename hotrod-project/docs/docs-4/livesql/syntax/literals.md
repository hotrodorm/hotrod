# LiveSQL Literals

Literal scalars are values that are not parameterized, but are added as inline
literals in the SQL statement. Literal Scalars are available since LiveSQL 4.2.

For example, the following SQL statement (in PostgreSQL) has eight literals in it:

```sql
SELECT
  'Hello', -- a character literal
  14, -- an numeric integer literal
  10680.52, -- a numeric decimal literal
  DATE '2023-12-25', -- a date literal
  TIMESTAMP '2018-03-22 08:30:58.123456', -- a timestamp literal
  TIME '17:05:48.624', -- a time literal
  TIMESTAMP WITH TIME ZONE '2018-03-22 08:30:58.123456+08:15', -- a timestamp with time offset literal
  TIME WITH TIME ZONE '17:05:48.624-03:30' -- a time with time offset literal
```

LiveSQL implements literals for Strings, numeric values, and date/time values.

## Use

Literal values can be created using the `sql.literal()` method. This method has the following variations:

| Method | Description |
| ------ | ------------|
| `sql.literal(String value)` | creates a literal String enclosed in single quotes. For example, `sql.literal("Hello")` will be shown as `'Hello'` in the query |
| `sql.literal(long value)` | creates a numeric integer literal. For example, `sql.literal(1050)` can be used to add the `1050` value to a query. This method also accepts `int`, `short` and `byte` values. Notice it accepts only primitive types (so no nulls are allowed); numeric objects need to be first unboxed to be used in this method |
| `sql.literal(double value, int precision)` | creates a numeric integer literal. For example, `sql.literal(3.1415926536, 5)` can be used to add the `3.14159` value to a query. This method also accepts `float` values. Notice it accepts only primitive types (so no nulls are allowed); numeric objects need to be first unboxed to be used in this method |
| `sql.literal(LocalDate value)` | creates a date without a time component or time offset. The date is included in the query according to the specific database dialect. For example, `sql.literal(LocalDate.of(2023, 12, 25))` is rendered as `DATE '2023-12-25'` in PostgreSQL, but as  `CAST('2023-12-25' as DATE)` in SQL Server |
| `sql.literal(LocalTime value, int precision)` | creates a time without a date component or time offset. The time is included in the query according to the specific database dialect, and displays the specific number of decimal places according to the `precision` parameter. For example, `sql.literal(LocalTime.of(17, 9, 31, 72000000), 3)` is rendered as `TIME '17:09:31.072'` in PostgreSQL, but as  `CAST('17:09:31.072' as TIME)` in SQL Server |
| `sql.literal(LocalDateTime value, int precision)` | creates a timestamp without time offset. The timestamp takes the formatting according to the specific database dialect, and displays the specific number of decimal places according to the `precision` parameter. For example, `sql.literal(LocalDateTime.of(2023, 12, 25, 17, 9, 31, 72000000), 3)` is rendered as `TIME '2023-12-15 17:09:31.072'` in PostgreSQL, but as  `CAST('2023-12-15 17:09:31.072' as DATETIME2)` in SQL Server |
| `sql.literal(OffsetTime value, int precision)` | creates a time with time offset. The time is included in the query according to the specific database dialect, and displays the specific number of decimal places according to the `precision` parameter. For example, `sql.literal(OffsetTime.of(17, 9, 31, 72000000, ZoneOffset.ofHours(-4)), 3)` is rendered as `TIME '17:09:31.072-04:00'` in PostgreSQL |
| `sql.literal(OffsetDateTime value, int precision)` | creates a timestamp with time offset. The timestamp takes the formatting according to the specific database dialect, and display the specific number of decimal places according to the `precision` parameter. For example, `sql.literal(OffsetDateTime.of(2023, 12, 25, 17, 9, 31, 72000000, ZoneOffset.ofHours(-4)), 3)` is rendered as `TIME '2023-12-15 17:09:31.072-04:00'` in PostgreSQL, but as  `CAST('2023-12-15 17:09:31.072 -04:00' as DATETIMEOFFSET)` in SQL Server |

What if I prefer to parameterize a scalar? Well, in that case, use `sql.val(value)` instead. `sql.val(value)` always parameterizes values.

## Examples

The query shown above for PostgreSQL:

```sql
SELECT
  'Hello', -- a character literal
  14, -- an numeric integer literal
  10680.52, -- a numeric decimal literal
  DATE '2023-12-25', -- a date literal
  TIMESTAMP '2018-03-22 08:30:58.123456', -- a timestamp literal
  TIME '17:05:48.624', -- a time literal
  TIMESTAMP WITH TIME ZONE '2018-03-22 08:30:58.123456+08:15', -- a timestamp with time offset literal
  TIME WITH TIME ZONE '17:05:48.624-03:30' -- a time with time offset literal
```

Can be written in LiveSQL as:

```java
    Select<Row> q2 = sql.select(
        sql.literal("Hello"),
        sql.literal(14),
        sql.literal(10680.52, 2),
        sql.literal(LocalDate.of(2023, 12, 25)),
        sql.literal(LocalDateTime.of(2018, 3, 22, 8, 30, 58, 123456000), 6),
        sql.literal(LocalTime.of(17, 5, 48, 624000000), 3),
        sql.literal(OffsetDateTime.of(2018, 3, 22, 8, 30, 58, 123456000, ZoneOffset.ofHoursMinutes(-8, 15)), 6),
        sql.literal(OffsetTime.of(17, 5, 48, 624000000, ZoneOffset.ofHoursMinutes(-3, 30)), 3)
    );
```


## SQL Injection Safety

LiveSQL literals as fuly safe from SQL Injection.

No SQL Injection attacks can be carried out using literal, even if they come from non-validated external sources. If a non-ASCII string is used when creating a String literal, LiveSQL throws an `InvalidLiteralException` and the literal value is not created.

Numeric and date/time literals are safe by definition, since they cannot inject arbitraty text values into the query.

ASCII strings literals are included in the queries enclosed in single quotes. LiveSQL takes the safe approach and limits the string characters to ASCII only, between space and tilde (~) characters. Single quote characters, if present, are escaped appropriately. Control characters or other non-ASCII characters are not allowed.

String with non-ASCII characters can still be used in LiveSQL queries, but as parameteried values instead of literals. They can be added to the query using `sql.val(String)` method that applies them as a parameterized values.


## Query Performance Side Effects

When using scalar values a JDBC query can take two forms, depending if the scalar is typed directly in the SQL statement or parameterized instead.

The following query uses a literal value in the `WHERE` clause::

```sql
select * from client where name = 'Anne Smith';
```

The same query can be written using JDBC parameters as:

```sql
select * from client where name = ?;
```

Later on &mdash; but before the query is actually executed &mdash; and as a separate phase for the second query the parameter is bound by *applying* the value `Anne Smith` to the query.

The key point here is that the query optimization happens when the query is submitted to the database engine. In the first, case the optimizer knows exactly what the column `name` is compared against. In the second case it doesn't, because the actual parameter value will be applied later, when the optimization and execution plan selection already took place.

When the optimizer doesn't know the exact parameter value, then there's no much optimization that can be done in this respect. On the flip side, when the exact parameter values are known at optimization time, there's a variety of options available: histograms can be inspected to obtain more precise selectivities for predicates, partial indexes become available to use by the execution plan, table partitions and tablespaces can be fixed immediately, etc. Providing specific values to the optimizer is particularly useful in access and filtering predicates, such as the ones included in the `WHERE` clause.

So, you would ask, should we use literals in all the queries all the time? Not so fast.

Using literal values all the time has the down side of tricking the database into considering virtually identical queries as totally different ones. For example, the followiing queries are considered different by the optimizers:

```sql
select * from client where name = 'Anne Smith';
select * from client where name = 'Steve McQuinn';
```

When the first query is executed, the optimizer goes through the process of finding the best execution plan of it and caches it. Later on, when the second query is run, the optimizer looks for the query in the cache **and does not find it**; since it defeats the cache, the optimizer needs to optimize the second query from scratch. And that will happen over and over again, if you run the same query thousands of times with different values. This will also fill the database optimizer cache with a myriad of cached plans, and may even keep some really important plans out of it.

If the value were parameterized, as in:

```sql
select * from client where name = ?;
select * from client where name = ?;
```

Then both queries would look identical to the optimizer and the effort of optimization would need to be performed only once. Once the plan is in the cache, then there's going to be no effort in the optimization phase for the second, third execution, and so forth. Plus, it won't fill out the optimizer cache, leaving plenty of room for other queries.

So, which one is it?

When it comes to literal in the filtering predicates (typically in the `WHERE` or `HAVING` clauses), the rule of thumb seems to be to use literal scalars when the range of possible values is very, very limited. For example, when querying against a `status` column that has few possible values. Boolean ones are great candidates since they can have two values (or three if we include null). When the literals show up in the select list or other part of the query then it doesn't make too much of a difference to the optimizer.

Partial indexes are a great example of how a literal can make all the difference compared to a parameterized scalar. For example, consider the following indexes:

```sql
create index ix1 on purchases (client, status);
create index ix2 on purchases (client) where status = 'OPEN';
```

Now, if the following queries are executed:

```sql
select * from purchases where client_id = 1780035 and status = ?;
select * from purchases where client_id = 1780035 and status = 'OPEN';
```

For the first query &mdash; that is parameterized &mdash; the optimizer will only be able to use index `ix1`. For the second one &mdash; that has a literal in it &mdash; the optimizer will be able to use any of those two indexes, and will probably choose the second one, that fares much better if the table is big and has few open purchases.


## DATE, TIME, and TIMESTAMP Compatibility

Since different databases support different data types, LiveSQL prevents some literals to be created if a type is not available for a specific database.

The precision of TIME and TIMESTAMP literals data types also varies between different databases. If an invalid precision is specified LiveSQL also prevents the literal to be created.

The following table shows which types of literals are valid for each database, including their maximum precision in parenthesis:

| Database   | DATE | TIMESTAMP | TIME     | TIMESTAMP WITH TIME ZONE | TIME WITH TIME ZONE |
|------------|:----:|:---------:|:--------:|:------------------------:|:-------:|
| Oracle     | Yes[^1] | Yes (9)   | --       | Yes (9)                  | --      |
| DB2 LUW    | Yes  | Yes (9)   | Yes (9)  | --                       | --      |
| PostgreSQL | Yes  | Yes (9)   | Yes (9)  | Yes (9)                  | Yes (9) |
| SQL Server | Yes  | Yes (7)   | Yes (7)  | Yes (7)                  | --      |
| MySQL      | Yes  | Yes (6)   | Yes (6)  | --                       | --      |
| MariaDB    | Yes  | Yes (6)   | Yes (6)  | --                       | --      |
| Sybase ASE | Yes  | Yes (6)   | Yes (3)  | --                       | --      |
| H2         | Yes  | Yes (9)   | Yes (9)  | Yes (9)                  | Yes (9) |
| HyperSQL   | Yes  | Yes (9)   | Yes (9)  | Yes (9)                  | Yes (9) |
| Derby      | Yes  | Yes (9)   | Yes (9)  | --                       | --      |

[^1]: SQL DATEs are modelled in Oracle using the Oracle DATE data type. Though an Oracle DATE has a time component, this is one is ignored for this specific purpose.














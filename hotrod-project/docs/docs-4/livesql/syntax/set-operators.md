# LiveSQL Set Operators

Set Operators play an integral part of the SQL Standard to consolidate separate subqueries into
a single one according to traditional set algebra, using UNION, INTERSECT, and EXCEPT to combine them.

Each element in this set algebra is a select query. These select queries can be combined into
complex set expressions including any combination of the set operators and any number of nested
parenthesis to group them.


## Background

This important functionality is available starting in version 4.2. Although some efforts were made
in version 3.0 to include the set operators they were not made public until version 4.2, where
they were included with all variants an options.


## Examples

A simple example that combines two rows can take the form:

```sql
select id, name from employee where region = 1201
union
select cid, contractor_name from contractor where region_id = 1201
```

This can be written in LiveSQL as:

```java
AccountTable a = AccountDAO.newTable("a");
ContractorTable c = ContractorDAO.newTable("c");

List<Row> rows = sql.select(a.id, a.name)
    .from(a)
    .where(a.region.eq(1201))
    .union()
    .select(c.cid, a.contractorName)
    .from(c)
    .where(c.regionId.eq(1201))
    .execute();
```

A more complex (quite synthetic for brevity) multi-level query with set algebra can take the form:

```sql
select 101 as n
union all select 250
except (
  select 400
  intersect all (
    select 500
    except all select 600
  )
)
order by n
limit 3
```

This can be written in LiveSQL as:

```java
List<Row> rows = sql
    .select(sql.val(101).as("n"))
    .unionAll().select(sql.val(250))
    .except(
      sql.select(sql.val(400))
      .intersectAll(
        sql.select(sql.val(500))
        .exceptAll(sql.select(sql.val(600)))
    )
    .orderBy(sql.ordering("n"))
    .limit(3)
    .execute();
```

**Note**: For brevity in the example above, the SELECT subqueries were typed in the most concise way
only (e.g. as `select 300`). Keep in mind that each SELECT subquery can take the full query form, including the `FROM`, `JOIN`, `WHERE`, `GROUP BY`, `HAVING`, `ORDER BY`, `OFFSET`, `LIMIT` clauses,
as well as subqueries, and the full LiveSQL expression language.


## SQL Set Operators

The SQL Standard defined the following six standard operators:

- UNION [DISTINCT]
- UNION ALL
- INTERSECT [DISTINCT]
- INTERSECT ALL
- EXCEPT [DISTINCT]
- EXCEPT ALL

The difference between the `DISTINCT` variant &mdash; that is the default one &mdash; and the
`ALL` variant is that the former removes duplicate rows, essentially working as a traditional set operator. The second one does not remove duplicate rows and treats all result sets as *multi-sets*; that is, they can include duplicate rows. This is valid for union, intersection, and set subtraction.

Not all database engines support all these operators. Particularly, EXCEPT was implemented recently
by MariaDB (since 10.4) and MYSQL (since 8.0.31), and INTERSECT ALL and EXCEPT ALL are not available
in several databases. LiveSQL's dialect detects the engine and its version and throws an error
message if an operator is not available; in these cases, it does not execute the query in the database.


## Number of Columns and Column Types

All sub-SELECT of a set operator must have the same number of columns. The column types must also
be compatible.


## Precedence

The SQL Standard defines that `INTERSECT` has a higher precedence compared to `UNION` or `EXCEPT`. Regarding `UNION` and `EXCEPT` they should be processed in the order they are defined,
from left to right.

Unfortunately, not all database engines adhere to this precedence. Notably Oracle database and H2 database have a different default precedence.

For portability and consistency across databases, LiveSQL adheres to the SQL Standard and automatically add parenthesis when set operators of difference precedence are combined in any section of a
LiveSQL query.

For example:

```java
List<Row> rows = sql.select(sql.val(100))
    .union()
    .select(sql.val(200))
    .intersect()
    .select(sql.val(300))
    .execute();
```

Always produces the query:

```sql
select 100
union (
  select 200
  intersect
  select 300
)
```

The parenthesis were added automatically by LiveSQL to ensure proper precedence.


## Explicit Parentesis

Now, if in the previous example the first two subqueries need to be enclosed in parenthesis instead 
of the last two, LiveSQL includes the `sql.enclose()` method to explicitly establish the correct 
precedence.

The following query:

```sql
(
  select 100
  union
  select 200
)
intersect
select 300
```

Can be written as:

```java
List<Row> rows =
    sql.enclose(
      sql.select(sql.val(100))
      .union()
      .select(sql.val(200))
    )
    .intersect()
    .select(sql.val(300))
    .execute();
```

Parenthesis can also be included using nested set operators, as discussed in the next section.


## Inline and Nested Set Operators

As shown in the previous examples the set operators can be placed all in the same level, or with
strict precedence by the use of parenthesis. LiveSQL's set operators come in two flavors: inline and nested.

Inline set operators don't include any parameters when used. For example `.union()`. That means a `.select()` query comes right after it and inline precedence is used to assemble them. For example:

```java
List<Row> rows = sql.select(sql.val(100))
    .unionAll() // inline UNION ALL
    .select(sql.val(200))
    .intersect() // inline INTERSECT
    .select(sql.val(300))
    .execute();
```

Nested set operators do include a SELECT query as a parameters. This nested query can be as simple as
as plain SELECT or can include any number of variations, including full SELECT syntax, and more nested
subqueries. For example:

```java
List<Row> rows = sql.select(sql.val(100))
    .unionAll( // nested UNION ALL
      sql.select(sql.val(200))
      .intersect( // nested INTERSECT
        sql.select(sql.val(300))
      )
    )
    .execute();
```

Inline and nested set operators can be intermixed seamlessly as needed in any part of the query.

Particularly using nested set operators, multi-level and complex queries can be written using 
LiveSQL. Nesting set operators can quickly become complex and difficult to debug. For example,
if A, B, C, D, E, F, and G represent SELECT-subqueries then LiveSQL's set syntax can be used to
represent, for example:

(A &#x222a; (B &#x2229; (C - D &#x2229; E) - F &#x222a; G)

As you can see, this is not trivial anymore, to interpret or to debug.


## Column Names

The SQL result set that a set operator produces includes column names that take their names from the original column names (from tables or expressions aliased with `AS`) of the first combined SELECT in the level. Column names or aliases from the second sub-SELECT and on in the set algebra are ignored.

Even though they look identical to the original column names of the first sub-SELECT, these names are not related to them anymore; they belong to the operator scope, not the table. Therefore they cannot
be qualified with table prefixes (as in `a.amount`) but only as plain identifiers (just `amount`).

This has implications when referring to these columns, specifically when set operators are used as part of a generic subquery or when ordering result sets.


## Ordering

There are two ways of defining the ordering of a query with set operators: using column names, or using column numbers (ordinals).

When using column names, the query accepts aliases coming from the first sub-SELECT (as explained in 
the section above) and not column names anymore. For example:

```java
List<Row> rows = sql
    .select(a.amount, a.region.as("rid")).from(a).where(a.vip.eq(1))
    .unionAll()
    .select(sql.val(1000), sql.val(999))
    .orderBy(sql.ordering("rid"), sql.ordering("amount").desc())
    .execute();
```

When using ordinals to order the columns are numbered starting from 1. The same query above can also 
be written as:

```java
List<Row> rows = sql
    .select(a.amount, a.region.as("rid")).from(a).where(a.vip.eq(1))
    .unionAll()
    .select(sql.val(1000), sql.val(999))
    .orderBy(sql.ordering(2), sql.ordering(1).desc())
    .execute();
```

Ordering can combine multiple ordering terms using names or ordinals interchangeably.

As opposed to plain SELECT queries, expressions are not allowed when specifying the ordering
of queries combined with set operators. Only plain columns are allowed. That is, LiveSQL does
not accept composite expressions such as `rid + 10` or `rid + balance`, but only plain ordering terms such as `rid`.

Nevertheless, ordering terms can include the ordering direction and the nulls ordering clause as well. For example, the ordering `rid DESC NULLS LAST` (LiveSQL's `sql.ordering("rid").desc().nullsLast()`) is perfectly valid.


## Offsets and Limits

The result set of a set operator can be subject to the offset and limit clauses, as in any traditional
SELECT query.

For example the following query skips 50 rows and then limits the result set of the combined query 
to 10 rows:


```java
AccountTable a = AccountDAO.newTable("a");
ContractorTable c = ContractorDAO.newTable("c");

List<Row> rows = sql
    .select(a.id, a.name).from(a).where(a.region.eq(1201))
    .union()
    .select(c.cid, a.contractorName).from(c).where(c.regionId.eq(1201))
    .orderBy(sql.ordering(1), sql.ordering(2))
    .offset(50)
    .limit(10)
    .execute();
```

**Note**: Although it's not strictly necesarry, it's a good practice to use `ORDER BY` before
offsetting and limiting.
































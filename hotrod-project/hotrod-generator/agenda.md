# Agenda

This agenda includes the subject open to discussion for 5.0.

## 1. Names for the Value Objects for Tables, Views, and for Nitro Result Sets

With the aim of unifying:

- The configuration.
- The documentation.
- The messaging and errors.

We'll use:

- **Layout**: for the class with structure and no behavior that mimics the database columns for a table, view or Nitro result set.
- **Model**: for the class where the developer can add extra behavior and properties.

Other words considered:

- copy
- mirror
- image
- replica
- reproduction
- archetype
- original
- sketch
- blueprint
- outline
- synopsis
- skeleton
- frame
- projection
- carbon
- clone

## 2. New Configuration Tags

Currently:

```xml
<hotrod>
  <generators>
    <mybatis-spring>
      <daos
        base-dir="src/main/java" package="my.persistence" primitives-package="primitives"
        dao-prefix="" dao-suffix=""
        abstract-vo-prefix="" abstract-vo-suffix=""
        vo-prefix="" vo-suffix=""
        ndao-prefix="" ndao-suffix=""
        nabstract-vo-prefix="" nabstract-vo-suffix=""
        nvo-prefix="" nvo-suffix=""
        bean-qualifier=""
      />
    </mybatis-spring>
  </generators>
</hotrod>
```

New configuration:

```xml
<hotrod>
  <generators>
     <jdbc base-dir="src/main/java"
           package="app.persistence"
           qualifier="">
       <dao prefix="" suffix="DAO" base-dir="" subpackage="dao" />
       <layout prefix="" suffix="Layout" base-dir="" subpackage="layout" />
       <model prefix="" suffix="" base-dir="" subpackage="model" />
     </jdbc>
   </generators>
</hotrod>
```

## 3. Package Structure

Resolved by #2.

## 4. Optimistic Locking (OL) and CRUD

Optimistic Locking only affects the SQL UPDATEs and DELETEs:

In 4.x:

```java
1a.  accountDAO.update(Account a); // update by PK
1b.  accountDAO.update(Account a); // update by PK - OL with VERSION NUMBER
2.   accountDAO.update(Account a, Account example); // update by example
3.   accountDAO.update(Account a, Table t, Predicate predicate); // update by example

4a.  accountDAO.delete(Integer id); // delete by PK
4b.  accountDAO.delete(Integer id); // delete by PK - OL with VERSION NUMBER
5.   accountDAO.delete(Account example); // delete by example
6.   accountDAO.delete(Table t, Predicate predicate); // delete by criteria
```

In 5.x:

```java
7a.  accountDAO.update(Account a); // update by PK
7b.  accountDAO.update(Account a); // update by PK - OL with VERSION NUMBER
7c.  accountDAO.update(Account a); // update by PK - OL with TIMESTAMP
7d.  accountDAO.update(Account a, AccountBaseline baseline); // update by PK - OL with FULL ROW CHECK
8.   accountDAO.update(Account example, Account values); // update by example
9.   accountDAO.update(Account a, Table t, BooleanExpression predicate); // update by example

10a. accountDAO.delete(Integer id); // delete by PK
10b. accountDAO.delete(AccountBaseline baseline); // delete by PK - OL with VERSION NUMBER
10c. accountDAO.delete(AccountBaseline baseline); // delete by PK - OL with TIMESTAMP
10d. accountDAO.delete(AccountBaseline baseline); // delete by PK - OL with FULL ROW CHECK
11.  accountDAO.delete(Account example); // delete by example
12.  accountDAO.delete(Table t, BooleanExpression predicate); // delete by criteria
```

## 5. Column Attributes

Switch from `java-name` to `property` and from `java-type` to `type`. Affects:

- `<column>.java-name`
- `<column>.java-type`
- `<table>.java-name`
- `<view>.java-name`
- `<enum>.java-name`
- `<parameter>.java-type`
- `<converter>.java-type`
- `<converter>.java-raw-type`
- `<type-solver>/<when>.java-type`

## 6. Test Select Mode: Cursor

DONE.

## 7. Implement `.parameterNullable()` in Dynamic SQL

Differentiate between `.parameter(name)` and `.parameterNullable(name, jdbcType)`.

## 8. Converter Output Parameters

A converter is implemented. The database column "status" (type `INTEGER`) is represented in Java as a `String` according to the following logic:

| App Value<br/>(Java String) | Stored<br/>(database INTEGER) |
|:-:|:-:|
| PEN  |                      1 |
| RECH |                      2 |
| ACT  |                      3 |
| ANUL |                      4 |

This is correct:

```sql
.where(t.status.eq("PEND")) // LiveSQL
WHERE t.status = 'PEND'     -- Valid SQL

.where(t.status.ne("ACT"))  // Liv
WHERE t.status <> 'ACT' -- Valid SQL
```

But this will fail:

```sql
.where(t.status.gt("PEN"))
WHERE t.status > 'PEN' -- Invalid SQL: status is int!

.where(t.status.length().eq(3))
WHERE length(t.status) = 3  -- Invalid SQL: status is int!
```

Which operators should we consider?

```sql
=
<>
>
<
>=
<=
between
like
+
-
*
/
mod()
abs()
length()
etc.
```

Maybe columns with converter should only implement `=` and `<>`.








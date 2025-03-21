# Agenda

Subject to discuss.

## 1. Nombres para el abstract VO y VO 

With the aim of unifying:

- The configuration.
- The documentation.
- The messaging and errors.

We'll use:

- **Layout**: for the structure that mimics the database tables and views.
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
         base-dir="src/main/java" package="mi.persistencia" primitives-package="primitives"
         dao-prefix="" dao-suffix=""
         abstract-vo-prefix="" abstract-vo-suffix=""
         vo-prefix="" vo-suffix=""
         ndao-prefix="" ndao-suffix=""
         nabstract-vo-prefix="" nabstract-vo-suffix=""
         nvo-prefix="" nvo-suffix=""
         bean-qualifier=""
       />
```

New configuration:

```xml
<hotrod>
  <generators>
     <jdbc base-dir="src/main/java"
           package="mi.persistencia"
           qualifier="">
       <dao prefix="" suffix="" base-dir="" subpackage="" />
       <layout prefix="" suffix="" base-dir="" subpackage="" />
       <model prefix="" suffix="" base-dir="" subpackage="" />
     </jdbc>
   </generators>
</hotrod>
```

## 3. Package Structure

Resolved by #2.

## 4. Optimistic Locking (OL) y CRUD

Optimistic Locking sólo afecta los UPDATEs y DELETEs de SQL:

En 4.x:

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

En 5.x:

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

This should be running correctly but it's not tested.










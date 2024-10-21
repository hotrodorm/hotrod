# Logic to Determine the Data Type of a Column

The HotRod CRUD, Nitro, and LiveSQL modules follow slightly different set of rules to determine the resulting type of a column being read by a SELECT query. The following examples show the different cases.

## Examples

For the following examples consider the following table:

```sql
  create table employee (
    salary decimal(10, 2),
    bonus int
  );
```

### A. Table Column in CRUD &amp; Nitro

The columns `salary` and `bonus` are table columns in CRUD/Nitro.

In this example, the column `salary` can end up having a different type depending on how it's configured:

1. The simplest configuration will use the HotRod dialect default type. In this case the column type will be `BigDecimal`:

    ```xml
      <table name="employee" />
    ```

2. If a type solver is defined, it overrides the dialect default. In this case the column type is `Double`:

    ```xml
      <type-solver>
        <when test="type = 'DECIMAL'" java-class="Double" />
      </type-solver>
      <table name="employee" />
    ```

3. If the type is set in an explicit `<column>` tag it overrides the dialect default and the type solver. In this case the column type is `Float`:

    ```xml
      <type-solver>
        <when test="type = 'DECIMAL'" java-class="Double" />
      </type-solver>
      <table name="employee">
        <column name="salary" java-type="Float" />
      </table>
    ```

### B. Expression in Nitro

The column `total` is an expression in Nitro. Unless there's a `class` or `converter` attribute, its type will be the JDBC driver default type:

```xml
  <select ...>
    select
    <columns vo="AppraisedEmployee">
      <vo table="employee" property="appemp" alias="e" />
      <expression property="total">
        salary + bonus
      </expression>
    </columns>
    from employee e
  </select>
```

### C. Table Column in LiveSQL Select by Criteria

When using a *Select By Criteria* the columns `salary` and `bonus` are retrieved into the Employee class the same way as in case A:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Employee> employees = sql.select(e, e.salary.gt(50000))
    .execute();
```

### D. Table Column in LiveSQL Select

When using a *LiveSQL Select* the columns `salary` and `bonus` are read using JDBC Driver Default Types:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Row> rows = sql.select(
      e.salary,
      e.bonus
    )
    .from(e)
    .execute();
```

### E. Expressions in LiveSQL

When using a *LiveSQL Select*:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Row> rows = sql.select(
      e.salary.plus(e.bonus).as("total"),
      e.salary.mult(1.25).as("gross").type(Integer.class)
    )
    .from(e)
    .execute();
```

In this case:

- The column `total` is untyped and is read using the Runtime Type Solver (E2) as `Double` or the JDBC Driver default type as `BigDecimal` (E3).
- **New!** The column `gross` is typed and is read as an `Integer` (E1).

## Currently (4.8)

The table shown below explains the logic to determine the data type of a columns in HotRod 4.8.

The numbering indicates the ordering in which the rules are checked. The first one that applies determine the data type, and the rest is ignored.

| A. Table Column in CRUD &amp; Nitro | B. Expression in Nitro | C. Table Column in LiveSQL Select By Criteria | D. Table Column in LiveSQL Select | E. Expression in LiveSQL |
| :----------------------------- | :-------- | :----------------------- | :-- | :-- |
| 1. `<column java-type>` | 1. `class` or `converter` | 1. `<column java-type>` | 1. JDBC Driver Default | 1. JDBC Driver Default |
| 2. Static Table `<type-solver>` | 2. JDBC Driver Default (used to use the Static Table `<type-solver>`) | 2. Static Table `<type-solver>` | -- | -- |
| 3. HotRod Dialect Default                | --                         | 3. HotRod Dialect Default | -- | -- |


## Proposed New Logic (4.9)

| A. Table Column in CRUD &amp; Nitro | B. Expression in Nitro | C. Table Column in LiveSQL Select By Criteria | D. Table Column in LiveSQL Select | E. Expression in LiveSQL |
| :----------------------------- | :-------- | :----------------------- | :-- | :-- |
| 1. `<column java-type>` | 1. `class` or `converter` | 1. `<column java-type>` | 1. `<column java-type>` | 1. `.type(class/converter)` |
| 2. Static Table `<type-solver>` | 2. JDBC Driver Default (used to use the Static Table `<type-solver>`) | 2. Static Table `<type-solver>` | 2. Static Table `<type-solver>` | 2. Runtime Query `<type-solver>` |
| 3. HotRod Dialect Default                | --                         | 3. HotRod Dialect Default | 3. HotRod Dialect Default | 3. HotRod Dialect Default |
| 4. JDBC Driver Default | -- | 4. JDBC Driver Default | 4. JDBC Driver Default | 4. JDBC Driver Default |

The main changes are:

1. Case D will always behave like case C. This will provide a known and expected way of reading data from tables and views.
1. The JDBC driver default type will be added to become a default type of last resort.
1. The new method `.type(class/converter)` in LiveSQL can override the data type of a table column or expression.
1. The *Runtime Query Type Solver* may work slightly different compared to the *Static Table Type Solver*. Unfortunately, the JDBC standard provides different meta data for static tables and for runtime SELECT queries.
1. Converters are included in all cases.
1. The term "Table Column" refers to columns of entities and, as such, applies to columns of database tables and views.















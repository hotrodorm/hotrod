# Logic to Determine the Data Type of a Column

## Currently (4.8)

The table shown below explains the logic to determine the data type of a columns in HotRod 4.8:

| A. Table Column in CRUD &amp; Nitro | B. Expression Column in Nitro | C. Table Column in LiveSQL | D. Expression in LiveSQL |
| :----------------------------- | :-------- | :----------------------- | :-- |
| 1. `<column java-type>` | 1. `class` or `converter` | 1. `<column java-type>` | 1. JDBC Driver Default |
| 2. `<type-solver>`                | 2. JDBC Driver Default | 2. `type-solver>`                 | -- |
| 3. Dialect Default                | --                         | 3. Dialect Default | -- |

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

The column `total` is an expression in Nitro. Its type will be the JDBC driver default type:

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

### C. Table Column in LiveSQL

When using a *Select By Criteria* the columns are retrieved into the Employee class the same way as in case A:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Employee> employees = sql.select(e, e.salary.gt(50000))
    .execute();
```

### D. Expression in LiveSQL

When using a *General LiveSQL Select* the columns `salary` and `bonus` are retrieved using the main type rules:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Row> rows = sql.select(
      e.salary,
      e.bonus,
      e.salary.plus(e.bonus).as("total")
    )
    .from(e)
    .execute();
```

## Proposed New Logic (4.9)

| A. Table Column in CRUD &amp; Nitro | B. Expression Column in Nitro | C/D. Table Column in LiveSQL | E. Expression in LiveSQL |
| :----------------------------- | :-------- | :----------------------- | :-- |
| 1. `<column java-type>` | 1. `class` or `converter` | 1. `<column java-type>` | 1. `.type(class)` |
| 2. Code Generation `<type-solver>` | 2. JDBC Driver Default | 2. Code Generation `<type-solver>`                 | 2. Runtime `<type-solver>` |
| 3. Dialect Default                | --                         | 3. Dialect Default | 3. Dialect Default |

### E. Expressions in LiveSQL

When using a *General LiveSQL Select*:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Row> rows = sql.select(
      e.salary,
      e.bonus,
      e.salary.plus(e.bonus).as("total"),
      e.salary.mult(1.25).as("gross").type(Integer.class)
    )
    .from(e)
    .execute();
```

In this case:

- The columns `salary` and `bonus` are retrieved using the main type rules (A).
- The column `total` is untyped and is read using the Runtime Type Solver (E2) as `Double` or the JDBC Driver default type as `BigDecimal` (E3).
- The column `gross` is typed and is read as an `Integer` (E1).

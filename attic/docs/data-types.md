# Logic to Determine the Data Type of a Column

## Currently (4.8)

| Priority | A. Table Column<br/>in CRUD/Nitro | B. Expression Column<br/>in Nitro | C. Table Column<br/>in LiveSQL | D. Expression<br/>in LiveSQL |
|:--------:| ------------------------------ | -------------------------- | ------------------------------ | -- |
| 1 | `<column java-type>` | JDBC Driver Default        | `<column java-type>` | JDBC Driver Default |
| 2 | `<type-solver>`                | --                         | `type-solver>`                 | -- |
| 3 | Dialect Default                | --                         | Dialect Default                | -- |

## Examples

For the following examples consider the following table:

```sql
  create table employee (
    salary decimal(10, 2),
    bonus int
  );
```

### A. Table Column in CRUD/Nitro

The columns `salary` and `bonus` are table columns in CRUD/Nitro.

In this example, the column `salary` can end up having a different type dependin on how it's configured:

1. The simplest configuration will use the HotRod dialect default type. In this case the column type will be `BigDecimal`:

    ```xml
      <table name="employee" />
    ```

2. A type solver can override the dialect default. In this case the column type is `Double`:

    ```xml
      <type-solver>
        <when test="type = 'DECIMAL'" java-class="Double" />
      </type-solver>
      <table name="employee" />
    ```

3. The type set in an explicit `<column>` overrides other settings. In this case the column type is `Float`:

```xml
  <type-solver>
    <when test="type = 'DECIMAL'" java-class="Double" />
  </type-solver>
  <table name="employee">
    <column name="salary" java-type="Float" />
  </table>
```

### B. Expression in Nitro

The column `total` is an expression in Nitro:

```xml
  <select ...>
    select
    <columns vo="AppraisedEmployee">
      <vo table="employee" property="appemp" alias="e" />
      <expression property="total">
        salary + bonus
      </expression>
    </columns>
    from employee
  </select>
```

### C. Table Column in LiveSQL

The columns `salary` and `bonus` are retrieved into the Employee class the same way as in case A:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Employee> employees = sql.select(e, e.salary.gt(50000)).execute();
```

### D. Expression in LiveSQL

All columns `salary`, `bonus`, and total are returned into a Map using the JDBC Driver default classes:

```java
  EmployeeTable e = EmployeeDAO.newTable();
  List<Row> rows = sql.select(e.star(), e.salary.plus(e.bonus).as("total"))
    .from(e)
    .execute();
```


# CRUD

CRUD models schema tables and views as fully typed classes. They can be used in CRUD queries as well as in LiveSQL queries.

Tables and views are modeled as Layout and Model classes and their columns are modeled as properties of these.

The persistence methods to retrieve and update data are modeled in separate Data Access Objects (DAOs) with methods for each one. These include different variations of the traditional SELECT, INSERT, UPDATE, and DELETE statements according to the specifics of each table or view in the database.

Note that the CRUD modeling of tables as Layout classes and Model classes is also used by LiveSQL queries and by Nitro SELECTs.

See the [Hello CRUD](../guides/hello-crud.md) example to see all CRUD methods described in this page in action.

## The Layout and Model Classes

The data retrieved by the persistence layer is typically modeled with a separate layout and model classes. See [Database Row Modeling](./database-row-modeling.md) for details.


## The Entity DAOs

DAOs are classes that collect all persistence methods related to a table or view.


### Entity Methods

CRUD automatically generates one DAOs for each included table and view. Each DAO includes basic persistence
methods to `SELECT`, `INSERT`, `UPDATE`, and `DELETE`
on the database.

The generated methods differ between a table and a view:

| Persistence Method | In Tables | In Views | Optimistic Locking |
| -- | :-- | :-- | :--: |
| [Select by Primary Key](./select-by-primary-key.md) | *only when the table has a PK*<br/>`select(pkColumns...)` | N/A | &mdash; |
| [Select by Example](./select-by-example.md) | `select(example)` | `select(example)` | &mdash; |
| [Select by Criteria](./select-by-criteria.md) | `select(t, predicate)` | `select(v, predicate)` | &mdash; |
| [Insert](./insert.md) | `insert(model)` | `insert(model)` | &mdash; |
| [Insert By Example](./insert-by-example.md) | `insert(model)` | `insert(model)` | &mdash; |
| [Update By Primary Key](./update-by-primary-key.md) | *only when the table has a PK:*<br/>`update(model)` | N/A | :heavy_check_mark: |
| [Update by Example](./update-by-example.md) | `update(example, newValues)` | `update(example, newValues)` | &mdash; |
| [Update by Criteria](./update-by-criteria.md) | `update(newValues, t, predicate)` | `update(newValues, v, predicate)` | &mdash; |
| [Delete by Primary Key](./delete-by-primary-key.md) | *only when the table has a PK:*<br/>`delete(pkColumns...)` | N/A | :heavy_check_mark: |
| [Delete by Example](./delete-by-example.md) | `delete(example)` | `delete(example)` | &mdash; |
| [Delete by Criteria](./delete-by-criteria.md) | `delete(t, predicate)` | `delete(v, predicate)` | &mdash; |

**Note**: CRUD does not make a strong differentiation between tables and views. Typically databases do not inform
if a view is updatable or not, so CRUD adds data modification methods to all views. It's up to the developer
to decide if these methods can actually be used on each view or not. By and large databases consider a view updatable if it does have a 1:1 relationship with the underlying *driving* table and the primary key of this
table is available in the result set of the view. This is not written in stone, however, so it's crucial to
consult the specific database documentation to get the final word on it.


### Custom Entity Methods

Extra DAO methods can also be added to the entity DAOs using [Nitro](../nitro/README.md) functionality. A custom method
can be silent (no result set returned) or can return a list of rows. If they return rows, the entity DAO methods are
restricted to return Model objects of the spefic entity (table or view).

To retrieve custom result sets use the general form of Nitro DAOs in using the `<dao>` tags, instead of the bounded form of the Entity DAOs.


## Configuration

When the Discovery Mode is enabled CRUD inspects an entire schema (or schemas), and automatically generates the persistence layer for all tables and views discovered in them.

When the Discovery Mode is disabled CRUD includes only the tables and views explicitly declared in the
[Configuration File](../config/README.md).

CRUD can also mix the Discovery Mode with explicitly declared tables and views. In this case all tables and views from both sources make up the persistence layer; the explicit configuration supersedes the details provided by the discovery mechanism.

For each table and view included, CRUD adds one Layout class, one Model class, and one DAO to the persistence layer. The naming details and the location of the Layout, Model, and DAOs classes can be configured using the `<dao>`, `<layout>`, and `<model>` tags within the [&lt;jdbc>](../config/tags/jdbc.md) tag. The Layout and Model clasess can be further customized with the
[Table](../config/tags/table.md) and [View](../config/tags/view.md) tags as well as
with the [Name Solver](../config/tags/name-solver.md).

The [Type Resolution Mechanics](../guides/type-resolution-mechanics.md) describes the full set of rules that decide property types for all columns.


## Optimistic Locking

CRUD can be configured &ndash; on a per table basis &ndash; to implement optimistic locking. If this is enabled,
then CRUD SELECT queries on the specific table get automatically related to CRUD data modification queries, UPDATE and DELETE.

If changes produced by other threads or processes are detected in the row then the data modification
query is aborted and an exception is thrown, to invalidate the transaction.

See [Optimistic Locking](../config/tags/optimistic-locking.md) for details on how to activate this feature.

## Examples

The following examples are included in the [Hello CRUD](../guides/hello-crud.md) example. They consider a table defined as:

```sql
CREATE TABLE employee (
  id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 500) PRIMARY KEY NOT NULL,
  first_name VARCHAR(20),
  last_name VARCHAR(20) NOT NULL,
  salary INT,
  branch_id INT DEFAULT 101
);
```

### 1. Select by Primary Key

This method is only available in tables with primary keys:

```java
Employee emp = this.employeeDAO.select(150);
```

### 2. Select by Example

This method is available in all tables and views:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employees = this.employeeDAO.select(example);
```

Basic row ordering can also be specified, as in:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employeesb = this.employeeDAO.select(example,
  EmployeeOrderBy.LAST_NAME$DESC,
  EmployeeOrderBy.FIRST_NAME
);
```

### 3. Select by Criteria

This method is available in tables and views. It can be used to search using more complex predicates:

```java
EmployeeTable e = this.employeeDAO.newTable();
List<Employee> employees = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
).execute();
```

This form can be enhanced to use complex ordering, offset, limit, and row locking as in:

```java
EmployeeTable e = this.employeeDAO.newTable();
List<Employee> employees2 = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
  )
  .orderBy(sql.caseWhen(e.branchId.between(2, 4), 0).elseValue(1).end(), e.salary.desc())
  .offset(1)
  .limit(1)
  .forUpdate()
  .skipLocked()
  .execute();
```

### 4. Insert

This method is available in tables and views. It inserts a single row.

For tables, it automatically retrieves the identity primary key value; sequence-generated primary key values can also be retrieved when configured:

```java
Employee emp = new Employee();
emp.setFirstName("Catherine");
emp.setLastName("De Bourgh");
emp.setSalary(140);
Employee inserted = this.employeeDAO.insert(emp);
```

### 5. Insert by Example

This method is available in tables and views. It can be particularly useful in views when we want to intentionally omit a subset of the columns during the insert.

For tables, it automatically retrieves the identity primary key value; sequence-generated primary key values can also be retrieved when configured:

```java
Employee emp = new Employee();
emp.setLastName("Llacolen");
Employee inserted = this.employeeDAO.insertByExample(emp);
```

### 6. Update by Primary Key

This method is only available in tables with primary keys:

```java
Employee emp = this.employeeDAO.select(154);
emp.setSalary(emp.getSalary() + 10);
int count = this.employeeDAO.update(emp);
```

The count value informs us how many rows were actually updated.

### 7. Update by Example

This method is available in all tables and views:

```java
Employee example = new Employee();
example.setBranchId(4);
Employee values = new Employee();
values.setSalary(78);
int count = this.employeeDAO.update(example, values);
```

The count value informs us how many rows were actually updated.

### 8. Update by Criteria

This method is available in tables and views. It can be used to search using more complex predicates:

```java
EmployeeTable e = this.employeeDAO.newTable();
Employee values = new Employee();
values.setBranchId(106);
int count = this.employeeDAO.update(values, e,
    e.branchId.in(6, 16).or(e.salary.ge(105))
).execute();
```

The count value informs us how many rows were actually updated.

### 9. Delete by Primary Key

This method is only available in tables with primary keys:

```java
int count = this.employeeDAO.delete(154);
```

The count value informs us how many rows were actually deleted.

### 10. Delete by Example

This method is available in all tables and views:

```java
Employee example = new Employee();
example.setBranchId(7);
int count = this.employeeDAO.delete(example);
```

The count value informs us how many rows were actually deleted.

### 11. Delete by Criteria

This method is available in tables and views. It can be used to search using more complex predicates:

```java
EmployeeTable e = this.employeeDAO.newTable();
int count = this.employeeDAO.delete(e, //
    e.salary.le(70).or(e.lastName.like("G%")) //
).execute();
```

The count value informs us how many rows were actually updated.






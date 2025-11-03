# CRUD

CRUD provides a simple, straightforward out-of-the-box persistence layer that can be used to start prototyping an app in minutes.

Tables and views are modeled as Layout fully typed and Model classes and their columns are modeled as properties of these. These are used by CRUD, LiveSQL queries, and Nitro SELECTs.

The persistence methods to retrieve and update data are modeled in separate Data Access Objects (DAOs) with methods for each one. These include different variations of the traditional SELECT, INSERT, UPDATE, and DELETE statements according to the specifics of each table or view in the database.

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
| [Select by Primary Key](#1-select-by-primary-key) | `select(pkColumns...)`<br/>*only when the table has a PK* | N/A | &mdash; |
| [Select by Example](#2-select-by-example) | `select(example)` | `select(example)` | &mdash; |
| [Select by Criteria](#3-select-by-criteria) | `select(t, predicate)` | `select(v, predicate)` | &mdash; |
| [Insert](#4-insert) | `insert(model)` | `insert(model)` | &mdash; |
| [Insert By Example](#5-insert-by-example) | `insertByExample(model)` | `insertByExample(model)` | &mdash; |
| [Update By Primary Key](#6-update-by-primary-key) | `update(model)`<br/>*only when the table has a PK* | N/A | :heavy_check_mark: |
| [Update by Example](#7-update-by-example) | `update(example, newValues)` | `update(example, newValues)` | &mdash; |
| [Update by Criteria](#8-update-by-criteria) | `update(newValues, t, predicate)` | `update(newValues, v, predicate)` | &mdash; |
| [Delete by Primary Key](#9-delete-by-primary-key) | `delete(pkColumns...)`<br/>*only when the table has a PK* | N/A | :heavy_check_mark: |
| [Delete by Example](#10-delete-by-example) | `delete(example)` | `delete(example)` | &mdash; |
| [Delete by Criteria](#11-delete-by-criteria) | `delete(t, predicate)` | `delete(v, predicate)` | &mdash; |

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

CRUD can be configured &ndash; on a per table basis &ndash; to implement optimistic locking. If this is enabled, then CRUD SELECT queries on the specific table get automatically related to CRUD data modification queries, UPDATE and DELETE.

If changes produced by other threads or processes are detected in the row then the data modification
query is aborted and an exception is thrown, to invalidate the transaction.

See [Optimistic Locking](../config/tags/optimistic-locking.md) for details on how to enable one of the available strategies for one or more tables.

## CRUD Methods

The CRUD methods are described below consider the following table as an example:

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

This method selects a single row from a table that has a primary key. A such, this method is only available in tables with primary keys.

The simplest form this method takes is shown below:

```java
Employee emp = this.employeeDAO.select(150);
```

#### Composite Primary Keys

The parameter(s) in the call corresponds to the list of primary key values. If the table has a composite primary key, then multiple parameters are added. The parameter types will correspond to the specific app types for the columns according to the Type Resolution Mechanics.

If the table has a composite key with three columns (VARCHAR, DATE, INTEGER) this method could look like:

```java
Payment p = this.paymentDAO.select("INT", LocalDate.of(2025, 10, 23), 7);
```


### 2. Select by Example

This method retrieves a list of rows from a table or view, using an example of the values of its columns. All the values specified as non-null are used to search for rows using an AND predicate.

This method is available in all tables with or without primary key and also in all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employees = this.employeeDAO.select(example);
```

#### Basic Ordering

This method has a second variation that can specify a basic row ordering by adding one or more column ordering parameters, as in:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employeesb = this.employeeDAO.select(example,
  EmployeeOrderBy.LAST_NAME$DESC,
  EmployeeOrderBy.FIRST_NAME
);
```

If a more advanced row ordering is needed, the next method "Select by Criteria" offers more options.

### 3. Select by Criteria

This method retrieves a list (or streaming cursor) of rows from a table or view, using [LiveSQL Expressions](../livesql/syntax/expressions.md). These are full LiveSQL expressions that can include math operations, date algebra, string manipulation, functions, etc.

This method is available in all tables with or without primary key and also in all views.

For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
List<Employee> employees = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
).execute();
```

#### Adding More Options

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

#### Using Cursors

This method can also use a `Cursor<>` to stream rows from the database. Compared with a `List<>` that retrieves the entire set of rows in memory, a cursor will only retrieve rows in small subsets and will buffer them accordingly. You app won't incurr in heavy memory usage even when processing a very high number of rows.

A cursor is enabled by using `.executeCursor()` instead of the simple form `.execute()`. For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
Cursor<Employee> employees = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
).executeCursor();
for (Employee emp : employees) {
  // Do something with the employee "emp"
  // Notice we don't create a List<> or other collection; we just process the rows one by one
}
```

Finally, if your query needs more flexible functionality you can use LiveSQL SELECT queries (with or without tuples functionality) to define complex queries. Or... resort to fully typing Nitro queries that offer access to the full SQL language dialect. These options fall outside the scope of CRUD, however.

### 4. Insert

This method inserts a single row in a table or view.

It's available in all tables and views.

The application prepares the data to insert in a Layout object and then uses the `insert(<layout>)` to insert the row, as shown below:

```java
Employee emp = new Employee();
emp.setFirstName("Catherine");
emp.setLastName("De Bourgh");
emp.setSalary(140);
Employee inserted = this.employeeDAO.insert(emp);
```

All columns are used in this form of the INSERT functionality. Columns with null values will be inserted as nulls.

#### Primary Key Auto-Generation

Auto-generated columns are columns that are generated by the database automatically on each insert.
They can take three main forms:

- `IDENTITY GENERATED ALWAYS`: The database always generates a value for this column. The `INSERT`
functionality cannot specify it. No extra configuration needed; CRUD automatically finds out which columns have this property
- `IDENTITY GENERATED BY DEFAULT`: The database generates this value when the `INSERT` functionality
does not include it, or when it includes it with a null. PostgreSQL's `SERIAL` types and MySQL's
`AUTO_INCREMENT` feature fall into this category. No extra configuration needed; CRUD automatically finds out which columns have this property
- SEQUENCES: Extra configuration needs to be added to tell CRUD which sequence to use for each table. See the `sequence` attribute of the `<column>` tag in the [Configuration File Reference](../config/README.md) to specify it

There are two extra cases, that sometimes can be related to primary key auto-generation:

- `GENERATED AS` clauses: CRUD does not populate back the values of these columns. An extra explicit `SELECT` can be used to retrieve their values
- `DEFAULT` constraints: CRUD does not populate back the values of these columns. An extra explicit `SELECT` can be used to retrieve their values

Now, when inserting a row in a table with an auto-generated primary key this method automatically retrieves it value in the resulting model object.

As shown above the IDENTITY strategies have two forms:

- If a table column is marked as `IDENTITY GENERATED ALWAYS` the value provided by the apps in the Layout object the is ignored and populated back from the database once the row is inserted
- If a table column is marked as `IDENTITY GENERATED BY DEFAULT` there are two cases:
    - If the value provided by the Layout object is null, this value is populated back from the database once the row is inserted
    - If the value provided by the Layout object is not null this value is used as an insertion value


The following table describes which strategies are implemented for primary key auto-generation when inserting in each database:

| Database | Identities | Sequences |
| -- | -- | -- |
| Oracle     | Yes | Yes, with configuration |
| DB2 LUW    | Yes | Yes, with configuration |
| PostgreSQL | Yes, including SERIAL types*1 | Yes, with configuration |
| SQL Server | Yes*2 | No |
| MySQL      | Yes, including AUTO_INCREMENT clauses | No |
| MariaB     | Yes, including AUTO_INCREMENT clauses  | No |
| SAP ASE    | Yes*3 | Yes, with configuration |
| H2         | Yes | Yes, with configuration |
| HyperSQL   | Yes | Yes, with configuration |
| Derby      | Yes | Yes, with configuration |

*1 PostgreSQL implements identities for any column, not necessarily primary keys only. Identity value retrieval is only implemented for the primary key identity column, if available; other identity columns are not retrieved

*2 SQL Server only implements IDENTITY ALWAYS; the BY DEFAULT variation is not supported and the insert operation will fail if a PK value is provided

*3 Explicit PK value for an IDENTITY column was not implemented; the insert operation will fail if a PK value is provided on an IDENTITY column

#### Inserting through a View

Consider that it's possible to insert rows through a view, but this depends in the specifics of each
database. The most common rules database impose to do this have to do with two aspects:

- The view must have a non-aggregated driving table.
- The primary key of it should be available as a column in the view.
- If the view has a filtering condition, the database may enforce the rule that the inserted row must be valid according to it.
- The underlying driving table should not implement primary key auto-generation logic such as `IDENTITY`.

### 5. Insert by Example

This method inserts a single row in a table or view, omitting unspecified columns so table level DEFAULT constraints or GENERATE clauses can operate.

It's available in all tables and views.

The application prepares the data to insert in a Layout object and then uses the `insert(<layout>)` to insert the row, as shown below:

```java
Employee emp = new Employee();
emp.setLastName("Llacolen");
Employee inserted = this.employeeDAO.insertByExample(emp);
```

If an auto-generated primary key is active in the table, it's retrieved in the resulting Model object. When inserting on a view, the auto-generated primary key value is not retrieved.

The omitted columns are not retrieved automatically even if table level constraints populated them; if the app would need to execute an extra SELECT to retrieve them.

#### Inserting through a View

See the section [Inserting through a View](#inserting-through-a-view) above.


### 6. Update by Primary Key

This method updates a single row in a table that has a primary key. As such, this method is only available in tables with primary keys.

For example:

```java
Employee emp = this.employeeDAO.select(154);
emp.setSalary(emp.getSalary() + 10);
int count = this.employeeDAO.update(emp);
```

The count value informs us how many rows were actually updated.

The method works in the same way for tables with composite primary keys. The corresponding columns are used to locate and update the row.

This method cannot be used to update the primary key of a row. However, you can use the update by example functionality described below to do so.

#### Optimistic Locking

If any Optimistic Locking strategy was enabled in the table the update method will detect row changes or row deletion automatically. For example:

```java
InvoiceVO inv = InvoiceDAO.select(58640);
inv.setStatus("PAID");
try {
  this.invoiceDAO.update(inv);
} catch (RuntimeException e) {
  System.out.println("Concurrent invoice change detected. Invoice not deleted.");
}
```

In the case shown above the logic detects the row change (maybe someone changed the invoice or deleted it) and the current update fails.

### 7. Update by Example

This method updates multiple rows on a table or view, using an example of the values of its columns to search and another example to update them. In short:

- Only the non-null values in the *example* are used to search for rows, using an AND predicate.
- Only the non-null values in the *values* parameter are updated; the other columns of the selected rows are left intact.

This method is available in all tables with or without primary key and also in all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(4);
Employee values = new Employee();
values.setSalary(78);
int count = this.employeeDAO.update(example, values);
```

The count value informs us how many rows were actually updated.

This method can be used to update the primary key of a table.

### 8. Update by Criteria

This method updates multiple rows on a table or view, using [LiveSQL Expressions](../livesql/syntax/expressions.md). These are full LiveSQL expressions that can include math operations, date algebra, string manipulation, functions, etc.

This method is available in all tables with or without primary key and also in all views.

For example:

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

This method deleted a single row in a table that has a primary key. As such, this method is only available in tables with primary keys.

For example:

```java
int count = this.employeeDAO.delete(154);
```

The count value informs us how many rows were actually deleted.

If the table has a composite primary key, then the method includes multiple parameters, one per primary key column.

#### Optimistic Locking

If any Optimistic Locking straqtey was enabled in the table the delete method will detect row changes or row deletion automatically. For example:

```java
InvoiceVO inv = InvoiceDAO.select(58640);
try {
  this.invoiceDAO.delete(58640);
} catch (RuntimeException e) {
  System.out.println("Concurrent invoice change detected. Invoice not deleted.");
}
```

In the case shown above the logic detects the row change (maybe someone changed the invoice or deleted it) and the current delete fails.

### 10. Delete by Example

This method deletes multiple rows on a table or view, using an example of the values of its columns. All the values specified as non-null are used to search for rows using an AND predicate.

This method is available in all tables with or without primary key and also in all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(7);
int count = this.employeeDAO.delete(example);
```

The count value informs us how many rows were actually deleted.

### 11. Delete by Criteria

This method deletes multiple rows on a table or view, using [LiveSQL Expressions](../livesql/syntax/expressions.md). These are full LiveSQL expressions that can include math operations, date algebra, string manipulation, functions, etc.

This method is available in all tables with or without primary key and also in all views.

For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
int count = this.employeeDAO.delete(e,
    e.salary.le(70).or(e.lastName.like("G%"))
).execute();
```

The count value informs us how many rows were actually updated.






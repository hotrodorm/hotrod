# CRUD

CRUD offers a simple and efficient out-of-the-box persistence layer that enables rapid prototyping of applications in just minutes.

In this framework, tables and views are represented as fully typed Layout and Model classes, with their columns modeled as properties. These components are utilized by CRUD, LiveSQL queries, and Nitro SELECT statements.

The methods for data retrieval and updates are organized within separate Data Access Objects (DAOs), with dedicated methods for each operation. These structures encompass various implementations of traditional SQL statements: SELECT, INSERT, UPDATE, and DELETE, tailored to the specifics of each table or view in the database.

See the [Hello CRUD](../guides/hello-crud.md) example to see all CRUD methods described in this page in action.

## The Layout and Model Classes

The data retrieved by the persistence layer is typically modeled with a separate layout and model classes. See [Database Row Modeling](./database-row-modeling.md) for details.

## SQL Injection Safety

CRUD is always safe from SQL Injection. The CRUD methods always apply JDBC parameters, and never concatenate them.

## The Entity DAOs

DAOs are classes that collect all persistence methods related to a table or view.


### Entity Methods

CRUD automatically generates a Data Access Object (DAO) for each included table and view. Each DAO includes the basic persistence methods to perform SELECT, INSERT, UPDATE, and DELETE operations on the database.

The methods generated vary depending on whether they are associated with a table or a view:

| Persistence Method | In Tables | In Views | Optimistic Locking |
| -- | :-- | :-- | :--: |
| [Select by Primary Key](#1-select-by-primary-key) | `select(pkColumns...)`<br/>*only when the table has a PK* | N/A | &mdash; |
| [Select by Example](#2-select-by-example) | `select(example)` | `select(example)` | &mdash; |
| [Select by Criteria](#3-select-by-criteria) | `select(t, predicate)` | `select(v, predicate)` | &mdash; |
| [Insert](#4-insert) | `insert(model)` | `insert(model)` | &mdash; |
| [Insert By Example](#5-insert-by-example) | `insertByExample(model)` | `insertByExample(model)` | &mdash; |
| [Update By Primary Key](#6-update-by-primary-key) | `update(model)`<br/>*only when the table has a PK* | N/A | Available |
| [Update by Example](#7-update-by-example) | `update(example, newValues)` | `update(example, newValues)` | &mdash; |
| [Update by Criteria](#8-update-by-criteria) | `update(newValues, t, predicate)` | `update(newValues, v, predicate)` | &mdash; |
| [Delete by Primary Key](#9-delete-by-primary-key) | `delete(pkColumns...)`<br/>*only when the table has a PK* | N/A | Available |
| [Delete by Example](#10-delete-by-example) | `delete(example)` | `delete(example)` | &mdash; |
| [Delete by Criteria](#11-delete-by-criteria) | `delete(t, predicate)` | `delete(v, predicate)` | &mdash; |

CRUD does not explicitly differentiate between tables and views. Typically, databases do not indicate whether a view is updatable, so CRUD includes data modification methods for all views. It is the developer's responsibility to determine whether these methods are applicable to each view.

In general, databases consider a view updatable if it maintains a 1:1 relationship with the underlying driving table and the primary key of this table is present in the view's result set. However, this guideline is not absolute, so it is essential to consult the specific database documentation for definitive information.


### Custom Entity Methods

Additional DAO methods can be added to the entity DAOs using the functionality provided by [Nitro](../nitro/README.md). Custom methods can be silent (not returning a result set) or can return a list of rows. When rows are returned, the entity DAO methods are restricted to returning Model objects corresponding to the specific entity (either table or view).

To retrieve custom result sets, you can utilize the general form of Nitro DAOs available in the `<dao>` tags, rather than the bounded form of the Entity DAOs.


## Configuration

When Discovery Mode is enabled, CRUD inspects an entire schema (or multiple schemas) and automatically generates the persistence layer for all discovered tables and views.

When Discovery Mode is disabled, CRUD includes only the tables and views that are explicitly declared in the [Layer Configuration File](../config/README.md).

CRUD also allows for a combination of Discovery Mode with explicitly declared tables and views. In this scenario, all tables and views from both sources form the persistence layer, with explicit configurations taking precedence over those provided by the discovery mechanism.

For each included table and view, CRUD adds one Layout class, one Model class, and one DAO to the persistence layer. The naming details and locations of the Layout, Model, and DAO classes can be configured using the `<dao>`, `<layout>`, and `<model>` tags within the [&lt;jdbc>](../config/tags/jdbc.md) tag. Additionally, the Layout and Model classes can be further customized using the [Table](../config/tags/table.md) and [View](../config/tags/view.md) tags, as well as the [Name Solver](../config/tags/name-solver.md).

The [Type Resolution Mechanics](../guides/type-resolution-mechanics.md) outlines the complete set of rules that determine property types for all columns.

## Optimistic Locking

CRUD can be configured &mdash; on a per-table basis &mdash; to implement optimistic locking. When this feature is enabled, CRUD SELECT queries on the specified table are automatically linked to data modification queries, specifically UPDATE and DELETE.

If changes made by other threads or processes are detected in the row, the data modification query is aborted, and an exception is thrown to invalidate the transaction.

For details on how to enable one of the available strategies in the CRUD layer for one or more tables, see [Optimistic Locking](../config/tags/optimistic-locking.md).

## CRUD Methods

The CRUD methods described below consider the following table as an example:

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

This method retrieves a single row from a table that has a primary key. As such, it is only available for tables with defined primary keys.

The simplest form this method can take is illustrated below:

```java
Employee emp = this.employeeDAO.select(150);
```

#### Composite Primary Keys

The parameter(s) in the call correspond to the list of primary key values. If the table has a composite primary key, multiple parameters will be included. The parameter types will align with the specific application types for the columns, as defined by the Type Resolution Mechanics.

For example, for a table with a composite key consisting of three columns (VARCHAR, DATE, INTEGER), this method can be used as follows:

```java
Payment p = this.paymentDAO.select("AMT", LocalDate.of(2025, 10, 23), 7);
```


### 2. Select by Example

This method retrieves a list of rows from a table or view based on example values for its columns. All non-null values specified are used to search for matching rows with an AND predicate.

This method is available for all tables, regardless of whether they have a primary key, as well as for all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employees = this.employeeDAO.select(example);
```

#### Basic Ordering

This method includes a variation that allows you to specify basic row ordering by adding one or more column ordering parameters, as shown below:

```java
Employee example = new Employee();
example.setBranchId(2);
List<Employee> employeesb = this.employeeDAO.select(example,
  EmployeeOrderBy.LAST_NAME$DESC,
  EmployeeOrderBy.FIRST_NAME
);
```

If more advanced row ordering is required, the next method, "Select by Criteria," provides more flexible options.

### 3. Select by Criteria

This method retrieves a list (or streaming cursor) of rows from a table or view using [LiveSQL Expressions](../livesql/syntax/expressions.md). These expressions can include a variety of operations, including mathematical calculations, date algebra, string manipulation, and functions.

This method is applicable to all tables, regardless of whether they have a primary key, as well as to all views.

For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
List<Employee> employees = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
).execute();
```

#### Adding More Options

This method can be enhanced to incorporate complex ordering, offset, limit, and row locking, as demonstrated below:

```java
EmployeeTable e = this.employeeDAO.newTable();
List<Employee> employees2 = this.employeeDAO.select(e,
    e.salary.between(70, 100).and(e.lastName.like("%t%"))
  )
  .orderBy(sql.caseWhen(e.branchId.between(2, 4), 0).elseValue(1).end(), e.salary.desc())
  .offset(200)
  .limit(50)
  .forUpdate()
  .skipLocked()
  .execute();
```

#### Using Cursors

This method can utilize a `Cursor<>` to stream rows from the database. Unlike a `List<>`, which retrieves the entire set of rows into memory, a cursor retrieves rows in smaller subsets and buffers them accordingly. This approach helps your application avoid heavy memory usage, even when processing a large number of rows.

To enable a cursor, use `.executeCursor()` instead of the standard `.execute()` method. For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
Cursor<Employee> employees = this.employeeDAO
    .select(e,
      e.salary.between(70, 100).and(e.lastName.like("%t%"))
    ).executeCursor();
for (Employee emp : employees) {
  // Do something with the employee "emp"
  // Notice we don't create a List<> or other collection; we just process the rows one by one
}
```

Finally, if your query requires more flexible functionality, you can use LiveSQL SELECT queries (with or without tuples) to define complex queries. Alternatively, you can opt for fully typed Nitro queries, which provide access to the entire SQL language dialect. However, these options fall outside the scope of CRUD.

### 4. Insert

This method inserts a single row into a table or view.

It is available for all tables and views.

The application prepares the data for insertion using a Layout object and then calls `insert(<layout>)` to add the row, as illustrated below:

```java
Employee emp = new Employee();
emp.setFirstName("Catherine");
emp.setLastName("De Bourgh");
emp.setSalary(140);
Employee inserted = this.employeeDAO.insert(emp);
```

In this form of the INSERT functionality, all columns are used. Columns with null values will be inserted as nulls.

#### Primary Key Auto-Generation

Auto-generated columns are those that the database automatically generates with each insert. They can take three main forms:

- IDENTITY GENERATED ALWAYS: The database always generates a value for this column, and the INSERT functionality cannot override it. No additional configuration is needed and CRUD automatically detects which columns have this property.
- IDENTITY GENERATED BY DEFAULT: The database generates a value for this column when the INSERT functionality does not include it or includes it as null. PostgreSQL's SERIAL types and MySQL's AUTO_INCREMENT features fall into this category. No extra configuration is required an CRUD automatically identifies these columns.
- SEQUENCES: Additional configuration is necessary to inform CRUD which sequence to use for each table. Refer to the sequence attribute of the `<column>` tag in the [Configuration File Reference](../config/README.md) for specification.

There are two additional cases that may be related sometimes to primary key auto-generation:

- GENERATED AS clauses: CRUD does not populate the values of these columns directly. An additional SELECT can be used to retrieve their values.
- DEFAULT constraints: CRUD does not populate the values of these columns either. Again, an explicit SELECT is required to retrieve their values.

Now, when inserting a row into a table with an auto-generated primary key, this method automatically retrieves its value in the resulting model object.

As mentioned above, the IDENTITY strategies have two forms:

- If a table column is marked as IDENTITY GENERATED ALWAYS, the value provided by the application in the Layout object is ignored, and the value is populated from the database after the row is inserted.
- If a table column is marked as IDENTITY GENERATED BY DEFAULT, there are two scenarios:
    - If the value provided by the Layout object is null, this value is populated from the database after the row is inserted.
    - If the value provided by the Layout object is not null, this value is used for insertion.

The following table outlines the strategies implemented for primary key auto-generation when inserting in each database:

| Database | Identities | Sequences |
| -- | -- | -- |
| Oracle     | Yes | Yes, with configuration |
| DB2 LUW    | Yes | Yes, with configuration |
| PostgreSQL | Yes, including SERIAL types*1 | Yes, with configuration |
| SQL Server | Yes*2 | Yes, with configuration |
| MySQL      | Yes, including AUTO_INCREMENT clauses | No |
| MariaB     | Yes, including AUTO_INCREMENT clauses  | No |
| SAP ASE    | Yes*3 | Yes, with configuration |
| H2         | Yes | Yes, with configuration |
| HyperSQL   | Yes | Yes, with configuration |
| Derby      | Yes | Yes, with configuration |

*1 PostgreSQL implements identities for any column, not limited to primary keys. Identity value retrieval is only applicable for the primary key identity column, if available; other identity columns are not retrieved.

*2 SQL Server only supports IDENTITY ALWAYS; the BY DEFAULT variation is not supported. The insert operation will fail if a primary key value is provided.

*3 An explicit primary key value for an IDENTITY column is not implemented; the insert operation will fail if a primary key value is provided for an IDENTITY column.

#### Inserting through a View

It is possible to insert rows through a view, but this depends on the specifics of each database. The most common rules imposed by databases for this operation include the following aspects:

- The view must have a non-aggregated driving table.
- The primary key of the driving table should be available as a column in the view.
- If the view includes a filtering condition, the database may enforce that the inserted row must comply with this condition.
- The underlying driving table should not implement primary key auto-generation logic, such as IDENTITY.

### 5. Insert by Example

This method inserts a single row into a table or view, omitting unspecified columns to allow table-level DEFAULT constraints or GENERATE clauses to operate.

It is available for all tables and views.

The application prepares the data for insertion using a Layout object and then calls `insert(<layout>)` to insert the row, as illustrated below:

```java
Employee emp = new Employee();
emp.setLastName("Llacolen");
Employee inserted = this.employeeDAO.insertByExample(emp);
```

If an auto-generated primary key is active in the table, its value is retrieved in the resulting Model object. Nevertheless, when inserting through a view, the auto-generated primary key value is not retrieved.

The omitted columns are not automatically retrieved, even if table-level constraints populate them; the application must execute an additional SELECT to retrieve these values.

#### Inserting through a View

See the section [Inserting through a View](#inserting-through-a-view) above.


### 6. Update by Primary Key

This method updates a single row in a table that include a primary key. Therefore, it is only available for tables that have defined primary keys.

For example:

```java
Employee emp = this.employeeDAO.select(154);
emp.setSalary(emp.getSalary() + 10);
int count = this.employeeDAO.update(emp);
```

The count value indicates how many rows were actually updated.

This method functions similarly for tables with composite primary keys, using the corresponding columns of the composite key to locate and update the row.

Note that this method cannot be used to update the primary key of a row. However, you can utilize the update by example functionality described below to achieve this.

#### Optimistic Locking

If any Optimistic Locking strategy is enabled for the table, the update method will automatically detect row changes or deletions. For example:

```java
InvoiceVO inv = InvoiceDAO.select(58640);
inv.setStatus("PAID");
try {
  this.invoiceDAO.update(inv);
} catch (RuntimeException e) {
  System.out.println("Concurrent invoice change detected. Invoice not deleted.");
}
```

In the scenario described above, the logic detects a row change (such as another user modifying or deleting the invoice), resulting in the current update failing.

### 7. Update by Example

This method updates multiple rows in a table or view by using an example of the column values to search for rows, and another example to define the values for the update. In summary:

- Only the non-null values in the example are used to search for rows, leveraging an AND predicate.
- Only the non-null values in the values parameter are updated; other columns of the selected rows remain unchanged.

This method is available for all tables, regardless of whether they have a primary key, as well as for all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(4);
Employee values = new Employee();
values.setSalary(78);
int count = this.employeeDAO.update(example, values);
```

The count value indicates how many rows were actually updated.

This method can also be used to update the primary key of a table.

### 8. Update by Criteria

This method updates multiple rows in a table or view using [LiveSQL Expressions](../livesql/syntax/expressions.md). These expressions can include a variety of operations, including mathematical calculations, date algebra, string manipulation, and functions.

This method is applicable to all tables, regardless of whether they have a primary key, as well as to all views.

For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
Employee values = new Employee();
values.setBranchId(106);
int count = this.employeeDAO.update(values, e,
    e.branchId.in(6, 14).or(e.salary.ge(105))
).execute();
```

The count value indicates how many rows were successfully updated.

### 9. Delete by Primary Key

This method deletes a single row from a table that has a primary key. Therefore, it is only available for tables with defined primary keys.

For example:

```java
int count = this.employeeDAO.delete(154);
```

The count value indicates how many rows were successfully deleted.

If the table has a composite primary key, the method includes multiple parameters, one for each primary key column.

#### Optimistic Locking

If any Optimistic Locking strategy is enabled for the table, the delete method will automatically detect row changes or deletions. For example:

```java
InvoiceVO inv = InvoiceDAO.select(58640);
try {
  this.invoiceDAO.delete(58640);
} catch (RuntimeException e) {
  System.out.println("Concurrent invoice change detected. Invoice not deleted.");
}
```

In the scenario described above, the logic detects a row change (such as someone modifying or deleting the invoice), resulting in the current delete operation failing.

### 10. Delete by Example

This method deletes multiple rows from a table or view by using an example of the column values. All non-null values specified are employed to search for rows using an AND predicate.

This method is available for all tables, regardless of whether they have a primary key, as well as for all views.

For example:

```java
Employee example = new Employee();
example.setBranchId(7);
int count = this.employeeDAO.delete(example);
```

The count value indicates how many rows were successfully deleted.

### 11. Delete by Criteria

This method deletes multiple rows from a table or view using [LiveSQL Expressions](../livesql/syntax/expressions.md). These expressions can encompass a variety of operations, including mathematical calculations, date algebra, string manipulation, and functions.

This method is applicable to all tables, regardless of whether they have a primary key, as well as to all views.

For example:

```java
EmployeeTable e = this.employeeDAO.newTable();
int count = this.employeeDAO.delete(e,
    e.salary.le(70).or(e.lastName.like("G%"))
).execute();
```

The count value indicates how many rows were successfully deleted.





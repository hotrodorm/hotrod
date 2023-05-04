# The SELECT List 

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

LiveSQL includes variations to specify all or a subset of the columns and also to qualify the query for DISTINCT rows only. See the variations below.

- [The Typical SELECT List](#the-typical-select-list)
- [The SQL Wildcard](#the-sql-wildcard)
- [Using DISTINCT](#using-distinct)
- [Selecting Without a Table](#selecting-without-a-table)

## The Typical SELECT List

A typical query can include all the columns of a table or a subset of them. It can also alias them as needed, and include extra expressions computed on the fly. These variation are supported easily as shown below.

### Selecting All Columns

To select all columns of the table(s) start the query with `select()`. For example:

```java
EmployeeTable e = EmployeeDAO.newTable("e");

List<Row> rows = this.sql
    .select()
    .from(e) 
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM employee e
```


### Selecting Specific Columns

To select a subset of the columns of the table(s) start the query with:

```java
ProductTable p = ProductDAO.newTable("p");

List<Row> rows = this.sql
    .select(p.name, p.price.mult(p.qty), a.status)
    .from(p) 
    .execute();
```

Produces:

```sql
SELECT p.name, p.price * p.qty, p.status
FROM product p
```

The query can name the specific list of columns to produce. This list can also include expressions that the database can 
compute using functions and operators. 


### Aliasing Columns

Most of the time SELECT queries preserve the names of the columns they are retrieving. However, naming or renaming columns
can be useful to convey more clearly the meaning of a column value. This is specially useful when an expression is included in the 
select list where each engine typically generates a *cryptic* or random name for it, or when joining tables where multiple columns can end up 
having the same name.

To alias a column append the `.as(<string>)` method to it.

For example, if the query is adding the base price with the tax for the purchase, it looks much clearer to name the new
column as `total_price`, as shown below:

```java
ProductTable p = ProductDAO.newTable("p");

List<Row> rows = this.sql
    .select(p.name, p.price.plus(p.tax).as("total_price"))
    .from(p) 
    .execute();
```

Produces a second column with a clear name:

```sql
SELECT p.name, p.price + p.tax as total_price
FROM product p
```

## The SQL Wildcard

The SQL wildcard (`*`) is used to include all columns of a table or view. LiveSQL enhances its
functionality with filtering and aliasing. The latter can prove to be very useful when retrieving
the result set as a tuple of VOs.


### Selecting All Columns Of A Table

To select all columns of a table or view use the method `.star()`. For example:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
DepartmentTable d = DepartmentDAO.newTable("d");

List<Row> rows = this.sql
    .select(e.star(), d.deptName)
    .from(e)
    .join(d, d.id.eq(e.departmentId)
    .execute();
```

LiveSQL expands the wildcards and replaces them with the corresponding columns to apply property aliases. The resulting query is:

```sql
SELECT
  e.id as "id", e.first_name as "firstName", e.last_name as "lastName",
  d.dept_name as "deptName"
FROM employee e
JOIN department d ON d.id = e.department_id
```

### Filtering Columns

If we only want a subset of the columns referenced by a `*` symbol we can use the `.filter(<predicate>)` method to exclude 
some of them. For example, to include only columns of type `INTEGER` or `DECIMAL` of the table `EMPLOYEE` we can do:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
DepartmentTable d = DepartmentDAO.newTable("d");

List<Row> rows = this.sql
    .select(
      e.star()
       .filter(c -> "INTEGER".equals(c.getType()) || "DECIMAL".equals(c.getType())),
      d.deptName
    )
    .from(e)
    .join(d, d.id.eq(e.departmentId)
    .execute();
```

The resulting query is:

```sql
SELECT
  e.id as "id", e.department_id as "departmentId", -- only 'id' and 'department_id' are numeric
  d.dept_name as "deptName"
FROM employee e
JOIN department d ON d.id = e.department_id
```

The following column properties can be used in the filtering predicate:

| Property Getter | Description | Example |
| -- | -- | -- |
| .getName() | Canonical (official) name of the column | `CURRENT_BALANCE` |
| .getType() | Column type as informed by the database | `DECIMAL` |
| .getColumnSize() | Width of the column | `14` |
| .getDecimalDigits() | Decimal places (if any) | `2` |
| .getProperty() | The corresponding property name generated for Java | `currentBalance` |
| .getCatalog() | The catalog (if any) of the table or view | `null` |
| .getSchema() | The schema (if any) of the table or view | `CLIENT` |
| .getObjectName() | The canonical (official) name of the table or view | `CHECKING_ACCOUNT` |
| .getObjectInstance().getAlias() | The table or view alias in the query | `a` |
| .getObjectInstance().getType() | The table or view canonical (official) type as informed by the database | `TABLE` |

The example value above could correspond to the column `current_balance` in the table creation shown below. Details may vary from database to database:

```sql
create table client.checking_account (
  id int primary key not null,
  current_balance decimal(14, 2)
);
```

### Aliasing Columns

It can be useful to change the name of the columns from one table to differentiate
from the columns of another one, specially when joined tables have columns with the same name.

If we use the function `.star()` to select all the columns of one table we can alias them by using 
the `.as()` method on them. For example:

```java
InvoiceTable i = InvoiceDAO.newTable("i");
BranchTable b = BranchDAO.newTable("b");

ExecutableSelect<Row> q = this.sql
    .select(
      i.star().as(c -> "in#" + c.getProperty()),
      b.star().as(c -> "br#" + c.getProperty())
    )
    .from(i)
    .join(b, b.id.eq(i.branchId))
    .where(i.type.like("CK%"));
System.out.println("q:" + q.getPreview()); // to see the actual query
List<Row> rows = q.execute();

for (Row r : rows) {
  InvoiceVO ivo = this.invoiceDAO.parseRow(r, "in#");
  BranchVO bvo = this.branchDAO.parseRow(r, "br#");
}
```

Produces a query with the form:

```sql
SELECT
  i.id as "in#id", i.amount as "in#amount", i.branch_id as "in#branchId", i.created as "in#created"
  b.id as "br#id", b.name as "br#name", b.created as "br#created"
FROM public.invoice i
JOIN public.branch b ON b.id = i.branch_id
WHERE i.type like 'CK%'
```

While both tables have a column with the name `created`, the query distinguishes them using different aliases: `in#created` and `br#created` respectively.

Renaming the columns with different prefixes (and/or suffixes) for each table allows the `parseRow()` method to classify them back to 
the corresponding VOs. In this case `getProperty()` produced the property name that `parseRow()` expects. You can use
`getName()` to produce the raw column name instead.

Note that more complex logic can be used. All properties mentioned in the table above are available
to write more complex functions.


### Filtering and Aliasing Columns

Finally, aliasing columns can be combined with filtering. For example, the select list can take the form:

```java
List<Row> rows = this.sql
    .select(
      e.star()
       .filter(c -> !"BLOB".equals(c.getType()))
       .as(c -> "emp_" + c.getName())
    )
    .from(e)
    .execute();
```

In this case, the filter removes the BLOB columns. The remaining columns are aliased.


## Using DISTINCT

By default a SELECT query produce rows as found in the database and this could include duplicate rows: i.e. it returns a *multiset*.
However, in some circumstances we may need the query to remove duplicate rows: i.e. to return a *set*. Removing duplicate
rows is an extra effort the engine needs to do, and we indicate this by prepending the `DISTINCT` clause to the select list.


### DISTINCT Over All Columns

We can apply DISTINCT to all the columns of a table, as shown below:

```java
VehicleTable v = VehicleDAO.newTable("v");

List<Row> rows = this.sql
    .selectDistinct()
    .from(v) 
    .execute();
```

Produces:

```sql
SELECT DISTINCT *
FROM vehicle v
```

### DISTINCT Over A Subset Of Columns

The `DISTINCT` qualifier can also be combined with a specific list of columns:

```java
VehicleTable v = VehicleDAO.newTable("v");

List<Row> rows = this.sql
    .selectDistinct(v.brand, v.region)
    .from(v) 
    .execute();
```

Produces:

```sql
SELECT DISTINCT v.brand, v.region
FROM vehicle v
```

## Selecting Without A Table

Some database engines require a `FROM` clause in a `SELECT` statement. Other ones, such as
PostgreSQL, MySQL, MariaDB, and SQL Server, can execute a `SELECT` without a `FROM` clause if using literals in the select list, or
if the values can be computed directly without using any table. For example, the following query is valid in SQL Server:

```java
List<Row> rows = this.sql
    .select(
      sql.val(7),
      sql.val(15).mult(sql.val(3)), 
      sql.currentDate()
    )
    .execute();
```

The resulting query is:

```sql
SELECT 7, 15 * 3, getdate()
```

**Note**: Oracle, DB2, and Apache Derby cannot select without a `FROM` clause. In these databases you can use
[the DUAL and SYSDUMMY1 tables](./systables.md).


Next: [The FROM and JOIN Clauses](./from-and-joins.md)

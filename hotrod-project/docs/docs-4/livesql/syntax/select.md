# The SELECT Clause 

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

LiveSQL includes variations to specify all or a subset of the columns and also to qualify the query for DISTINCT rows only. See the variations below.


## Selecting All Columns

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


## Selecting Specific Columns

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
compute using functions or operators. 


## Aliasing Columns

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
SELECT p.name, p.price + p.qty as total_price
FROM product p
```


## Selecting All Columns From One Table &ndash; The * Wildcard

To select all columns of a table or view use the method `.star()`. For example:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
DepartmentTable d = DepartmentDAO.newTable("d");

List<Row> rows = this.sql
    .select(e.star(), d.name)
    .from(e)
    .join(d, d.id.eq(e.departmentId)
    .execute();
```

The resulting query is:

```sql
SELECT e.*, d.name
FROM employee e
JOIN department d ON d.id = e.department_id
```

## Filtering * Wildcard Columns

If we only want a subset of the columns referenced by a `*` symbol we can use the `.filter(<predicate>)` method to exclude 
some of them. For example, to include only columns of type `INTEGER` or `DECIMAL` of the table `EMPLOYEE` we can do:

```java
EmployeeTable e = EmployeeDAO.newTable("e");
DepartmentTable d = DepartmentDAO.newTable("d");

List<Row> rows = this.sql
    .select(
      e.star()
        .filter(c -> "INTEGER".equals(c.getType()) || "DECIMAL".equals(c.getType())),
      d.name
    )
    .from(e)
    .join(d, d.id.eq(e.departmentId)
    .execute();
```

The resulting query is:

```sql
SELECT
  e.id, e.department_id, -- only 'id' and 'department_id' are of type INTEGER or DECIMAL in this table
  d.name
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

## Aliasing * Wildcard Columns

In some queries it can be useful to change the name of the column from one table to differentiate
from the columns of another one, specially when joined tables have columns with the same name.

If we use the function `.star()` to select all the columns of one table we can alias them by using 
the `.as()` method on them. For example:

```java
BatchTable b = BatchDAO.newTable("b");
EmployeeTable e = EmployeeDAO.newTable("e");

List<Row> rows = this.sql
    .select(
      e.star().as(c -> Alias.property("emp", c.getProperty())),
      b.star().as(c -> Alias.literal(("bc_" + c.getName()).toLowerCase()))
    )
    .from(e) //
    .join(b, b.itemName.eq(e.name)) //
    .where(e.name.like("A%"));
```

Produces the query:

```sql
SELECT
  e.id as "empId", e.name as "empName", e.branch_id as "empBranchId", 
  b.sku_code as "bc_sku_code", b.item_name as "bc_item_name"
FROM public.employee e
JOIN public.batch b ON b.item_name = e.name
WHERE e.name like 'A%'
```

The LiveSQL query above renamed the columns from both tables. It did apply a different
strategy to rename them, however:

- In the first one, it used the method `Alias.property()` to generate a property-like column name. It prepended `emp` to the property names:
`branchId` became `empBranchId`.
- In the second case, it used the method `Alias.literal()` to generate a column name as a verbatim string. In this case it prepender `bc_`
to the column name and converted it to lower case.

Note that more complex logic can be used. All properties mentioned in the table above are available to write more complex functions.


## Column Filtering and Aliasing

Finally, aliasing columns can be combined with filtering. For example, the select list can take the form:

```sql
    .select(
      e.star()
       .filter(c -> !"BLOB".equals(c.getType()))
       .as(c -> Alias.property("emp", c.getProperty())),
    )
```

In this case, the filter removes the BLOB columns. The remaining columns are aliased.


## Selecting without a FROM Clause

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
[the `DUAL` and `SYSDUMMY1` tables](./systables.md).


## Selecting Distinct Rows

By default a SELECT query produce rows as found in the database and this could include duplicate rows: i.e. it returns a *multiset*.
However, in some circumstances we may need the query to remove duplicate rows: i.e. to return a *set*. Removing duplicate
rows is an extra effort the engine needs to do, and we indicate this by prepending the `DISTINCT` clause to the select list,
as shown below:

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

## Selecting Distinct Rows with Specific Columns

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

Next: [The FROM and JOIN Clauses](./from-and-joins.md)

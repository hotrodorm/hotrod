# The SELECT Clause 

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

LiveSQL includes variations to specify all or a subset of the columns and also to qualify the query for DISTINCT rows only. See the variations below.


## Selecting All Columns

To select all columns of the table(s) start the query with `select()`. For example

```java
EmployeeTable e = EmployeeDAO.newTable("e");

List<Map<String, Object>> rows = this.sql
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

List<Map<String, Object>> rows = this.sql
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


## Selecting without a FROM Clause

Most of the database engines require a `FROM` clause in a `SELECT` statement. Some engines such as
PostgreSQL, MySQL, MariaDB, and SQL Server can execute a `SELECT` without a `FROM` clause if using literals in the select list, or
if the values can be computed directly without using any table. For example, the following query is valid in SQL Server:

```java
List<Map<String, Object>> rows = this.sql
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

List<Map<String, Object>> rows = this.sql
    .select(p.name, p.price.plus(p.tax).as("total_price"))
    .from(p) 
    .execute();
```

Produces a second column with a clear name:

```sql
SELECT p.name, p.price + p.qty as total_price
FROM product p
```


## Selecting Distinct Rows

By default a SELECT query produce rows as found in the database and this could include duplicate rows: i.e. it returns a *multiset*.
However, in some circumstances we may need the query to remove duplicate rows: i.e. to return a *set*. Removing duplicate
rows is an extra effort the engine needs to do, and we indicate this by prepending the `DISTINCT` clause to the select list,
as shown below:

```java
VehicleTable v = VehicleDAO.newTable("v");

List<Map<String, Object>> rows = this.sql
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

List<Map<String, Object>> rows = this.sql
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

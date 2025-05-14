# DynamicSQL

DynamicSQL can execute SQL queries that combine static and dynamic sections in them. The dynamic sections are automatically activated or deactivated according to the specified logic and according to the parameters that are provided at runtime.

The following example includes a dynamic query that updates a table:

```java
  Query q = dyn
    .literal("UPDATE employee SET salary = salary + 10")
    .if_("!empty dept").literal(" WHERE dept_no = ").parameter("dept").endif()
    .endModificationQuery();
```

In this example the assembled query is made up of an initial section that is optionally followed by a WHERE clause. The WHERE clause will only be included in the query if the parameter `dept` is added to the parameter list with any non-null value.

## 1. Reusable Queries

DynamicSQL queries can be defined once and reused many times with different parameters. A typical example could look like:

```java
  // This can be defined once

  DynamicSQL dyn = new DynamicSQL();
  DynamicModificationQuery q = dyn
    .literal("UPDATE employee SET salary = salary + 10")
    .if_("!empty dept").literal(" WHERE dept_no = ").parameter("dept").endif()
    .endModificationQuery();

  // This changes on every execution

  Parameters params = dyn.newParameters();
  params.add("dept", 5);
  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

Every execution can provide different parameters and the resulting query will include the WHERE section or not depending on the specific parameter values.

The query definition is thread-safe, so it can be used in a multi-threaded environment.

## 2. Previewing Queries and JDBC Parameters

Once the query is prepared -- as shown in the previous example -- the final query and its actual JDBC parameters can be viewed before being executed using `PreparedQuery.getPreview()` as in:

```java
  PreparedModificationQuery p = q.prepare(params);
  System.out.println(p.getPreview());
```

This shows:

```
  UPDATE employee SET salary = salary + 10 WHERE dept_no = ?
  
  JDBC Parameters (1):
    1. dept: 5 (java.lang.Integer)
```

## 3. Static Queries -- No Moving Parts

The JDBC specification defines queries of a static nature. That means, their structure is set when the SQL statement is defined. Thus, JDBC queries can be described as made up of two types of sections:

- SQL language parts
- Parameter parts

For example, the query:

```sql
  1    UPDATE employee SET salary = salary +
  2    ?
  3    WHERE hired_on <=
  4    ?
```

Has four sections: lines #1 and #3 are SQL language parts, while lines #2 and #4 are parameter parts.

DynamicSQL implements these two types of sections using `.literal(String)` and `.parameter(String)`. The query shown above can be written using Dynamic SQL as:

```java
  DynamicModificationQuery q = dyn
      .literal("UPDATE employee SET salary = salary + ")
      .parameter("salaryIncrease")
      .literal(" WHERE hired_on <= ")
      .parameter("lastDate")
      .endModificationQuery();
```

We can now define the parameters and run the query:

```java
  Parameters params = dyn.newParameters();
  params.add("salaryIncrease", 15);
  params.add("lastDate", LocalDate.of(2022, 12, 31));

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

So far so good. This is pretty much equivalent to any JDBC query you have seen before. The query does not have any *moving parts*. All sections are always included whenever we run the query, regardless of the values of the runtime parameters. These parameters are only applied to the query but **do not affect** the structure of it.

## 4. Making It Dynamic

Let's look at the initial example again:

```java
  DynamicModificationQuery q = dyn
    .literal("UPDATE employee SET salary = salary + 10")
    .if_("!empty dept").literal(" WHERE dept_no = ").parameter("dept").endif()
    .endModificationQuery();

  Parameters params = dyn.newParameters();
  params.add("dept", 5);

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

The line `params.add("dept", 5);` defines the parameter value as 5. When **preparing** the query the `.if_()` clause will evaluate to true and any sections inside will be included in the query. Thus, the effective query will be:

```sql
  UPDATE employee SET salary = salary + 10 WHERE dept_no = ?

  JDBC Parameters (1):
    1. dept: 5 (java.lang.Integer)
```

On the contrary, if the parameter `dept` was set to null -- or left unset -- the effective query would not include the *if* section and will look like:

```sql
  UPDATE employee SET salary = salary + 10

  JDBC Parameters (0):
    N/A
```

The extra section is gone, and even the parameter is gone.

Simple? Yes. The basic dynamic operator `.if_()` is one of the most used in DynamicSQL and can be quite powerful to add dynamic logic to queries.

But now, how's the test expression `!empty dept` in the *if* operator evaluated to true or false? That's [Apache JEXL](https://commons.apache.org/proper/commons-jexl/) in action. See the JEXL's documentation for the full syntax of the expressions.

The full list of dynamic operators implemented in DynamicSQL is:

| Operator | Description |
| -- | -- |
| `.if_()` | Conditionally include the inner sections (SQL and parameters) if the test condition evaluates to true |
| `.choose()` | Include only the first inner `.when()` section that evaluates to true and ignore the rest. If an `.otherwise()` section is included at the end, then include this one if all sections fail the test condition |
| `.foreach()` | Iterate over an collection or array of elements. The inner sections are included multiple times, once per each element |
| `.bind()` | Binds a variable in the parameter scope, so it can be used by other sections |
| `.trim()` | A trim section includes multiple `.if_()` sections; trim will collect all inner sections that evaluate to true and will join them with a defined separator |
| `.where()` | A trim section tailored to be used as a WHERE clause; each inner clause can potentially be include or excluded, and the where section joins them using and AND or OR operator |
| `.set()` | A trim section tailored to be used as the SET clause of an UPDATE statement; each inner clause can potentially be include or excluded, and the set section joins them using commas |

## 5. Applying Parameters Using Bean Syntax -- JEXL

The `.parameter()` section includes a String that is the name of the parameter. The parameters name is formally a JEXL expression that can use bean syntax to access data structures. This includes accessing:

- Properties
- Array elements
- List elements
- Maps keys and values
- Any Java method available in the object, using the JEXL syntax

For example, in the following query:

```java
  public class Department {
    public Department(int minSalary) { this.minSalary = minSalary; }
    public int minSalary;
  }
  
  public class Branch {
    public Branch(int id, Department[] dept) { this.id = id; this.dept = dept; }
    public int id;
    public Department[] dept;
  }
  
  DynamicModificationQuery q = dyn
      .literal("UPDATE employee SET salary = salary + ")
      .parameter("salaryIncrease")
      .literal(" WHERE salary < ").parameter("branch.dept[1].minSalary")
      .endModificationQuery();

  Parameters params = dyn.newParameters();
  params.add("salaryIncrease", 20);
  Department[] depts = {new Department(90), new Department(105), new Department(101)};
  Branch b = new Branch(1001, depts);
  params.add("branch", b);

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

Even though the parameter defined in the `Parameters` class is just `branch`, the parameter section accesses the value of:

```
  branch.dept[1].minSalary
```

This expression traverses the object, gets the second element of the array property, and then the property `minSalary` of this element.

## 6. Parameter Injection

JDBC imposes limits on which sections of a query can accept parameters. Typically they allow parameters in any place where a scalar value can be placed. They typically don't allow the query to parameterize the name of the table or other bedrock part of the query.

DynamicSQL allows the query to implement Parameter Injection. This is the ability to freely concatenate *SQL parts* in the query as parameters.

> **IMPORTANT NOTE ON SECURITY**<br/> SQL Injection opens the door for a security risk. There's a big difference between safely **applying** a parameter using `.parameter()` and directly **injecting** a String section into the query to be run using `.parameterInjection()`. Only inject fully controlled Strings that are coming from inside the application, and never from any external source, such as a web parameter, an API, or a configuration file.

The following example illustrates this for a case where the name of the table and the name of the ordering criteria is only know at runtime and not at compilation time:

```java
  DynamicSelectQuery q = dyn //
      .literal("SELECT * FROM ") //
      .parameterInjection("table") //
      .literal(" WHERE recorded < ")
      .parameter("maxDate") //
      .literal(" ORDER BY ") //
      .parameterInjection("ordering") //
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("table", "employee");
  params.add("maxDate", LocalDate.of(2021, 6, 14));
  params.add("ordering", "hired_on DESC");

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

When previewing the query, we can se that both *injected* parameters are concatenated to the query while it's being assembled. Only the *applied* parameter becomes an actual JDBC parameter:

```
  SELECT * FROM employee WHERE recorded < ? ORDER BY hired_on DESC
                ~~~~~~~~                             ~~~~~~~~~~~~~
                injected                             injected

  JDBC Parameters (1):
    1. maxDate: 2021-06-12 (java.time.LocalDate)
```

If used properly, parameter injection can be very useful. Use with caution.

## 7. Selecting Data

When SQL queries return data to the application, the returned data takes the form of a *result set*. The typical queries that return data are the SELECTs queries; however, there are other cases that do return data as well, such as queries using the RETURNING functionality and some INSERT queries with auto-generated keys.

### Degree

The result set can have zero, one, or many columns: this is known as the **degree** of the result set.

When running a SELECT query DynamicSQL can retrieve the result set rows in different ways suitable for different cases, such as:

- Single-column result sets
- Using tuples -- for two to six columns
- Returning a generic `Row` solution -- a Map-like solution
- Using fully customized logic to read the rows, by writing a row reader class

See the examples in this section for each case.

### Cardinality

On the other hand, the result set can have zero, one, or many rows: this is known as the **cardinality** of the result set.

When it comes to the cardinality of the result set there are three options:

- We can retrieve a `List<>` of elements. This is the default functionality and it will load the entire resultset in memory at once; this is typically useful for small result sets
- If we know the query will return zero or one row at the most then we can skip the `List<>` and use the form for one row only
- If we the query returns many rows we can choose to use a cursor to process the returned rows one by one and keep the memory usage low, instead of loading the entire result in memory at once

We'll see that we can use `.execute()`, `executeOne()`, or `executeCursor()` to decide on how we want DynamicSQL to produce the result set for us.

### Degree and Cardinality

Any option that DynamicSQL offers to work with the degree of the result set can be fully combined with any option available to work with the cardinality. You can combine them all as needed.

### 7.1 Selecting Data -- Single Column

If the result set has a single column with a typical scalar type such as Integer, String, LocalDate, etc. we can use the short-hand `q.prepare(params, <class>)` to read it, as shown below:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT first_name FROM employee WHERE active = 'Y'")
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  PreparedSelectQuery<String> p = q.prepare(params, String.class);
  List<String> names = p.execute(conn);
```

This solution works well for typical types but may not work for exotic types such as record-type columns or data that requires conversion beyond a normal cast. Use a Generic Row or a Custom RowReader in these cases.

### 7.2 Selecting Data -- Tuples of Two to Six Columns

If the query returns two to six columns we can use tuples to read the result set using `q.prepare(params, <class>, .<class>...)` as shown below:

```java
  DynamicSelectQuery q = dyn //
      .literal("SELECT first_name, hired_on, salary FROM employee WHERE active = 'Y'") //
      .endSelectQuery();

  Parameters params = dyn.newParameters();

  PreparedSelectQuery<Tuple3<String, LocalDate, Integer>> p =
    q.prepare(params, String.class, LocalDate.class, Integer.class);
  List<Tuple3<String, LocalDate, Integer>> employees = p.execute(conn);
```

As well as in the previous case, this strategy works well for typical data types but not for exotic types.

### 7.3 Selecting Data -- Generic Row

If the result set has many columns you can use a Generic Row type to retrieve the result. The `Row` type works in a similar way as a `java.util.Map`; values are stored in the map using the column nanes as keys.

The following example shows this case:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'")
      .endSelectQuery();

  Parameters params = dyn.newParameters();

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

**Note**: When using this strategy we are not providing the specific data types for the columns we are reading from the database. In this case the JDBC driver will decide the Java type to read the data. In most cases the resulting data types are useful to the application. However, if they are not, then a Custom RowReader needs to be used. See next example.

### 7.4 Selecting Data -- Custom RowReader

To have full control on how the result set data is read from the database you can implement a Custom RowReader. This gives full control to the developer to read and massage the data as needed right when it's being read from the database.

Though not necessary, this example considers defining a class to store the data retrieved by the query:

```java
  class MyEmployee {

    private String firstName;
    private LocalDate hired;
    private Double grossSalary;

    public MyEmployee(String firstName, LocalDate hired, Double grossSalary) {
      this.firstName = firstName;
      this.hired = hired;
      this.grossSalary = grossSalary;
    }

    public final String getFirstName() {
      return firstName;
    }

    public final LocalDate getHired() {
      return hired;
    }

    public final Double getGrossSalary() {
      return grossSalary;
    }

  }
```

Now the strategy that uses a custom RowReader can take the form:

```java
  DynamicSelectQuery q = dyn //
      .literal("SELECT first_name, hired_on, salary * 1.31 as gross_salary FROM employee "
        + "WHERE active = 'Y'") //
      .endSelectQuery();

  Parameters params = dyn.newParameters();

  RowReader<MyEmployee> rr = new RowReader<MyEmployee>() {
    @Override
    public MyEmployee readRowFrom(ResultSet rs, Connection conn) throws SQLException {
      String fn = rs.getString(1);
      LocalDate ho = rs.getObject(2, LocalDate.class);
      Double gs = rs.getDouble(3);
      if (rs.wasNull())
        gs = null;
      return new MyEmployee(fn, ho, gs);
    }
  };

  PreparedSelectQuery<MyEmployee> p = q.prepare(params, rr);
  List<MyEmployee> rows = p.execute(conn);
```

As you can see there's a tradeoff in this case. On one side we have full control on how to read the data (using specific Java/JDBC types, using any conversion strategy, or even combining data); on the other there's more code to write, to test, and to actually *have* for a long time.

### 7.5 Selecting Data -- Single Row

If we know the query returns zero or one row only, then we can use the simple form:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM employee WHERE id = ")
      .parameter("id")
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("id", 104);

  PreparedSelectQuery<Row> p = q.prepare(params);
  Row row = p.executeOne(conn);
```

The last line of the example uses `executeOne()` and returns a single element rather than a `List<>` of elements. This can be combined with any row reader strategy described before.

If the query returns no rows the result will be a null value.

**Note**: If, for any reason, the query returns more than a single row an Exception will be thrown.

### 7.6 Selecting Data -- Using a Cursor

If we want to read the result set one row at a time we can use `executeCursor()` to retrieve the result, as in:

```java
  DynamicSelectQuery q = dyn //
      .literal("SELECT * FROM employee WHERE last_name = ") //
      .parameter("last") //
      .literal(" ORDER BY hired_on DESC").endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("last", "Smith");

  PreparedSelectQuery<Row> p = q.prepare(params);
  try (Cursor<Row> rows = p.executeCursor(conn)) {
    for (Row r : rows) {
      // process 'r'
    }
  }
```

This strategy can enormously reduce the memory consumption of big queries. Only a few rows of the result set will be loaded in memory at any given time.

To optimize the memory usage and data retrieval performance you can also specify the `fetchSize` for the cursor by using the method variation `.executeCursor(conn, fetchSize)`.

This strategy can also be combined with any row reader strategy described before.

#### Open Connection and Open Cursor

The `Connection` object must remain open while reading the cursor. Keep this in mind if you plan to return the `Cursor<>` object to a caller; the caller must keep the database connection open while reading the cursor.

The same can be said about the `Cursor`. Since this object implements `AutoCloseable` it can be easily used in a try-with-resources statement as shown in the example above. However, if the closing is managed separately -- maybe because the Cursor is returned to the caller -- appropriate care will need to be taken to make sure it's always closed and the internal resources are freed.

#### PostgreSQL

For cursors to be effective in PostgreSQL the query must always be executed inside a database transaction. If a transaction had not been initiated, the JDBC driver will automatically and silently materialize the whole result set in memory, defeating the purpose of the cursor altogether.

## 8. Dynamic Operators

The dynamic functionality of queries is implemented using the following seven operators:

- If
- Choose
- For Each
- Bind
- Trim
- Where
- Set

These operators can nest static SQL section as well as more nested dynamic operators -- any number of them, as needed. The Dynamic SQL API syntax checks the appropriate nesting and only allows the developer to write valid nesting structures. See below:

### 8.1 Dynamic SQL -- If

### 8.1 Dynamic SQL -- Choose

### 8.2 Dynamic SQL -- ForEach

### 8.3 Dynamic SQL -- Bind

### 8.4 Dynamic SQL -- Trim

### 8.5 Dynamic SQL -- Where

### 8.6 Dynamic SQL -- Set

## 9. General Purpose Queries

update, delete, truncate, create, drop, set, unset, begin transaction, commit, etc.

## 10. Inserting Data



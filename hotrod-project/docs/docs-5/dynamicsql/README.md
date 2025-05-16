# DynamicSQL

DynamicSQL can execute SQL queries that combine static and dynamic sections in them. The dynamic sections are automatically activated or deactivated according to the specified logic and according to the parameters that are provided at runtime.

Even though it was developed in the scope of the HotRod ORM, DynamicSQL can be used by separatedly.


The following example includes a dynamic query that updates a table:

```java
  Query q = dyn
    .literal("UPDATE employee SET salary = salary + 10")
    .if_("!empty dept").literal(" WHERE dept_no = ").parameter("dept").endif()
    .endModificationQuery();
```

In this example the assembled query is made up of an initial section that is optionally followed by a WHERE clause. The WHERE clause will only be included in the query if the parameter `dept` is added to the parameter list with any non-null value.

## 1. Using DynamicSQL

DynamicSQL is a dependency included in the HotRod ORM that is automatically available for use if you are using HotRod.

On the other hand, if you want to use DynamicSQL separatedly you can add the following Maven dependency to your project:

```xml
  <dependency>
    <groupId>org.hotrodorm.hotrod</groupId>
    <artifactId>hotrod-dynamicsql</artifactId>
  </dependency>
```

## 2. DynamicSQL Queries Are Reusable

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

## 3. Previewing Queries and JDBC Parameters

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

## 4. Static Queries -- No Moving Parts

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

## 5. Making It Dynamic

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

## 6. Applying Parameters Using JEXL Syntax

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

## 7. Parameter Injection

JDBC imposes limits on which sections of a query can accept parameters. Typically they allow parameters in any place where a scalar value can be placed. They typically don't allow the query to parameterize the name of the table or other bedrock part of the query.

DynamicSQL allows the query to implement Parameter Injection. This is the ability to freely concatenate *SQL parts* in the query as parameters.

> [!CAUTION]
> **IMPORTANT NOTE ON SECURITY**: SQL Injection opens the door for a security risk. There's a big difference between safely **applying** a parameter using `.parameter()` and directly **injecting** a String section into the query to be run using `.parameterInjection()`. Only inject fully controlled Strings that are coming from inside the application, and never from any external source, such as a web parameter, an API, or a configuration file.

The following example illustrates this for a case where the name of the table and the name of the ordering criteria is only know at runtime and not at compilation time:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM ")
      .parameterInjection("table")
      .literal(" WHERE recorded < ")
      .parameter("maxDate")
      .literal(" ORDER BY ")
      .parameterInjection("ordering")
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("table", "employee");
  params.add("maxDate", LocalDate.of(2021, 6, 14));
  params.add("ordering", "hired_on DESC");

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

When previewing the query, we can se that both *injected* parameters are concatenated to the query while it's being prepared. Only the *applied* parameter becomes an actual JDBC parameter:

```
  SELECT * FROM employee WHERE recorded < ? ORDER BY hired_on DESC
                ~~~~~~~~                             ~~~~~~~~~~~~~
                injected                             injected

  JDBC Parameters (1):
    1. maxDate: 2021-06-12 (java.time.LocalDate)
```

If used properly parameter injection can be very useful. Use with caution.

## 8. Selecting Data

When SQL queries return data to the application, the returned data takes the form of a *result set*. The typical queries that return data are the SELECTs queries; however, there are other cases that do return data as well, such as queries using the RETURNING functionality and some INSERT queries with auto-generated keys.

### The Query Degree

The result set can have zero, one, or many columns: this is known as the **degree** of the result set.

When running a SELECT query DynamicSQL can retrieve the result set rows in different ways suitable for different cases, such as:

- Single-column result sets
- Using tuples -- for two to six columns
- Returning a generic `Row` solution -- a Map-like solution
- Using fully customized logic to read the rows, by writing a row reader class

See the examples in this section for each case.

### The Query Cardinality

On the other hand, the result set can have zero, one, or many rows: this is known as the **cardinality** of the result set.

When it comes to the cardinality of the result set there are three options:

- We can retrieve a `List<>` of elements. This is the default functionality and it will load the entire resultset in memory at once; this is typically useful for small result sets
- If we know the query will return zero or one row at the most then we can skip the `List<>` and use the form for one row only
- If we the query returns many rows we can choose to use a cursor to process the returned rows one by one and keep the memory usage low, instead of loading the entire result in memory at once

We'll see that we can use `.execute()`, `executeOne()`, or `executeCursor()` to decide on how we want DynamicSQL to produce the result set for us.

### Degree and Cardinality

Any option that DynamicSQL offers to work with the degree of the result set can be fully combined with any option available to work with the cardinality. You can combine them all as needed.

### 8.1 Selecting Data -- Single Column

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

### 8.2 Selecting Data -- Tuples of Two to Six Columns

If the query returns two to six columns we can use tuples to read the result set using `q.prepare(params, <class>, .<class>...)` as shown below:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT first_name, hired_on, salary FROM employee WHERE active = 'Y'")
      .endSelectQuery();

  Parameters params = dyn.newParameters();

  PreparedSelectQuery<Tuple3<String, LocalDate, Integer>> p =
    q.prepare(params, String.class, LocalDate.class, Integer.class);
  List<Tuple3<String, LocalDate, Integer>> employees = p.execute(conn);

  for (Tuple3<String, LocalDate, Integer> r : employees) {
    System.out.println("Name: " + r.getA() + " -- Hired: " + r.getB() + " -- Salary: " + r.getC());
  }
```

As well as in the previous case, this strategy works well for typical data types but not for exotic types.

### 8.3 Selecting Data -- Generic Row

If the result set has many columns you can use a Generic Row type to retrieve the result. The `Row` type works in a similar way as a `java.util.Map`; values are stored in the map using the column nanes as keys.

The following example shows this case:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT id, last_name, salary * 1.31 as \"gross\" FROM employee WHERE active = 'Y'")
      .endSelectQuery();

  Parameters params = dyn.newParameters();

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
  for (Row r : rows) {
    System.out.println("id=" + r.get("ID") + ", last name=" + r.get("LAST_NAME")
      + ", gross=" + r.get("gross"));
  }
```

Notice we retrieve values from the Row object using upper case column names. This depends on the specific letter case that each database defaults to.

Also notice that in the case of the last column we explicitly defined the alias `gross`. This alias is enclosed in double quotes, that in this database means its name should be used as is, and therefore is not affected by the letter case.

**Note**: When using this strategy we are not providing the specific data types for the columns we are reading from the database. In this case the JDBC driver will decide the Java type to read the data. In most cases the resulting data types are useful to the application. However, if they are not, then a Custom RowReader needs to be used. See next example.

### 8.4 Selecting Data -- Custom RowReader

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
  DynamicSelectQuery q = dyn
      .literal("SELECT first_name, hired_on, salary * 1.31 as gross_salary FROM employee "
        + "WHERE active = 'Y'")
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

### 8.5 Selecting Data -- Single Row

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

### 8.6 Selecting Data -- Using a Cursor

If we want to read the result set one row at a time we can use `executeCursor()` to retrieve the result, as in:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM employee WHERE last_name = ")
      .parameter("last")
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

## 9. Dynamic Operators

The dynamic functionality of queries is implemented using the seven operators described in this section. They can include and exclude sections of the query based on the values of the runtime parameters.

The dynamic operators can be nested. That is, they can include static SQL sections and dynamic sections -- any number of them, as needed. The Dynamic SQL API syntax checks the appropriate nesting and only allows the developer to write valid nesting structures.

### 9.1 DynamicSQL IF

The *if* operator is the simplest operator. It includes a `test` predicate. The nested content -- static SQL sections, parameters, and other DynamicSQL operators are only included if the test predicate evaluates to true at runtime.

The following example illustrates how an if operator works. It includes nesting if operators:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM employee WHERE active = 'Y'")
      .if_("f != null")
        .if_("f.firstName != null").literal(" AND first_name = ").parameter("f.firstName").endif()
        .if_("f.lastName != null").literal(" AND last_name = ").parameter("f.lastName").endif()
        .if_("f.firstSSN != null").literal(" AND last_ssn = ").parameter("f.lastSSN").endif()
      .endif()
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  Map<String, Object> filter = new HashMap<>();
  filter.put("firstName", null);
  filter.put("lastName", "Andersson");
  filter.put("lastSSN", null);
  params.add("f", filter);

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

The inner if operators will only be evaluated if the parent if evaluates to true; otherwise, they'll be fully ignored and excluded.

An if operator can nest static and dynamic sections in their body, including if operators or any other operator.


### 9.2 DynamicSQL CHOOSE

The choose operator picks the first nested "when" clause that evaluates to true and discard the rest. If none is select and an "otherwise" clause was included, then this one will be selected.

The choose operator includes multiple `.when()` operators. These take the similar form if an `.if_()` operator; that is, they have a predicate and have nested sections.

The following example includes a choose operator that implements four types of ordering for the query:

```java
  DynamicSelectQuery q = dyn //
      .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'")
      .choose()
        .when("ordering == 1").literal(" ORDER BY first_name").endwhen()
        .when("ordering == 2").literal(" ORDER BY last_name").endwhen()
        .when("ordering == 3").literal(" ORDER BY hired_on DESC").endwhen()
        .otherwise().literal(" ORDER BY salary").endotherwise()
      .endchoose()
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("ordering", 2);

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

A choose operator can nest static and dynamic sections in their body, including more choose operators or any other operator.

### 9.3 DynamicSQL FOREACH

The foreach operator iterates over a collection or array of items. In every iteration the current item is available for use as in the variable scope. All nested sections are included once per item.

The following example makes it possible to use a list of values in a SQL IN predicate:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM employee WHERE id in ")
      .foreach("v", "searchedIds", "(", ", ", ")")
        .parameter("v")
      .endforeach()
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  List<Integer> searchedIds = Arrays.asList(101, 104, 105, 144);
  params.add("searchedIds", searchedIds);

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

The foreach operator can also include delimiters to include at the beginning, as separator, and at the end in the form `.foreach(item, collection, open, separator, close)`. These parameters are:

- `item`: the name of a variable that will hold the current item
- `collection`: the JEXL expression that will produce an array or collection of items
- `open`: the opening delimiter
- `separator`: the separator to be included by for each between each interation
- `close`: the closing delimiter

In this example the body of the foreach operator includes a single section: `.parameter("v")`.

Anyway, as well as any operator the body of this operator can include any DynamicSQL sections, including static sections, parameters, or any other operators in many nesting levels, as needed.

### 9.4 DynamicSQL BIND

The bind operator binds a variable in the parameter scope, so it can be used by other DynamicSQL operators in the rest of the query.

```java
  DynamicSelectQuery q = dyn
      .bind("pattern", "'%' + part + '%'")
      .literal("SELECT * FROM employee WHERE last_name like ")
      .variable("pattern")
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("part", "mit");

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

In this example `pattern` is not a parameter provided by in the parameter context, but it's a variable defined inside the query. This variable is later *applied* in the query. Variables, as well as parameters, are not injected but applied.

The bind operator does not have a body and, therefore, cannot nest other sections.

### 9.5 DynamicSQL TRIM

The trim operator includes multiple if operators. Each if operator is evaluated for inclusion and the included ones make it to the query, separated by the separator defined in the trim operator.

The example below decides to include or exclude columns in the select list at runtime; the query has a variable degree.

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT ")
      .trim("", ", ", "")
        .if_("getFirstName").literal("first_name").endif()
        .if_("getLastName").literal("last_name").endif()
        .if_("getHiredDate").literal("hired_on").endif()
      .endtrim()
      .literal(" FROM employee")
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  params.add("getFirstName", true);
  params.add("getLastName", false);
  params.add("getHiredDate", true);

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

In this example, the columns are included according to the provided boolean parameters, and stitched together using commas (`,`) as defined in this `.trim()` operator.

Trim uses the following parameters for formatting purposes:

- `header`: The opening delimiter
- `separator`: The separator to be included between selected sections
- `tail`: The closing delimiter

### 9.6 DynamicSQL WHERE

A where operator is a tailored trim operator that simplifies the writing of a dynamic WHERE clause.

For example:

```java
  DynamicSelectQuery q = dyn
      .literal("SELECT * FROM employee")
      .where("OR")
        .if_("f.first != null").literal("first_name = ").parameter("f.first").endif()
        .if_("f.last != null").literal("last_name = ").parameter("f.last").endif()
        .if_("f.hiredDate != null").literal("hired_on = ").parameter("f.hiredDate").endif()
      .endwhere()
      .endSelectQuery();

  Parameters params = dyn.newParameters();
  Filter filter = new Filter();
  filter.first = null;
  filter.last = "Smith";
  filter.hiredDate = LocalDate.of(2023, 12, 22);
  params.add("f", filter);

  PreparedSelectQuery<Row> p = q.prepare(params);
  List<Row> rows = p.execute(conn);
```

In this case the where operator will assemble any of those three if operators prepending `WHERE` and adding `OR` between them. If none of them is selected nothing will be added to the query, not even the `WHERE` section.

### 9.7 DynamicSQL SET

A set operator is a tailored trim operator that simplifies the writing of a dynamic SET clause.

For example:

```java
  DynamicModificationQuery q = dyn
      .literal("UPDATE employee")
      .set()
        .if_("nv.first != null").literal("first_name = ").parameter("nv.first").endif()
        .if_("nv.last != null").literal("last_name = ").parameter("nv.last").endif()
        .if_("nv.hiredDate != null").literal("hired_on = ").parameter("nv.hiredDate").endif()
      .endset()
      .literal("WHERE id = ").parameter("id")
      .endModificationQuery();

  Parameters params = dyn.newParameters();
  params.add("id", 103);
  NewValues newValues = new NewValues();
  newValues.first = "Leila";
  newValues.last = null;
  newValues.hiredDate = LocalDate.of(2023, 12, 25);
  params.add("nv", newValues);

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

In this case the set operator will assemble any of those three if operators prepending `SET` and adding `,` between them. If none of them is selected nothing will be added to the query, not even the `SET` section.

## 10. UPDATE and DELETE Queries

The UPDATE and DELETE queries are assembled like any other query and can be as static or dynamic as needed. As shown in the example above they are created using the `.endModificationQuery()` method.

When executed they return an int value that represent the number of affected rows.

## 11. General Purpose Queries

General purpose queries include any query that can be run in the database. They can be as static or dynamic as needed.

Typically the following queries fall into this category:

- UPDATE
- DELETE
- TRUNCATE
- CREATE
- DROP
- SET
- BEGIN TRANSACTION
- COMMIT
- ROLLBACK
- any other SQL query that doesn't return a result set

Again, when executed these queries return an int value that represent the number of affected rows.

## 12. Inserting Data

DynamicSQL can execute typical INSERT queries in the trivial way.

However, if we want the query to recover the generated keys we'll need to tell DynamicSQL how to do so. Each database provides different strategies to do so.

### Limitations

The currently implemented DynamicSQL INSERT functionality has the following limitations:

- Can only retrieve a key for a single inserted row. If the query inserts multiple rows and -- therefore -- produces multiple generated keys they cannot be retrieved by DynamicSQL.
- Can only retrieve keys of numeric type such as INT and BIGINT. It cannot retrieve UUID, TIMESTAMP, CHAR, or VARCHAR columns.
- Can only retrieve a single key per row. This is typically enough since there's only one primary key in each table. However, some databases (e.g. PostgreSQL) can have multiple identities in a table.
- Cannot retrieve other generated values, such as for omitted columns with DEFAULT constraints.
- Has only be tested with Oracle, DB2 LUW, PostgreSQL, SQL Server, MySQL, MariaDB, Sybase ASE, H2, HyperSQL, and the Derby databases. It most likely can work with most databases out there, but it hasn't been formally tested with them.

### 12.1 Inserting a Row Without Recovering Keys

There are two sub cases for this case:

#### a) A Table Without a Primary Key

The following example considers a table that doesn't have a primary key:

```sql
  create table app_log (
    message varchar(100),
    recorded_at timestamp
  );
```

To insert a row in it we can do:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO app_log (message, recorded_at) VALUES (")
      .parameter("msg")
      .literal(", ")
      .parameter("ts")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.NO_RETRIEVAL);

  Parameters params = dyn.newParameters();
  params.add("msg", "This is normal log.");
  params.add("ts", LocalDateTime.now());

  PreparedInsertQuery p = q.prepare(params);
  p.execute(conn);
```

This query will insert the row and return. There's no primary key, so there's nothing to return.

#### b) A Table with a Primary Key and No Auto-Generation Features

The following example considers an old-school table that has a primary key that has not auto-generated logic:

```sql
  create table region (
    code char(4) primary key not null,
    name varchar(100)
  );
```

To insert a row in it we can do:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO region (code, name) VALUES (")
      .parameter("cd")
      .literal(", ")
      .parameter("title")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.NO_RETRIEVAL);

  Parameters params = dyn.newParameters();
  params.add("cd", "NE03");
  params.add("title", "North East Sub");

  PreparedInsertQuery p = q.prepare(params);
  p.execute(conn);
```

Since there's no autogeneration feature defined in this table the application needs to provide the value for the primary key.

### 12.2 Inserting a Row Using Recovering Identity Keys

Nowadays the most commoon auto-generation logic in use are IDENTITY keys. This feature indicates that a primary key -- and also for non primary keys as well in some databases -- can be auto-generated by the database engine itself when a row is inserted.

There are two variations of auto-generated identity columns:

- ALWAYS: In this case the value for the column must always be excluded in the INSERT column-list. It will always be generated by the database.
- BY DEFAULT: In this case the value for the column is optional in the INSERT column-list. If provided, then the database will use this value during insertion; if not, the database will auto-generate a value for it.

The following example illustrates an identity column called `id`. The DynamicSQL query does not provide the value for it and expects the database to generate it automatically:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO employee (first_name, last_name) VALUES (")
      .parameter("firstName")
      .literal(", ")
      .parameter("lastName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET);

  Parameters params = dyn.newParameters();
  params.add("firstName", "Johnny");
  params.add("lastName", "Doe");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

The execute method of DynamicSQL executes the INSERT as expected and also retrieves the primary key value using the strategy IDENTITY_INLINE_KEYS_RESULTSET. The newly generated primary key value is returned as a Long value.

This solution will work well in the following databases:

- DB2 LUW
- PostgreSQL
- SQL Server
- MySQL
- MariaDB
- Sybase ASE
- H2
- HyperSQL
- Derby

**Note**: SQL Server only implements the ALWAYS variation of identities. The BY DEFAULT variation is not supported and the query will crash if a primary key value is provided.

**Note**: Sybase ASE implements the ALWAYS variation and the query will crash if a primary key value is provided. However, the statement `SET IDENTITY_INSERT <table> ON` can be executed right before the INSERT to enable explicit primary key values.

#### Oracle's Variation

There's a variation to this query that is used by the Oracle database. Oracle explicitly requires the name of the primary key column that is to be retrieved. In this case, if the primary key column is `id` the last line of the query definition should take the form:

```java
      .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET, null, null, "id");
```

### 12.3 Inserting a Row Recovering Sequences Values

In some cases the generation of keys is done with sequences. In this case different databases offer different strategies to recover these newly generated keys.

The following table uses a sequence to generate the primary keys:

```sql
  create table office (
    id int primary key not null,
    name varchar(100)
  );

  create sequence seq_office;
```

#### a) Oracle's Solution

Oracle can *inline* sequences, as shown below:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) VALUES (seq_office.NEXTVAL, ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null, "id");

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

#### b) DB2 LUW's Solution

DB2 LUW can also inline sequences, as shown below, although the inline sequence syntax differs:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) VALUES (NEXT VALUE FOR seq_office, ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null, "id");

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

#### c) PostgreSQL's Solution

PostgreSQL does not require the query to declare the primary key column so it's a bit simpler:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) VALUES (NEXTVAL('seq_office'), ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

#### d) SQL Server's Solution

SQL Server does not return the keys in the *key result set*, but on the standard result set dedicated for query rows:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) OUTPUT INSERTED.id VALUES (NEXT VALUE FOR seq_office, ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_STANDARD_RESULTSET);

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

#### e) MySQL and MariaDB

MySQL and MariaDB do not implement sequences.

#### f) H2 and HyperSQL's Solution

The H2 and HyperSQL databases implement the same strategy:

```java
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) VALUES (NEXT VALUE FOR seq_office, ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_STANDARD_RESULTSET);

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

#### g) Derby's Solution

The Derby database needs to perform a prefetch of the sequence value and then apply it behind the scenes to the INSERT query:

```java
  private static final InsertProperties INSERT_PROPERTIES = new InsertProperties(
      "VALUES NEXT VALUE FOR seq_account", "id");
  DynamicInsertQuery q = dyn
      .literal("INSERT INTO office (id, name) VALUES (")
      .parameter("id")
      .literal(", ")
      .parameter("officeName")
      .literal(")")
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, INSERT_PROPERTIES);

  Parameters params = dyn.newParameters();
  params.add("officeName", "Downtown Office");

  PreparedInsertQuery p = q.prepare(params);
  Long pk = p.execute(conn);
```

































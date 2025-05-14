# DynamicSQL

This is the DynamicSQL Module of the [HotRod ORM](../README.md).

DynamicSQL allows the developer to write SQL queries assembled from multiple sections that can be activated or deactivated according to the parameters that are provided at runtime.

## Example

The following example includes a non-dynamic query that updates a table:

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

The assembled query is made up of an initial section -- optionally followed by a WHERE clause. The WHERE clause will only be included in the query is the parameter `dept` is added to the parameter list (with any non-null value).

**Note**: The defined query is thread-safe so it can be defined once and reused many times with different parameters.


## 1. Previewing Queries and JDBC Parameters

Once the query is prepared -- as shown in the previous example -- the final query and its actual JDBC parameters can be viewed before being executed using `PreparedQuery.getPreview()` as in:

```java
  PreparedModificationQuery p = q.prepare(params);
  System.out.println(p.getPreview());
```

This shows:

```
  UPDATE employee SET salary = salary + 10 WHERE dept_no = ?
  
  JDBC Parameters (1):
    1. dept: 55 (java.lang.Integer)
```

## 2. Static Queries

Traditional JDBC queries are of a static nature. That means, their structure is set when the SQL statement is defined. JDBC queries can be described as made up of two types of sections:

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

The, we can define the parameter values, and run the query, as in:

```java
  Parameters params = dyn.newParameters();
  params.add("salaryIncrease", 15);
  params.add("lastDate", LocalDate.of(2022, 12, 31));

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

So far so good. This is pretty much equivalent to any JDBC query you have seen before. The query does not have any *moving parts*. All sections are always included whenever we run the query, regardless of the values of the runtime parameters. These parameters are only applied to the query but **do not affect** the structure of it.

## 3. Making it Dynamic

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

The line `params.add("dept", 5);` defines the parameter value as 5. In this case the `.if_()` clause will evaluate to true and the section inside will be included in the query. Thus, the effective query will be:

```sql
  UPDATE employee SET salary = salary + 10
  WHERE dept_no = ?

  JDBC Parameters (1):
    1. dept: 5 (java.lang.Integer)
```

On the contrary, if the parameter `dept` set to null (or left unset), the effective query would not include the *if* section and will look like:

```sql
  UPDATE employee SET salary = salary + 10

  JDBC Parameters (0):
    N/A
```

Simple? Yes. The basic dynamic operator `.if_()` is one of the most used in DynamicSQL. The full list of dynamic operators is:

| Operator | Description |
| -- | -- |
| `.if_()` | Conditionally include the inner sections (SQL and parameters) if the test condition evaluates to true |
| `.choose()` | Include only the first section that evaluates to true while excluding the rest. If an `.otherwise()` section is included at the end, then include this one if all sections fail the test condition |
| `.foreach()` | Iterate over an array or collection of elements. For every element found the inner sections are included once |
| `.bind()` | Binds a variable in the parameter scope, so it can be used by other sections |
| `.trim()` | A trim section includes multiple `.if_()` sections; trim will collect all inner sections that evaluate to true and use a defined separator to join them |
| `.where` | A trim section tailored to be used as a WHERE clause; each inner clause can potentially be include or excluded, and the where section joins them using and AND or OR operator |
| `.set` | A trim section tailored to be used as the SET clause of an UPDATE statement; each inner clause can potentially be include or excluded, and the set section joins them using commas |

## 4. Applying Parameters Using Bean Syntax -- JEXL

The `.parameter()` section includes a String that is the name of the parameter. That's correct, but the string value is actually an expression that can use bean syntax to access more complex data structures. This includes accessing:

- Properties
- Array elements
- List elements
- Maps
- Any Java method available in the object, using the available parameters as needed

For example, in the following query:

```java
  public class Department {
    public int minSalary;
  }

  public class Branch {
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
  Branch b = new Branch();
  b.id = 1001;
  b.dept = new Department[3];
  b.dept[1] = new Department();
  b.dept[1].minSalary = 105;
  params.add("branch", b);

  PreparedModificationQuery p = q.prepare(params);
  int count = p.execute(conn);
```

Even though the parameter defined in the `Parameters` class is `branch`, the parameter section accesses the value of `branch.dept[1].minSalary`: that is, the array property `dept`, the second element of it, and a property `minSalary` of this element.

## 5. Parameter Injection

JDBC imposes limits on which sections of a query can accept parameters. Typically they allow parameters in any place where a scalar value can be placed. They typically don't allow the query to parameterize the name of the table or other bedrock part of the query.

DynamicSQL allows the query to implement Parameter Injection. This is the ability to freely concatenate *SQL parts* in the query as parameters.

**SECURITY NOTE**: SQL Injection opens the door for a security risk. There's a big difference between safely **applying** a parameter using `.parameter()` and directly **injecting** a String section into the query to be run using `.parameterInjection()`. Only inject fully controlled Strings that are coming from inside the application, and never from any external source, such as a web parameter, an API, or a configuration file.

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

When previewing the query, we can se that both injected parameters are added to the query while it's being assembled. Only the applied parameter becomes a JDBC parameter:

```
  SELECT * FROM employee WHERE recorded < ? ORDER BY hired_on DESC
                ~~~~~~~~                             ~~~~~~~~~~~~~

  JDBC Parameters (1):
    1. maxDate: 2021-06-12 (java.time.LocalDate)
```

If used properly, parameter injection can be very useful. Use with caution.

## 6. Selecting Data -- Single column

## 7. Selecting Data -- Tuples of two to six columns

## 8. Selecting Data -- Generic Row

## 9. Selecting Data -- Custom RowReader

## 10. Selecting Data -- Single Row

## 11. Selecting Data -- Using a Cursor

## 12. Dynamic SQL -- Choose

## 13. Dynamic SQL -- ForEach

## 14. Dynamic SQL -- Bind

## 15. Dynamic SQL -- Trim

## 16. Dynamic SQL -- Where

## 17. Dynamic SQL -- Set

## 18. General Queries

update, delete, truncate, create, drop, set, unset, begin transaction, commit, etc.

## 19. Inserting a Row



# The LIMIT Clause

The `LIMIT` clause limits how many rows a `SELECT` statement returns.
In the presence of an `OFFSET` clause this number of rows starts counting after
all rows have been discarded by this `OFFSET` clause.

It has a single form where it accepts an integer number as a parameter.


## About LIMIT and ORDER BY

Most of the time the `LIMIT` clause should go along with an `ORDER BY` clause. Since table rows in relational
databases do not have inherent ordering an `ORDER BY` clause ensures the rows will be processed
in the same order every time and, thus, the effect of the limit will be consistent.

Otherwise, subsequent executions of the same query can return the same or different rows somewhat
*at random* and the result of the query may not make too much sense.

Without an `ORDER BY` clause the `LIMIT` clause can still make sense in a reduced number of cases
when the query needs to return a few rows &mdash; *any rows* &mdash; from a query or a table.


## Example

The following query returns the top 10 employees with the highest salary:

```java
EmployeeTable e = EmployeeDAO.newTable("e");

List<Map<String, Object>> rows = this.sql
    .select()
    .from(e) 
    .orderBy(e.salary.desc())
    .limit(10)
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM employee e
ORDER BY salary DESC
LIMIT 10
```

The limit value is a numeric value that can be computed by the client application and does not 
need to be a constant value.



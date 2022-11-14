# The OFFSET Clause

The `OFFSET` clause defines how many rows of the result set of a `SELECT` statement need to be 
skipped before sending it to the client application.

It has a single form where it accepts an integer number of rows.


## OFFSET and ORDER BY

The `OFFSET` clause should go along with an `ORDER BY` clause. Since table rows in relational
databases do not have inherent ordering an `ORDER BY` clause ensures the rows will be processed
in the same order every time and, thus, the offset will make sense.

Otherwise, subsequent executions of the same query with the same or different offset values
can return the same or different rows *at random*. Nevertheless, LiveSQL allows the use of 
`OFFSET` without an accompanying `ORDER BY` clause.


## Example

The following query returns all branches sorted by name, except the first 100 ones, from north
region:

```java
BranchTable b = BranchDAO.newTable("b");

List<Map<String, Object>> rows = this.sql
    .select()
    .from(b) 
    .where(b.region.eq("NORTH"))
    .orderBy(b.name)
    .offset(100)
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM branch b
WHERE region = 'NORTH'
ORDER BY name
OFFSET 100
```

The offset value is a numeric value that can be computed in the client application and does not 
need to be a constant value.

# The WHERE Clause

The `WHERE` clause filters out rows from the result set of a `SELECT` statement.

It has a single form where it accepts a boolean expression (aka *predicate*) as a parameter. This boolean
expression can be as simple or as complex as needed and it must evaluate to a boolean value.


## Example

The following query selects all widgets that are active.


```java
WidgetTable w = WidgetDAO.newTable("w");

List<Map<String, Object>> rows = this.sql
    .select()
    .from(w) 
    .where(w.status.eq("ACT"))
    .execute();
```

The resulting query is:

```sql
SELECT *
FROM widget w
WHERE status = 'ACT'
```

In this example the predicate in the `WHERE` clause is very simple.

A predicate can be more complex to accomodate arithmetic, boolean logic, parenthesis, subqueries, etc. 
See [Boolean Expressions](./boolean-expressions.md) for more details and examples.


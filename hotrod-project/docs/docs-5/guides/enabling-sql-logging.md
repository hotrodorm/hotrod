# Query Logging

Sometimes is critical to have the ability to see the exact queries and parameters that are used when running queries.

The DAOs in the persistence layer provide logging at the DEBUG and TRACE levels.


### 1. Enabling Logging in DAOs

The logging can be enabled for an entire group of DAOs or for a single DAO. In the `application.properties` file add one or more lines in the form:

```properties
logging.level.<package>.<dao>=<level>
```

If we have a couple of DAOs called `ReportingDAO` and `EmployeeDAO` in the package `app.persistence.dao` then, the following configuration:

```properties
logging.level.app.persistence.dao=DEBUG
```

Enables debugging for all DAOs in that package.

The configuration:

```properties
logging.level.app.persistence.dao.ReportingDAO=DEBUG
```

Enables debugging for the specific DAO `ReportingDAO`.

### 2. The Logging Level

The DAOs provide two levels of logging:

- A `DEBUG` level logs the SQL query being executed.
- A `TRACE` level logs the SQL query being executed and the specific parameters being applied to it.

For example, a query with DEBUG logging level produces:

```logging
2025-05-27 11:07:48.718 DEBUG 10803 --- [           main] app.persistence.dao.EmployeeDAO          : SQL:
SELECT
  id,
  first_name,
  last_name,
  branch_id
FROM employee
WHERE id = ?
```

The same query with a TRACE logging level shows the list of parameter values:

```logging
2025-05-27 11:07:48.718 TRACE 10803 --- [           main] app.persistence.dao.EmployeeDAO          : SQL:
SELECT
  id,
  first_name,
  last_name,
  branch_id
FROM employee
WHERE id = ?

JDBC Parameters (1):
  1. filter.id: 134081 (java.lang.Integer)
```


### 3. Enable LiveSQL Logging

LiveSQL is logged separately using the logger `org.hotrod.livesql.LiveSQL`, as in:

```properties
logging.level.org.hotrod.livesql.LiveSQL=DEBUG
```

Both levels DEBUG and TRACE are available.

LiveSQL's logging includes the query, the parameters, and the column types, as in:

```
2025-05-27 11:26:59.503 TRACE 13226 --- [           main] org.hotrod.livesql.LiveSQL               : SQL: --- SQL ----------
SELECT
  e.id as "id",
  e.first_name as "firstName",
  e.last_name as "lastName",
  e.branch_id as "branchId",
  b.name as "branchName"
FROM employee e
JOIN branch b ON b.id = e.branch_id
WHERE lower(e.last_name) like ? and b.type in (?, ?, ?)
ORDER BY b.name, e.last_name DESC
--- Parameters ---
 * 1 (java.lang.String, length=7): %smith%
 * 2 (java.lang.Integer): 2
 * 3 (java.lang.Integer): 6
 * 4 (java.lang.Integer): 7
--- Query Columns ---
 * id: class java.lang.Integer, source: ENTITY_COLUMN
 * firstName: class java.lang.String, source: ENTITY_COLUMN
 * lastName: class java.lang.String, source: ENTITY_COLUMN
 * branchId: class java.lang.Integer, source: ENTITY_COLUMN
 * branchName: class java.lang.String, source: ENTITY_COLUMN
------------------
```



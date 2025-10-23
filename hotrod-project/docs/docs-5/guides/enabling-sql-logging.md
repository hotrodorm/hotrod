# Enabling Query Logging

Sometimes is critical to have the ability to see the exact queries and parameters that are used when running queries.

The persistence layer used by HotRod provides logging at the DEBUG and TRACE levels.

## Logging Levels

The logging can log the SQL queries only, or the SQL queries and their actual parameters:

- The *Basic Mode* logs the SQL query only. This is activated when the logger's level is set to DEBUG.
- The *Full Mode* that shows the SQL query and its parameter values. This is activated when the logger's level is set to TRACE.

In short, TRACE logs the SQL and its parameters, DEBUG logs the SQL only, and INFO and up doesn't log SQL queries at all.

## Separate Loggers

Logging can be activated separately per DAO and in LiveSQL queries.

In the DAOs the logger is referenced by the full class name of the DAO that we want to log. For example, if we want to log the queries in the DAO `app.persistence.dao.EmployeeDAO` we should enable the logger by using the property `logging.level.app.persistence.dao.EmployeeDAO` in the Spring properties file.

LiveSQL can receive a logging adapter in the `execute()` methods to do logging. This logging adapter should implement both levels of logging. See the example below.


## Examples

The [Hello Logging](./hello-logging.md) example is a running example that shows logging in CRUD, Nitro, and LiveSQL.


### 1. Logging in DAOs and Nitro SELECTs

The logging can be enabled for an entire group of DAOs or for a single DAO. In the `application.properties` file add one or more lines in the form:

```properties
logging.level.<package>.<dao>=<level>
```

If we have a couple of DAOs called `ReportingDAO` and `EmployeeDAO` in the package `app.persistence.dao` then, the following configuration:

```properties
logging.level.app.persistence.dao=DEBUG
```

Enables debugging for all DAOs in that package. Now, the configuration:

```properties
logging.level.app.persistence.dao.ReportingDAO=DEBUG
```

Enables debugging for the specific DAO `ReportingDAO`.


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


### 2. LiveSQL Logging

LiveSQL can specify a logger adapter in the `execute()` methods. This logger adapter references your specific logger solution, and can be defined at the class level as:

```java
  private static final LiveSQLLogging LIVESQL_LOG = LiveSQLLogging.of(
    () -> LOG.isLoggable(Level.FINE), msg -> LOG.fine(msg),
    () -> LOG.isLoggable(Level.FINER), msg -> LOG.finer(msg)
  );
```

In the end LiveSQL will use `LOG` to perform the actual logging. The `LOG` logger (not shown in the example) should be configured in the standard way in Spring. Maybe as:

```properties
logging.level.app.accounting.reports.Reporting=TRACE
```

In Full Mode (activated by TRACE in this case) LiveSQL's logging includes the query, the parameters, and the column types, as in:

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




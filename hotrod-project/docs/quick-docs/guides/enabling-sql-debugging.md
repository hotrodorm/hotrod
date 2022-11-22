# Enabling SQL Debugging

To see the exact SQL queries that are run in the database you can activate the logging for
it.

Edit the `application.properties` file (either the embedded or external one) and append
the logging configuration. For example you can add:

```properties
logging.level.com.myapp.daos.primitives.employee.selectByPK=TRACE
logging.level.com.myapp.daos.primitives.account=DEBUG
```

Each logging configuration line has a strict form that includes four sections. For example, 
the first line above can be separated as:

- `logging.level.`: this indicates this is a logging configuration line.
- `com.myapp.daos.primitives.employee`: the specific logger for a DAO. This is the DAO mapper
namespace that can be found in the first lines of the mapper XML file corresponding to each DAO.
- `.selectByPK`: the specific DAO method (optional). If this section is included this configuration
applied to a single DAO method. If absent the whole DAO is affected.
- `=TRACE`: The debugging level. A `DEBUG` level shows the SQL statement and the applied parameters,
while a `TRACE` level will also include all the selected data; use the `TRACE` level with caution 
since it can add a massive amount of logging to your log files.

For LiveSQL the logging can be configured as whole adding the configuration line:

```properties
logging.level.org.hotrod.runtime.livesql.LiveSQLMapper=DEBUG
```

## Example

When the application is run with logging enabled the application will produce loggin lines such as:

```log
DEBUG --- [main] c.m.daos.primitives.account.selectByPK  : ==>  Preparing: select id, name from account where id = ?
DEBUG --- [main] c.m.daos.primitives.account.selectByPK  : ==> Parameters: 123(Integer)
DEBUG --- [main] c.m.daos.primitives.account.selectByPK  : <==      Total: 1
```

In this log (generated with a `DEBUG` level) the persistence layer now shows the SQL statement that
is run, the parameters applied to each parameter placeholder, and the number of rows returned by the query.


## DevOps configuration

The embedded `application.properties` file is typically included in the packages application and defines the
default logging level for the application. When DevOps deploys the application the logging level can be
tweaked by overriding these values using an external `application.properties` file. If this is present and
in the same folder as the application, Spring Boot will automatically read it and their values will override
the default values.

This way the DevOps team can activate or deactivate the level of logging by using a separate file and without 
changing the application itself.


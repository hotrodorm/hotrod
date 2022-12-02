# Designating a LiveSQL Dialect

The generic version of LiveSQL automatically detects at runtime the specific SQL dialect of the database.

Therefore, generic clauses or functions will be adapted according to the specific features available on the database engine
and version of it.

## Example

The following example shows a LiveSQL query that limits the number of rows a query returns:

```java
List<Row> rows = sql
    .select()
    .from(t)
    .limit(15)
    .execute();
```

Depending on the specific database and version this query can be rendered in different ways. For example:

- In Oracle 10g:

    ```sql
    select *
    from (
      select *, rownum as rn from t 
    ) x
    where rn <= 15
    ```

- In Oracle 12c:

    ```sql
    select * from t fetch next 15 rows only
    ```

- In MySQL:

    ```sql
    select * from t limit 15
    ```

- In SQL Server:

    ```sql
    select top 15 * from t
    ```


## Designating the LiveSQL Dialect

Some teams use one database for prototyping and a different one for production. Sometimes the difference is only minor, such as minor versions difference of the same database engine, or in other cases they can use different engines altogether. In cases like this, the developer may want to override the autodetection
capabilities of LiveSQL and designate the dialect directly instead.

To do this the developer can add the following properties to the `application.properties` file that is used at runtime:

```properties
livesql.livesqldialectname=MYSQL
livesql.livesqldialectdatabaseName=MariaDB
livesql.livesqldialectversionString="10.5"
livesql.livesqldialectmajorVersion=10
livesql.livesqldialectminorVersion=5
```

The most important property is the fist one. The rest can be useful in cases where the SQL support has changed considerably over time; for example, Oracle 12c improved SQL support considerably and it's important for LiveSQL to know how the target version of the database to render the SQL statements appropriately.


## Designating the LiveSQL Dialect for Multiple DataSources

When an application uses multiple datasources &ndash; either different databases or the several instances of the same database &ndash; 
the defaul datasource configuration cannot be used, but each datasource needs to be specified separately, including their dialects.
See [Using Multiple DataSources](../guides/using-multiple-datasources.md) for an example on how to define multiple datasources.

The `application.properties` file can take the form:

```properties
# General configuration of the app

logging.level.root=INFO

# First datasource configuration

datasource1.driver-class-name=com.mysql.cj.jdbc.Driver
datasource1.jdbc-url=jdbc:mysql://192.168.56.29:3306/database1
datasource1.username=user1
datasource1.password=pass1
datasource1.mappers=mappers/mysql

datasource1.livesqldialectname=MYSQL
datasource1.livesqldialectdatabaseName=MariaDB
datasource1.livesqldialectversionString="10.6"
datasource1.livesqldialectmajorVersion=10
datasource1.livesqldialectminorVersion=6

# Second datasouce configuration

datasource2.driver-class-name=org.postgresql.Driver
datasource2.jdbc-url=jdbc:postgresql://192.168.56.214:5432/database2
datasource2.username=user1
datasource2.password=pass1
datasource2.mappers=mappers/postgresql

datasource2.livesqldialectname=POSTGRESQL
datasource2.livesqldialectdatabaseName="PostgreSQL 12"
datasource2.livesqldialectversionString="12.4 (Linux)"
datasource2.livesqldialectmajorVersion=12
datasource2.livesqldialectminorVersion=4
```

Notice that the properties' prefixes can be changed to any string, as configured by the `@Configuration` classes.




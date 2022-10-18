# Designating a LiveSQL Dialect

The generic version of LiveSQL automatically detects at runtime the specific SQL dialect of the database.

Therefore, generic clauses or functions will be adapted according to the specific features available on the database engine
and version of it.

## Example

The following example shows a LiveSQL query that limits the number of rows a query returns:

```java
    List<Map<String, Object>> rows = sql
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

# Forcing the LiveSQL Dialect

Some teams use one database for prototyping and a different one for production. Sometimes the difference is only minor, such as minor versions difference of the same database engine, or in other cases they can use different engines altogether. In cases like this, the developer may want to override the autodetection
capabilities of LiveSQL and designate the dialect directly instead.

To do this the developer can add the following properties to the `application.properties` file that is used at runtime:

```properties
livesql.dialect.name=H2
livesql.dialect.databaseName=H2
livesql.dialect.versionString="1.4.197 (2018-03-18)"
livesql.dialect.majorVersion=1
livesql.dialect.minorVersion=4
```

The most important property is the fist one. The rest can be useful in cases where the SQL support has changed considerably over time; for example, Oracle 12c improved SQL support considerably and it's important for LiveSQL to know how the target version of the database to render SQL appropriately.



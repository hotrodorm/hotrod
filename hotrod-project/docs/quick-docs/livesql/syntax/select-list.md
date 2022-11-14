# The SELECT List

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

## Variations

The following variations are implemented:

### Select All Columns:

To select all columns of the table(s) start the query with:

```java
this.sql.select()
```

The resulting SQL statement starts with:

```sql
select *
```

### Select Distinct Rows:

    ```java
    this.sql.selectDistinct()
    ```

    Produces:

    ```sql
    select distinct *
    ```

### Select Specific Columns:

    ```java
    this.sql.select(a.name, a.status)
    ```

    Produces:

    ```sql
    select a.name, a.status
    ```

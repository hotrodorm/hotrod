# The SELECT List

A SELECT query starts with the SELECT List. This section specifies the columns that are to be retrieved as well as the modifiers for them.

## Variations

The following variations are implemented:

- Select All Columns:

    ```java
    this.sql.select()
    ```
Produces:

    ```sql
    select *
    ```

- `SELECT distinct *`:

    ```java
    this.sql.selectDistinct()
    ```

Produces:

    ```sql
    select distinct *
    ```

- Select Specific Columns:

    ```java
    this.sql.select(a.name, a.status)
    ```

Produces:

    ```sql
    select a.name, a.status
    ```

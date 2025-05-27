# General Purpose Queries

A General Purpose query does not return a result set and is implemented using `<query>` tag. Its
only return may be an optional count of affected rows.

Typically these correspond to `UPDATE`, `DELETE` SQL statements, but can actually include any valid 
SQL statement in the specific database such as `INSERT`, `CREATE`, `ALTER`, etc. including DML 
statements and stored procedures calls.

General purpose queries can be included in the definition of tables (`<table>`), views (`<view>`), 
and general DAO (`<dao>`) tags. See
[Configuration File Structure](../config/README.md) for details.

## Examples

A typical DML query could look like:

```xml
<query method="closeFullyPaidInvoices">
  update invoice
  set outstanding = 0
  where amount_paid >= amount_receivable
</query>
```

Nevertheless, any valid native query can be used. For example:

```xml
<query method="prepareDailyTransactions">
  truncate daily_transactions_tbl
</query>
```

The above two queries return the number of affected rows and are exposed to the developer as Java methods:

```java
public int closeFullyPaidInvoices() { ... }

public int prepareDailyTransactions() { ... }
```


## Features

Nitro General Purpose queries can have parameters, use Native SQL, and use DynamicSQL.

### 1. Parameters

All Nitro queries can have parameters that can be directly applied or injected in the query and that can
also be used to control the DynamicSQL logic. From the application's perspective these parameters become
parameters in the DAO method that executes the query.

For details on the definition and usage of parameters see [Nitro Parameters](./nitro-parameters.md).


### 2. The `<complement>` Tag

Unlike SELECT queries, general purpose queries do not need a column discovery phase and, therefore, do not need
the use of the `<complement>` tag.


### 3. Native SQL

All native SQL statements are supported to take full advantage of the database dialect features.


### 4. Dynamic SQL

General purpose queries can be enhanced with [Dynamic SQL](./nitro-dynamicsql.md). Dynamic SQL allows the query to include or
exclude fragments of the SQL statement at runtime, based on the values of the supplied parameters.





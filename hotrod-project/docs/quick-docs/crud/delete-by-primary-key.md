# Delete by Primary Key

This method provided by CRUD deletes a row in a table of the database.

The primary key must be provided in the VO parameter of the method.


## Example

The following example deletes an invoice from the invoice table:

```sql
create table invoice (
  id int primary key not null,
  client_id int not null,
  total int not null
);
```

```java
@Autowired
private InvoiceDAO invoiceDAO;

...

InvoiceVO i = InvoiceDAO.selectByPK(58640);
this.invoiceDAO.deleteByPK(i);
```


## Optimistic Locking

Optimistic locking can detect if a row has been modified between it's read and it's deleted.
This can be useful to detect race conditions and act accordingly when multiple processes are 
modifying or deleting the same row simultaneously. See
[Optimistic Locking](../config/tags/version-control-column.md#optimistic-locking) for configuration
details.

The example above has been enhanced by adding a new column `row_version` and by enabling optimistic
locking in the table using the `<version-control-column>` tag. It now detects changes in the row:

```sql
create table invoice (
  id int primary key not null,
  client_id int not null,
  total int not null,
  row_version int
);
```

```java
@Autowired
private InvoiceDAO invoiceDAO;

...

InvoiceVO i = InvoiceDAO.selectByPK(58640);
try {
  this.invoiceDAO.deleteByPK(i);
} catch (RuntimeException e) {
  System.out.println("Concurrent invoice change detected. Invoice not deleted.");
}
```

In the case shown above the logic detects the row change (maybe someone changed the invoice or deleted 
it) and the current delete fails. Typically, the business logic will wait a short while and then 
would read the fresh row (again) and retry the delete for a number of times.

Notice that the new `row_version` column is used silently in the application and does not 
pollute the source code.


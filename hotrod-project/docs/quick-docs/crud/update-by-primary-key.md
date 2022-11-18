# Update by Primary Key

This method provided by CRUD updates a row in a table of the database.

The primary key must be provided in the VO for CRUD to locate and update the row. All
columns except the primary key are updated according to values (null or not null)
in the VO.


## Example

The following table adds $100 to the balance of the account.

```sql
create table prepaid_account (
  id int primary key not null,
  balance int not null
);
```

```java
@Autowired
private PrepaidAccountDAO prepaidAccountDAO;

...

PrepaidAccountVO acc = PrepaidAccountDAO.selectByPK(1005);
acc.setBalance(acc.getBalance() + 100);
this.prepaidAccountDAO.updateByPK(acc);
```


## Optimistic Locking

Optimistic locking can detect if a row has been modified between it's read and it's updated.
This can be useful to detect race conditions and act accordingly when multiple processes are 
modifying the same row simultaneously. See
[Optimistic Locking](../config/tags/version-control-column.md#optimistic-locking) for configuration
details.

The example above has been enhanced by adding a column `row_version` and enabling optimistic
locking in the table using the `<version-control-column>` tag. It not detects changes in the row:

```sql
create table prepaid_account (
  id int primary key not null,
  balance int not null,
  row_version int
);
```

```java
@Autowired
private PrepaidAccountDAO prepaidAccountDAO;

...

PrepaidAccountVO acc = PrepaidAccountDAO.selectByPK(1005);
acc.setBalance(acc.getBalance() + 100);
try {
  this.prepaidAccountDAO.updateByPK(acc);
} catch (RuntimeException e) {
  System.out.println("Concurrent balance modification detected. Row not updated.");
}
```

In the case shown above the logic detects the row change (maybe someone deposited or withdrew
money) and the update fails. Typically, the business logic will wait a short while and then 
would read the fresh row (again) and retry the update for a number of times.

Notice that the new `row_version` column is used silently in the application and does not 
pollute the source code.

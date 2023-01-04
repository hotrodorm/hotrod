# Natural Typing Identifiers

HotRod adopts Natural Typing Identifiers when naming database objects in the configuration file.

The configuration file references existing database objects by name in several places. For example,
it needs to name a table, a view, a column, a sequence, etc. These names are known as **database identifiers**.

The natural typing form allows the objects to be typed in the HotRod configuration file
as they would be typed in a SQL statement, giving preference to lower case typing. Let's look at canonical
identifiers first to see how they differ from natural identifiers.

## Canonical Identifiers

Canonical Identifiers correspond to the internal official name each database assigns to a database object. This applies to all
database objects such as tables, views, columns, sequences, constraints, etc. The logic to search for these
identifiers changes per database (Oracle works differently compare to PostgreSQL), sometimes changes per platform (e.g. DB2), 
or per OS (e.g. MySQL works differently in Windows and in Linux).

For example, when you create a table with a SQL command like:

```sql
create table order_line (
  product_id int primary key not null,
  quantity int not null
)
```

The database will create a table `order_line`. Each database will register the name in a different way:

| Database | Canonical Name |
| -- | -- |
| Oracle | `ORDER_LINE` |
| DB2 | `ORDER_LINE` |
| PostgreSQL | `order_line` |
| SQL Server | `order_line` [^1] |
| MySQL (Windows) | `ORDER_LINE` |
| MySQL (Linux) | `order_line` |
| MariaDB (Windows) | `ORDER_LINE` |
| MariaDB (Linux) | `order_line` |
| SAP ASE | `order_line` |
| H2 | `ORDER_LINE` |
| HyperSQL | `ORDER_LINE` |
| Apache Derby | `ORDER_LINE` |

[^1]: The default settings of SQL Server don't differentiate between lower and upper case identifiers. This behavior can be changed
by changing the database encoding with one which names ends with `_CS` (case sensitive). This change has multiple side effects, so it's
not recommended to do it unless you have a lot time to test them thoroughly.



## Natural Typing Identifiers

Natural Typing Identifiers differ from Canonical Identifiers. 


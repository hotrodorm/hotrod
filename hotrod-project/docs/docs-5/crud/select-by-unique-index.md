# Select by Unique Index

The `selectByUI...()` methods are implemented in DAOs related to tables when this table for each unique index on it.

Each DAO method receives as parameters the values for the members of the index and returns a VO with the selected row.
Keep in mind that unique constraints also generate unique indexes. These indexes are considered as any other unique index and are 
included in the DAO.

The resulting unique index produced by the primary key is already covered by the [Select By Primary Key](./select-by-primary-key.md) 
method and, therefore, is not implemented as a separate unique index method.


## Example

In the example shown below the first table has a unique index, the second table
has three indexes (two being unique), and the third one doesn't have a unique index:

```sql
create table widget (
  widget_id int primary key not null,
  code char(6)
  name varchar(60)
);

create unique index widget_ix1 on widget (code);

create table car (
  id int primary key not null,
  brand_id int,
  type char(4),
  vin char(20) unique,
  region decimal(8),
  engine_number varchar(24)
);

create index car_ix1 on car (brand_id, type);

create unique index car_ix2 on car (region, engine_number);

create table medication (
  med_id int primary key not null,
  patient_id int,
  fequency number(8)
);

create index med_ix1 on medication (patient_id);
```

CRUD will produce one DAOs for each table:
- `WidgetDAO` will implement the method `WidgetVO selectByUICode(String code)`.
- `CarDAO` will implement the methods `CarVO selectByUIVin(String vin)` and `CarVO selectByUIRegionEngineNumber(Integer region, String engineNumber)`.
- `MedicationDAO` will not include any `selectByUI...()` method since it doesn't have any unique index (besides 
the primary key).


## Column Seam

In rare occasions multiple DAO method may end up having the exact same name, a *name collision* that may result in
an invalid DAO class. In these cases it's possible to differentiate them by defining an explicit
[Column Seam](./column-seam.md) to resolve the name collision.








# The `<table>` Tag

A `<table>` tag tells the HotRod generator to include a table in the code generation. Each `<table>` tag adds one table
to the persistence layer.

## Attributes

In its simplest form this tag only specifies the table name. Table columns are inspected and all DAO and VO
classes are produced according to the default logic. Names are produced in camel-case according to the
table and column names and can be customized by the [`<name-solver>`](./name-solver.md) rules. Data types are produced
according to the [default types for each database](../supported-databases.md) and can be customized by 
the [`<type-solver>`](./type-solver.md) rules.

The tag can also include more modifiers that can be useful in special cases, as shown below:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the table in natural typing | Required |
| `catalog` | The catalog of the table, if different from the default one | The current catalog, specified in the runtime properties file |
| `schema` | The schema of the table, if different from the default one | The current schema, specified in the runtime properties file |
| `java-name` | Sets the Java class name base for the DAO and VO | Camel-case identifier based on the database identifier |
| `column-seam` | Separator string to add when producing a Java identifier from multiple columns | *empty-string* |

## Natural Typing Identifiers

See [Natural Typing Identifiers](../natural-typing-identifiers.md).

## Customizing Columns

As described before the naming of the column properties and their types follow rules specified by the `<name-solver>` and `<type-solver>` and,
in the absence of these, follow default rules that depend on each database.

Nevertheless, if the name or type of the column produced by the rules above is not suitable for one or more columns, you can add a `<column>` tag
that can set its name or type directly.

For example, if the following table is created (PostgreSQL) as:

```sql
create table invoice (
  id int primary key not null,
  ux_pre_tax_amt decimal(10, 2),
  tax_percent decimal(6, 4),
  created_at timestamp,
  status int check (status in (1, 2, 5, 6))
)
```

By default HotRod will generate the following columns and types:

| Database Column | VO Property | VO Type |
| -- | -- | -- |
| ID | id | Integer |
| CREATED_AT | createdAt | java.sql.Timestamp |
| TAX_PCT | taxPercent | java.math.BigDecimal |
| UX_TOT_TAX_AMT | uxTotTaxAmt | java.math.BigDecimal |
| STATUS | status | String |

If we wanted to override these names and types and decided for:
- `CREATED_AT` as `java.time.LocalDateTime`.
- `TAX_PC` with the property name `taxPercent`.
- `UX_TOT_TAX_AMT` with the property name `totalAmount` and type `Double`.
- `STATUS` to be represented by a Java enum (maybe with values `CREATED`, `APPROVED`, `REJECTED`, `PAID`).

We can use `<column>` tags to set them as needed:

```xml
  <table name="invoice">
    <column name="created_at" java-type="java.time.LocalDateTime" />
    <column name="tax_pct" java-name="taxPercent" />
    <column name="ux_tot_tax_amt" java-name="totalAmount" java-type="Double" />
    <column name="status" converter="invoiceStatusConverter" />
  </table>
```

The columns that are not mentioned are not affected and their names and types use the default rules. Note,
also, that changes can be combined in the same tag, as shown for the column `UX_TOT_TAX_AMT` that sets its
name and type.









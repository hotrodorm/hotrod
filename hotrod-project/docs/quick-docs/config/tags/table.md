# The `<table>` Tag

A `<table>` tag tells the HotRod generator to include a table in the code generation. Add one `<table>` tag for
each table you want to include in the persistence layer.

In the simplest form this table can only specify the table name. Table columns are inspected and all DAO and VO
classes are produced according to the defaults of each database. Names are produced in camel-case according to the
table and column names and can be customized by the [`<name-solver>`](./name-solver.md) rules. Data types are produced
according to the [default types for each database](../supported-databases.md) and can be customized by 
the [`<type-solver>`](./type-solver.md) rules.

The tag can also include more modifiers that can be useful in special cases. See table below:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the table in natural typing | N/A. Required |
| `catalog` | The catalog of the table, if different from the default one | The current catalog, specified in the runtime properties file |
| `schema` | The schema of the table, if different from the default one | The current schema, specified in the runtime properties file |
| `java-name` | Sets the Java class name base for the DAO and VO | Camel-case identifier based on the database identifier |
| `column-seam` | Separator string to add when producing a Java identifier from multiple columns | *empty-string* |

## Natural Typing Identifiers

See [Natural Typing Identifiers](../natural-typing-identifiers.md).






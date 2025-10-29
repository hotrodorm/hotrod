# The `<type-solver>` Tag

HotRod's configuration can include a rule based static type solver that can decide on the types
of the columns for tables, views, and Nitro select queries. These data type are used when producing the layout classes of the persistence layer.

The static type solver is an integral part of the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md). The [Hello Type Resolution](../../guides/hello-type-resolution.md) show the static type solver in action, along with the rest of the type resolution rules.

In order to do this the developer can add the `<type-solver>` tag. For example:

```xml
<type-solver>
    <when test="scale > 0" type="java.math.BigDecimal" />
    <when test="name.matches('.*_IMAGE') and size > 10000" converter="ByteArrayConverter" />
    <when ... />
</type-solver>
```

For each column the rules are checked in order. If any of them is evaluated as `true` then it's used to determine the type of 
the property, and no further rules are evaluated. 

The available properties (such as `scale`, `name`, and `size` above) depend on each specific database. To find the full list of available
properties on your specific database schema get the column metadata using the [Export Columns](../../maven/goal-export-columns-txt.md) 
command (in TXT or XLSX format).

## Precedence &mdash; Tables &amp; Views

Every table or view in scope column is read as a property on the Layout class. The property class is computed according to the following rules:

1. If there's a `<column>` tag for the column with a  type or converter, then this type or converter is used. The rest of the rules are ignored.

2. If there's `test` rule in the `<type-solver>` that matches the column, then this rule specifies the type for the property. The rest of the rules are ignored.

3. If none of the rules above is matched, then the database adapter for the specific database provides a default type for the column. See [Default Types](../supported-databases.md) for each database.

## Precedence &mdash; Queries

Every query column is read as a property on the Layout class. The property class is computed according to the following rules:

1. If there's a `<column>` tag for the column with a  type or converter, then this type or converter is used. The rest of the rules are ignored.

2. If there's `test-resultset` rule in the `<type-solver>` that matches the column, then this rule specifies the type for the property. The rest of the rules are ignored.

3. If none of the rules above is matched, then the database adapter for the specific database provides a default type for the column. See [Default Types](../supported-databases.md) for each database.

## The `test` Expressions

This expression declares a predicate to be evaluated. If found true that rule is selected and no further rules are processed.

These expressions are written in JEXL syntax. For details on the JEXL's syntax see [Apache Commons JEXL](https://commons.apache.org/proper/commons-jexl/).

The available properties for the column metadata are described in [TXT Column Metadata](../../maven/goal-export-columns-txt.md). *Standard properties*
are available across all databases, while *native properties* enhance the column metadata, but depend on each specific database.

## Resulting Type

If a rule is matched the resulting type specified in the rule is applied to the column property.

A `<when>` tag has three attributes that are used to specify the resulting type:

 - `type`
 - `converter`
 - `force-jdbc-type-on-write`

The tag can either specify the `java-type` or the `converter` attributes; they are exclusive. If the `java-type` is specified, this is taken as the final type to use in the generated code. Alternatively, it can specify `converter` in case a converter needs to be used for the column.

When using `java-type` option, the attibute `force-jdbc-type-on-write` can be specified in case the developer needs to force the JDBC type when sending a
value back to the database, typically on the `UPDATE` or `INSERT` SQL statements.


 
 
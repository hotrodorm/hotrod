# The `<runtime-type-solver>` Tag

The Runtime Type Solver can apply rules to decide the types of expressions in LiveSQL SELECT queries.

The runtime type solver is an integral part of the [Type Resolution Mechanics](../../guides/type-resolution-mechanics.md). The [Hello Type Resolution](../../guides/hello-type-resolution.md) show the runtime type solver in action, along with the rest of the type resolution rules.

These rules do not affect columns from tables or views since their type is resolved in a static manner during the layer generation. These rules do not affect expressions which type has been designated using the `.type(<class>)` or `.type(<converter-bean>)` clauses.

The rules are expressed in JEXL syntax and can use all the properties of the `java.sql.ResultSetMetaData` class. For example:

```xml
<type-solver>
    <when test="scale > 0" java-type="java.math.BigDecimal" />
    <when test="columnName.matches('.*_IMAGE') and size > 10000" converter="ByteArrayConverter" />
    <when ... />
</type-solver>
```

For each column the rules are checked in order. If any of them is evaluated as `true` then it's used to determine the type of the property, and no further rules are evaluated.

## The ResultSetMetaData Properties

The following properties, exposed by the JDBC driver, can be used in the JEXL expressions:

| Property | Type |
| :-- | :-- |
| catalogName | String |
| schemaName | String |
| tableName | String |
| columnName | String |
| columnLabel | String |
| columnType | int |
| columnTypeName | String |
| nullable | int |
| signed | boolean |
| columnClassName | String |
| columnDisplaySize | int |
| precision | int |
| scale | int |
| autoIncrement | boolean |
| caseSensitive | boolean |
| currency | boolean |
| definitelyWritable | boolean |
| readOnly | boolean |
| searchable | boolean |
| writable | boolean |

## The `test` Expression

This expression declares a predicate to be evaluated. If found true that rule is selected and no further rules are processed.

These expressions are written in JEXL syntax. For details on the JEXL's syntax see [Apache Commons JEXL](https://commons.apache.org/proper/commons-jexl/).

## Resulting Type

If a rule is matched the resulting type specified in the rule is applied to the column property.

A `<when>` tag has three attributes that are used to specify the resulting type:

 - `java-type`
 - `converter`
 - `force-jdbc-type-on-write`

The tag can either specify the `java-type` or the `converter` attributes; they are exclusive. If the `java-type` is specified, this is taken as the final type to use in the generated code. Alternatively, it can specify `converter` in case a converter needs to be used for the column.

When using `java-type` option, the attibute `force-jdbc-type-on-write` can be specified in case the developer needs to force the JDBC type when sending a
value back to the database, typically on the `UPDATE` or `INSERT` SQL statements.


 
 
# The `<type-solver>` Tag

HotRod's configuration can include a rule based type solver that can assign types or converters to VO properties according 
to custom logic specified by the developer. 

In order to do this the developer can add the `<type-solver>` tag. For example:

```xml
<type-solver>
    <when test="scale > 0" java-type="java.math.BigDecimal" />
    <when test="name.matches('.*_IMAGE') &amp;&&amp; size > 10000" converter="ByteArrayConverter" />
    <when ... />
</type-solver>
```

For each column the rules are checked in order. If any of them is evaluated as `true` then it's used to determine the type of 
the property, and no further rules are evaluated. 

See the full list of available properties of a column at [Column Metadata](../../maven/command-export-columns-txt.md#properties).

## Location

See [Configuration File Structure](../configuration-file-structure.md) for the correct location of this tag in the configuration file.

## Precedence

When a table or view column produces a VO property, the property type is assigned according to the following precedence:

1. If there's a &lt;column> tag for the column that specifies its type or a converter, this type or converter is used with no further processing.

2. If there's rule in the type solver that matches the column, then this rule specifies the type for the VO property, with no further processing.

3. If none of the rules above is matched, then the database adapter for the specific database provides a default type for the column.

## The `test` Expression

The `test` attribute includes a boolean expression that is evaluated. If found true that rule is selected and no further rules are processed.

The expression in the `test` attribute use OGNL syntax. For details on the OGNL syntax in the section *Appendix: OGNL Language Reference* 
of [Apache Commons OGNL](https://commons.apache.org/proper/commons-ognl/language-guide.html).

Most of the time the `test` attribute includes a simple predicate; however, the full power of OGNL syntax can be used to implement complex rules, as needed.  

The available properties for the column metadata are described in [TXT Column Metadata](../../maven/command-export-columns-txt.md). *Standard properties* 
are available across all databases, while *native properties* enhance the column metadata, but depend on each specific database.

## Resulting Type

If a rule is matched the resulting type specified in the rule is applied to the column property.

A `<when>` tag has three attributes that are used to specify the resulting type:

 - `java-type`
 - `converter`
 - `force-jdbc-type-on-write`
 
The tag can either specify the `java-type` or the `converter` attributes; they are exclusive. If the `java-type` is specified, this is taken as the final type to use in the generated code. Alternatively, it can specify `converter` in case a converter needs to be used for the column.

When using `java-type` option, the attibute `force-jdbc-type-on-write` can be specified in case the developer needs to force the JDBC type when sending a
value back to the database, typically on the `UPDATE` or `INSERT` SQL statements.


 
 
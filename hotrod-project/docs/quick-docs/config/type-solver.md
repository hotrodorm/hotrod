# The Type Solver

HotRod's configuration can include a rule based type solver that can assign types or converters to VO properties according to custom logic specified by the developer.

In order to do this the developer can add the <&lt;type-solver> tag. For example:

    <type-solver>
      <when test="c.decimalplaces > 0" java-type="BigDecimal" jdbc-type="NUMERIC" />
      <when test="c.table = 'account' && c.isBlob" converter="ByteArrayConverter" />
      <when ... />
    </type-solver>

For each column the rules are checked in order. If any of them is evaluated as `true` then it's used to determine the type of the property.

The `test` attribute specifies a boolean expression that is evaluated that uses full OGNL expression syntax. 
For details on the OGNL syntax in the section *Appendix: OGNL Language Reference* 
of [Apache Commons OGNL](https://commons.apache.org/proper/commons-ognl/language-guide.html).


## Precedence

When a table or view column produces a VO property, the propery type is assigned according to the following precedence:

1. If there's a &lt;column> tag for the column that specifies its type or a converter, this type or converter is used with no further processing.

2. If there's rule in the type solver that matches the column, then this rule specifies the type for the VO property, with no further processing.

3. If none of the rules above is matched, then the database adapter for the specific database provides a default type for the column.

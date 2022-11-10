# The `<parameter>` Tag

This tags defines a parameter of a Nitro query and is used by the `<query>` and `<select>` tags.

Each query can include zero, one, or more `<parameter>` tags.

For more details see [Nitro Queries](../../nitro/nitro.md).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the parameter | Required |
| `java-type` | The java type for the Java method parameter | Required |
| `jdbc-type` | The JDBC type [^1] | Required |
| `sample-sql-value` | A sample value. Used by the generator when assembling the whole SQL statement. This is rarely needed for exotic database types only. The value provided replaces the parameter in the query and must ensure the whole query ends up being a valid query | Internally produce value |


[^1]: This is a requirement of the JDBC spec where each nulls needs to have a type when applied as a query parameter. See [java.sql.Types](https://docs.oracle.com/javase/8/docs/api/java/sql/Types.html) for a list of valid types.


# The `<parameter>` Tag

This tags defines a parameter of a Nitro query and is used by the `<query>` and `<select>` tags.

Each query can include zero, one, or more `<parameter>` tags.

For more details see [Nitro Queries](../../nitro/README.md).


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the parameter | Required |
| `type` | The java type for the Java method parameter | Required |
| ~~`java-type`~~ | *Deprecated*. Use `type` instead | |
| `jdbc-type` | The JDBC type. Only required when the parameter is used in the SELECT list of a query | N/A |
| `sample-sql-value` | A sample value. Used by the generator when assembling the whole SQL statement. This is rarely needed if at all, and it's for exotic database types only. The value provided replaces the parameter in the query during the generation phase only (not in the application) and must ensure the whole query ends up being a valid query | Internally produced value |

See [Nitro Parameters](../../nitro/nitro-parameters.md) for examples.



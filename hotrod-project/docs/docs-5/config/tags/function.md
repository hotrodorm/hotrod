# The &lt;function> Tag

This tag configures the name and template of a custom LiveSQL function.

As of version 5.1.27 the SQL templates of the custom functions are now defined statically when the persistence layer is generated and cannot be modified at runtime. This is essential to keep the application fully protected from SQL Injection.

## Sub Tags

This tag does not include any subtags.

## Attributes

This tag includes the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The name of the function as known in the source code. Must start with a letter and continue with letters, digits, and/or underscore symbols | N/A |
| `template`  | Specifies SQL template for this custom function. See [Function Templates](../../livesql/extending-livesql-functions.md#function-templates) | N/A |

## SQL Templates


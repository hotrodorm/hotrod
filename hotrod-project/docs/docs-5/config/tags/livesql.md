# The &lt;livesql> Tag

This is an optional tag that can be used to define custom LiveSQL functions that are used by the application. If your application does not define custom LiveSQL functions you can safely ignore this tag.

## Example

If your application defines two custom LiveSQL functions this section of the configuration file may take the form:

```xml
<livesql>
  <function name="random" template="(random(123) * (#{}))" />
  <function name="adddays" template="dateadd(day, #{}::integer, #{}::date)" />
</livesql>
```

The SQL templates of the custom functions are, therefore, defined statically when the persistence layer is generated and cannot be modified at runtime. This is essential to keep the application fully protected from SQL Injection.


## Sub Tags

This tag can include one or more of the function tags as shown below:

| Sub Tag | Description |
| -- | -- |
| [&lt;function>](./function.md) | Configure the name and template of a custom LiveSQL function |

## Attributes

This tag does not have any attribute.

 
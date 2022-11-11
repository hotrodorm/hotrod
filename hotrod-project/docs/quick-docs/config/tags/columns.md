# The `<columns>` Tag

This tags can be used in a `<select>` query tag and designates the query as a Structured Select rather
than a Flat Select.


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `vo` | Specifies the name of the resulting VO if the query produces a single VO | N/A |
| `id` | The 'id' attribute must be specified when the 'vo' attribute is specified and <expression> tags are included. It includes the comma-separated list of properties that uniquely identify a row | N/A |  


## General Structure


This tag can include one or more `<vo>`, `<expression>`, or `<association>` tags. See [Nitro Queries](../../nitro/nitro.md).



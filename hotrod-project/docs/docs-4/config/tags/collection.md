# The `<collection>` Tag

This tag can be used inside a Graph Select (`<select>` tag) and specifies the inclusion of a joined 
collection table or view in the query.

A collection represents a joined table or view that can produce zero, one, or more related elements. As such,
an included collection entity can be represented by a property with the `List<>` type in the parent VO.


## Attributes

This tag is a semantic variation of the `<vo>`, and as such, includes the same attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `table` | The table name of the entity. Mutually exclusive with the `view` attribute | N/A |
| `view` | The view name of the entity. Mutually exclusive with the `table` attribute | N/A |
| `catalog` | The catalog of the table or view, if different from the default one | N/A |
| `schema` | The schema of the table or view, if different from the default one | N/A |
| `id` | Must be specified when the `vo` attribute is specified and `<expression>` tags are also included. It includes the comma-separated list of properties that uniquely identify a row | N/A |  
| `property` | The Java property that will hold the entity values in the composite VO | N/A |
| `alias` | The table or view alias in the SQL query, if any | N/A |
| `extended-vo` | The name of the extended VO if the extra expressions are added to the entity VO | N/A |


## General Structure


This tag can include one or more `<expression>`, `<association>`, or `<collection>` tags. See [Nitro Queries](../../nitro/nitro.md).



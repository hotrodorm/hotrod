# The `<expression>` Tag

This tags can be used inside a Structured Select (`<select>` tag) and specifies the inclusion of an
extra computes expression in the SELECT list.

An expression is by definition external to the represented entities and needs to be modeled as an extra 
separated property in the parent VO.


## Attributes

This tag is a semantic variation of the `<vo>`, and as such, includes the same attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `property` | The property name in the parent VO | Required |
| `class` | Overrides the property class in the parent VO. Mutually exclusive with `converter` | N/A |
| `converter` | Specifies a converter will be used to convert the expression values. Mutually exclusive with `class` | N/A |


## General Structure


This tag can be included in the `<columns>`, `<vo>`, `<association>`, or `<collection>` tags. See [Nitro Queries](../../nitro/nitro.md).



# The `<sequence>` Tag

This tag enables the retrieval of sequence values from a DAO. `<sequence>` tags can be added to any `<table>`, `<view>`, or `<dao>` tag.

The resulting method can independently retrive the value of a sequence, unrelated to any other SQL statement such as `INSERT`.


## Attributes

This tag can include the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The sequence name | Required |
| `catalog` | The catalog of the sequence, if different from the detault one | N/A |
| `schema` | The schema of the sequence, if different from the detault one | N/A |
| `method` | The Java method that will retrieve a value from the sequence | Required |



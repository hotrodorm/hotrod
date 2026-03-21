# The &lt;sequence> Tag

This tag enables the retrieval of sequence values from a DAO. &lt;sequence> tags can be added to any &lt;table>, &lt;view>, or &lt;dao> tag.

The resulting method can independently retrieve the value of a sequence, unrelated to any other SQL statement such as INSERT or SELECT.


## Attributes

This tag can include the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `name` | The sequence name | Required |
| `method` | The method that will retrieve a value from the sequence | Required |
| `catalog` | The catalog of the sequence, if different from the current catalog | N/A |
| `schema` | The schema of the sequence, if different from the current schema | N/A |



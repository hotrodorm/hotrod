# The `<mappers>` Tag

This is an optional tag used to configure the location of the generated MyBatis mapper files.

It has the following attributes:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `base-dir` | The base dir where to generate the mapper files | `src/main/resources` |
| `dir` | The relative dir (relative to the base dir) where to generate the mapper files | `mappers` |

If this tag is omitted the mappers are generated at `src/main/resources/mappers`.


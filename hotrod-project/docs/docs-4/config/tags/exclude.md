# The `<exclude>` Tag

Each `<exclude>` tag added to the configuration informs the HotRod generator to exclude tables or views from the persistence layer.

This tag only affects the auto-discovery feature of HotRod. If a table or view is directly included with a `<table>` or `<view>` tags, the `<exclude>` tag
does not exclude it.


## Attributes

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the table or view to exclude | Required |
| `catalog` | The catalog of the table or view to exclude | The current catalog, specified in the runtime configuration or properties file |
| `schema` | The schema of the table or view to exclude | The current schema, specified in the runtime configuration or properties file |

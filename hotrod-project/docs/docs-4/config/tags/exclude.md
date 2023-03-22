# The `<exclude>` Tag

An `<exclude>` tag can be added to a `<schema>` or `<current-schema>` tag to exclude a table or view from being discovered 
and added to the configuration.See [`<discover>`](./discover.md) and [Schema Discovery](../guides/schema-discovery.md).

**Note**: if a table or view is explicitly included by a `<table>` or `<view>` tags, the `<exclude>` tag does not exclude it.


## Attributes

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the table or view to exclude | Required |


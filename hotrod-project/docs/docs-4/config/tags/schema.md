# The `<schema>` Tag

This tag specifies a schema to discover tables and views. See [`<discover>`](./discover.md) and [Schema Discovery](../../guides/schema-discovery.md).


## Attributes

This tag can include the following attributes:

| Attribute | Description | Defaults to |
| --- | --- | --- |
| `name` | The name of the schema | Required |
| `catalog` | The catalog where this schema resides  | Optional |


## Included Tags

One or more [`<exclude>`](./exclude.md) tags can be added to this tag to specify tables or views that should be excluded from discovery


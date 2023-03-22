# The `<current-schema>` Tag

This tag specifies that the current schema should be included for discover tables and views. See [`<discover>`](./discover.md) and [Schema Discovery](../../guides/schema-discovery.md).

The current schema is not specified in the HotRod main configuration file, but it's indicated as a property using the `jdbccatalog` and `jdbcschema` properties in the runtime configuration.


## Attributes

This tag has no attributes.


## Included Tags

One or more [`<exclude>`](./exclude.md) tags can be added to this tag to specify tables or views that should be excluded from discovery


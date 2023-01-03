# The `<mybatis-spring>` Tag

This tag can include one or more generators. At the time of this writing only one generator remains active: the MyBatis-Spring generator. Other
generators have been deprecated.

It can optionally include any of the following tags, in the order shown below:

- A `<daos>` tag to configure the details of the generated Java classes, such as base dir, packages, prefixed, and suffixes.
- An optional `<mappers>` tag to configure the location of the generated mapper files.
- An optional `<select-generation>` tag to configure the SQL Processor for Nitro queries.
- One or more `<property>` tags to set up internal details of the generator engine.


## Attributes

This tag includes the following attribute:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `auto-discovery` | Enables or disables the auto-discovery of tables and views in the schema. Valid values are: `enabled`, `disabled`, and `enabled_when_no_objects_declared`.<br/>Auto-discovery of tables and views is enabled by default when the config file does not include any `<table>`, `<view>`, or `<dao>` tags | `enabled_when_no_objects_declared` |


## Auto-Discovery Mode

When auto-discovery mode is enabled all tables and views that are found in the current catalog/schema are included in the persistence layer, except 
for the tables or view specified by the [`<exclude>`](./exclude.md) tag.


# The `<discover>` Tag

Schema discovery inspects one or more database schemas to include all tables and view of them in the persistence layer. This can come in handy when starting a project with minimal configuration.

Schema discovery is enable by adding the `<discover>` tag. 

Schema discovery is mutually exclusive with facets. If you want to use discovery you cannot define facets, and vice versa.

Tables and view can be discovered in the current schema only, or in multiple schemas. Exception rules can also be specified to exclude tables or
views by name, per schema.

The DAO and VO names of discovered object follow the default rules using camel-case form, or can be further tailored with the use of a `<name-solver>` tag.

The data types of columns in discovered tables and views follow the default rules using for each database, and optionally tailored with the use of a `<type-solver>` tag.

For examples and use cases see [Schema Discovery](../../guides/schema-discovery.md).




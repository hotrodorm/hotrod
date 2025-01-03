# The `<select-generation>` Tag

The `<select-generation>` tag allows the developer to configure a SQL processor for 
[Nitro Flat Selects](../../nitro/nitro-flat-selects.md) and [Nitro Graph Selects](../../nitro/nitro-graph-selects.md).

The default `result-set` processor is currently supported by virtually all databases. This tag can activate the old processor
as in:

```xml
<mybatis-spring>
  ...
  <select-generation strategy="create-view" />
  ...
</mybatis-spring>
```

## Attributes

This tag includes the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| `strategy` | The processor strategy to parse Nitro selects | `result-set` |
| `temp-view-base-name` | Specifies the views' base name that the processor uses. Only used by the `create-view` processor | `hotrodtempview` |


## Processors

HotRod implements two SQL Processors:

| SQL Processor  | Notes | Benefits & Drawbacks |
|----------------|-------|---|
| `result-set` | Default processor, virtually supported by all databases nowadays. | Faster, inline parameters, single pass, no need for temp views |
| `create-view` | Compatible with all databases. Very old databases may only support this processor. | Great Compatibility. More cumbersome, since parameters need the `<complement>` tag and needs to create temp views in the sandbox database |


### The `result-set` Processor

The `result-set` processor analyzes queries by preparing JDBC Result Sets. It does not create views in the database schema, and
the connecting user only needs read-only access to the sandbox database.

Parameters can be placed anywhere in the SQL query and do not need to be enclosed in `<complement>` tags. The `<complement>` tag is 
still needed when Dynamic SQL tags are included in Nitro queries.


### The `create-view` Processor

The `create-view` Processor is the traditional processor implemented in HotRod. It analyzes SQL queries by creating a temporary views in 
the sandbox database. 

In order to create views this processor requires the connecting user to have `CREATE` privileges in the database schema.

This processor accepts parameters in the `WHERE` clause of the main query or any subquery. However, the location where the developer
can place parameters using this processor is somewhat limited compared to the new `result-set` processor. It also requires all 
parameters to be enclosed in `<complement>` tags.

This processor is enabled by default. It can also be explicitly set using the `<select-generation>` tag, as in:

```xml
  <select-generation strategy="create-view" />
```


## Comparison

The following table compares both Query Processors:

| Comparing  | `create-view` | `result-set`[^1] |
|----------------|-------|---|
| Database Privileges  | Needs the CREATE VIEW privilege in the sandbox database | Read-only access in the sandbox database |
| Parameters   | Only in WHERE clause, enclosed in a `<complement>` tag | Inline, anywhere allowed by JDBC |
| Database Connections | Needs 2 database connections. Two passes that may interfere with data load when specified in the JDBC URL | Single pass |
| Performance          | Slow on some databases[^2] | Fast |
| Drawbacks            | May leave dangling views under error conditions | May not work on very old databases |
| `<complement>` tag | Needed for parameters sections and for Dynamic SQL | Only for Dynamic SQL |


[^1]: This is the default, old processor.
[^2]: It has been observed that Oracle database seems to be particularly slow when retrieving database metadata; opening two connections
makes it even slower.

 
  
      
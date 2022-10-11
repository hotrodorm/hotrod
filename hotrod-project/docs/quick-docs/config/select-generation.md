# The Select Generation

The `<select-generation>` tag allows the developer to choose a SQL processor and to configure it. 

**Note**: Use the newer `result-set` processor that is currently supported by virtually all databases. This processor is scheduled to be the default processor in version 4.

The `<select-generation>` tag. This tag is optional and defaults to the old processor. It must be included in the `<mybatis-spring>` tag, as in:

    <mybatis-spring>
      ...
      <select-generation strategy="result-set" />
      ...
    </mybatis-spring>

HotRod implements two SQL Processors:

| SQL Processor  | Notes | Benefits & Drawbacks |
|----------------|-------|---|
| `create-view` | Compatible with all databases. Very old databases may only support this processor. | Great Compatibility. Parameters need the `<complement>` tag. Need to create temp views in the sandbox database |
| `result-set` | New processor, virtually supported by all databases nowadays. | Simplicity. Inline Parameters. Faster, single pass, no need to create temp views |

By default HotRod uses the traditional `create-view` processor, but the developer can select the new one by using this configuration tag.

When using the traditional `create-view` processor, the additional `temp-view-base-name` attribute can be used to specify the views' base name that the processor uses.

## The `create-view` Processor

The `create-view` Processor is the traditional processor implemented in HotRod and it analyzes SQL queries by creating a temporary views in 
the sandbox database. 

In order to create views this processor requires the connecting user to have `CREATE` privileges in the database schema.

This processor accepts parameters in the `WHERE` clause of the main query or any subquery. However, the location where the developer
can place parameters using this processor is somewhat limited compared to the new `result-set` processor. It also requires all parameters to be enclosed in
`<complement>` tags.

This processor is enabled by default. It can also be explicitly set using the `<select-generation>` tag, as in:

    <select-generation strategy="create-view" />

## The `result-set` Processor

The `result-set` processor analyzes queries by preparing JDBC Result Sets. It does not create views in the database schema, and
the connecting user only needs read-only access to the sandbox database.

Parameters can be placed anywhere in the SQL query and do not need to be enclosed in `<complement>` tags.

To enable this processor you need to explicitly include the `<select-generation>` tag, as in:

    <select-generation strategy="result-set" />

The `<complement>` tag is still needed when Dynamic SQL tags are included in Nitro queries.

## Side-by-side Query Processor Comparison

The following table compares both Query Processors:

| Aspect  | `create-view` *1 | `result-set` |
|----------------|-------|---|
| Database Privileges  | Needs the CREATE VIEW privilege in the sandbox database | Read-only access in the sandbox database |
| Parameters   | Only in WHERE clause, enclosed in a `<complement>` tag | Inline, anywhere allowed by JDBC |
| Database Connections | Needs 2 database connections. Two passes that may interfere with data load when specified in the JDBC URL | Single pass |
| Performance          | Slow on some databases *2 | Fast |
| Drawbacks            | May leave dangling views under error conditions | May not work on very old databases |
| `<complement>` tag | Needed for parameters and for Dynamic SQL | Only for Dynamic SQL |


*1 This is the default, old processor.

*2 It has been observed that Oracle database seems to be particularly slow to retrieve database metadata; opening two connections
makes it even slower.

 
  
      
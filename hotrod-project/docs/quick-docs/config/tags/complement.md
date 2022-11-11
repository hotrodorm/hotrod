# The `<complement>` Tag

This tags can be used in a `<select>` query tag to enclose a section of Dynamic SQL and hide it during the 
query parsing phase.

Its purpose is to hide sections of the parsed query to make it valid according to the SQL dialect in use. HotRod
requires the query to be valid in order to parse it and to retrieve the detailed structure of the returned result set.

See [Nitro Queries](../../nitro/nitro.md) and [Dynamic SQL](../../nitro/nitro-dynamic-sql.md) for more details.




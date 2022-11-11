# The `<complement>` Tag

This tags enclosed a section of Dynamic SQL to hide it during the query parsing phase of a `<select>` query tag.

Its purpose is to hide sections of the parsed query to make it valid according to the SQL dialect in use. HotRod
requires the query to be valid in order to parse it and retrieve the detailed structure of the returned result set.

See [Dynamic SQL](../../nitro/nitro-dynamic-sql.md) for more details.




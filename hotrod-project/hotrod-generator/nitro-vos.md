# Nitro VOs

| Type | Syntax | Description |
| --- | --- | --- |
| 1. Distinct | vo="ProductSearched" type="distinct" | (currently implemented) No sharing; separate Layout+Model per SELECT query |
| 2. Combined | vo="ProductSearched" type="combined" | Combined Layout+Model; multiple SELECT queries share the same VO; a single Layout+Model combines all the properties of all SELECT queries; some queries may use one subset of the properties, other queries may use a different subset of them |
| 3. Shared | vo="ProductSearched" type="shared" | Shared Layout+Model; multiple SELECT queries share the exact same VO; a single Layout+Model must match exactly all SELECT queries that use it in column names and types |
| 4. Map | type="map" | Produces the result into a Map&lt;String, Object>; suitable for dynamic queries where the number or types of the columns are only available at runtime |


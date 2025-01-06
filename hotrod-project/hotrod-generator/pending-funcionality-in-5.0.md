# Hotrod 5.0 Functionality

| Module | Functionality | Type | Status |
|:-- |:-- |:--:| -- |
| CRUD | Optimistic locking now implements the TIMESTAMP and FULL ROW CHECK strategies in addition to the existing VERSION NUMBER | New | Completed |
| LiveSQL | Advanced Column Typing: comprehensive control of the type of a column for CRUD, LiveSQL, and Nitro | New | Completed |
| LiveSQL | Implemented missing COUNT(expression) function | New | Completed |
| DynamicSQL | New independent DynamicSQL module | New | Completed |
| CRUD | SELECT BY PK/EXAMPLE, UPDATE BY PK/EXAMPLE, DELETE BY PK/EXAMPLE implemented | Existing | Completed |
| CRUD | INSERT and INSERT BY EXAMPLE implemented | Existing | Completed |
| CRUD | Primary key retrieval implemented on INSERTs for identities and sequences | Existing | Completed |
| CRUD | SELECT/UPDATE/DELETE BY CRITERIA | Existing | Completed |
| CRUD | Logging implemented with FINE/DEBUG (SQL only) and FINER/TRACE (SQL + parameters) | Existing | Completed |
| CRUD | Bean factory implemented for SELECTs | Existing | Completed |
| CRUD | Converters when reading from the database (also in Nitro and LiveSQL) | Existing | Completed |
| CRUD | Optimistic Locking | Existing | Completed |
| Nitro | Dynamic SQL | Existing | Completed |
| Nitro | General Queries implemented | Existing | Completed |
| Nitro | Flat SELECTs implemented| Existing | Completed |
| Nitro | Entity SELECTs implemented | Existing | Completed |
| LiveSQL | LiveSQL module migrated to plain JDBC | Existing | Completed |
| CRUD | SELECT sequence | Existing | Pending |
| CRUD | Enum Tables | Existing | Pending |
| CRUD | Converters when writing to the database | New | Pending |
| Nitro | Converters when writing to the database | New | Pending |
| LiveSQL | Converters when writing to the database | New | Pending |
| Nitro | Query mode: Single-row | Existing | Pending |
| Nitro | Graph queries | Existing | Pending |
| Nitro | Query mode: Cursor | Existing | Pending |
| CRUD | SELECT BY FK(s) | Removed | Completed |
| CRUD | SELECT by unique indexes| Removed | Completed |
| Maven Arquetype | Maven Arquetype module was removed | Removed | Completed |
| Generator | Log4j fully removed in favor of JUL | Removed | Completed |
| MyBatis | MyBatis fully removed | Removed | Completed |
| CRUD | Packages are changed in the hotrod library. The "runtime" segment is now removed | Existing | Completed |

## Functionality TBD

CRUD:

- New names for DAOs and VOs.
- New configuration structure, tags, and attributes for DAOs and VOs.
- New package structure for DAOs and VOs.

Nitro:

- The jdbc-type is not required for a parameter that is not applied to the query. Maybe separate in &lt;parameter> and &lt;nullable-parameter>.



# Hotrod 5.0 Functionality

| # | Module | Functionality | Scope | Status |
|:--:|:-- |:-- |:--:| -- |
| 1 | CRUD | Migrated to plain JDBC | Existing | Completed |
| 2 | CRUD | SELECT BY PK/EXAMPLE, UPDATE BY PK/EXAMPLE, DELETE BY PK/EXAMPLE implemented | Existing | Completed |
| 3 | CRUD | INSERT and INSERT BY EXAMPLE implemented | Existing | Completed |
| 4 | CRUD | Primary key retrieval implemented on INSERTs for identities and sequences | Existing | Completed |
| 5 | CRUD | SELECT/UPDATE/DELETE BY CRITERIA | Existing | Completed |
| 6 | CRUD | Logging implemented with FINE/DEBUG (SQL only) and FINER/TRACE (SQL + parameters) | Existing | Completed |
| 7 | CRUD | Bean factory implemented for SELECTs | Existing | Completed |
| 8 | CRUD | Converters when reading from the database (also in Nitro and LiveSQL) | Existing | Completed |
| 9 | CRUD | Optimistic Locking | Existing | Completed |
| 10 | CRUD | SELECT sequence | Existing | Pending |
| 11 | CRUD | Enum Tables | Existing | Pending |
| 12 | CRUD | Optimistic locking now implements the TIMESTAMP and FULL ROW CHECK strategies in addition to the existing VERSION NUMBER | New | Completed. Needs review. |
| 13 | CRUD | Converters when writing to the database | New | Pending |
| 14 | CRUD | SELECT BY FK(s) | Removed | Completed |
| 15 | CRUD | SELECT by unique indexes| Removed | Completed |
| 16 | CRUD | Packages are changed in the hotrod library. The "runtime" segment is now removed | Existing | Completed |
| 17 | LiveSQL | LiveSQL migrated to plain JDBC | Existing | Completed |
| 18 | LiveSQL | Advanced Column Typing: comprehensive control of the type of a column for CRUD, LiveSQL, and Nitro | New | Completed |
| 19 | LiveSQL | Implemented missing COUNT(expression) function | New | Completed |
| 20 | LiveSQL | Converters when writing to the database | New | Pending |
| 21 | DynamicSQL | New independent DynamicSQL module | New | Completed |
| 22 | Nitro | Migrated to plain JDBC including Dynamic SQL | Existing | Completed |
| 23 | Nitro | General Queries implemented | Existing | Completed |
| 24 | Nitro | Flat SELECTs implemented| Existing | Completed |
| 25 | Nitro | Entity SELECTs implemented | Existing | Completed |
| 26 | Nitro | Converters when writing to the database | New | Pending |
| 27 | Nitro | Query mode: Single-row | Existing | Pending |
| 28 | Nitro | Graph queries | Existing | Pending |
| 29 | Nitro | Query mode: Cursor | Existing | Pending |
| 30 | Generator | Log4j fully removed in favor of JUL | Removed | Completed |
| 31 | Generator | MyBatis fully removed | Removed | Completed |
| 32 | Maven Arquetype | Maven Arquetype module was removed | Removed | Completed |


## Functionality TBD

CRUD:

- New names for DAOs and VOs.
- New configuration structure, tags, and attributes for DAOs and VOs.
- New package structure for DAOs and VOs.

Nitro:

- The jdbc-type is not required for a parameter that is not applied to the query. Maybe separate in &lt;parameter> and &lt;nullable-parameter>.



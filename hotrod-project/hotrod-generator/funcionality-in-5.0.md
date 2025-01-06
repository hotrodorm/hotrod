# Hotrod 5.0 Functionality

| # | Module | Functionality | Scope | Status |
|:--:|:-- |:-- |:--:|:--:|
| 1 | CRUD | Migrated to plain JDBC | Existing | :heavy_check_mark: |
| 2 | CRUD | SELECT BY PK/EXAMPLE, UPDATE BY PK/EXAMPLE, DELETE BY PK/EXAMPLE implemented | Existing | :heavy_check_mark: |
| 3 | CRUD | INSERT and INSERT BY EXAMPLE implemented | Existing | :heavy_check_mark: |
| 4 | CRUD | Primary key retrieval implemented on INSERTs for identities and sequences | Existing | :heavy_check_mark: |
| 5 | CRUD | SELECT/UPDATE/DELETE BY CRITERIA | Existing | :heavy_check_mark: |
| 6 | CRUD | Logging implemented with FINE/DEBUG (SQL only) and FINER/TRACE (SQL + parameters) | Existing | :heavy_check_mark: |
| 7 | CRUD | Bean factory implemented for SELECTs | Existing | :heavy_check_mark: |
| 8 | CRUD | Converters when reading from the database (also in Nitro and LiveSQL) | Existing | :heavy_check_mark: |
| 9 | CRUD | Optimistic Locking | Existing | :heavy_check_mark: |
| 10 | CRUD | SELECT sequence | Existing | soon |
| 11 | CRUD | Enum Tables | Existing | :question: |
| 12 | CRUD | Optimistic locking now implements the TIMESTAMP and FULL ROW CHECK strategies in addition to the existing VERSION NUMBER | New | :heavy_check_mark:. Needs review. |
| 13 | CRUD | Converters when writing to the database | New | soon |
| 14 | CRUD | SELECT BY FK(s) | Existing | :x: |
| 15 | CRUD | SELECT by unique indexes| Existing | :x: |
| 16 | CRUD | Packages are changed in the hotrod library. The "runtime" segment is now removed | Existing | :heavy_check_mark: |
| 17 | LiveSQL | LiveSQL migrated to plain JDBC | Existing | :heavy_check_mark: |
| 18 | LiveSQL | Advanced Column Typing: comprehensive control of the type of a column for CRUD, LiveSQL, and Nitro | New | :heavy_check_mark: |
| 19 | LiveSQL | Implemented missing COUNT(expression) function | New | :heavy_check_mark: |
| 20 | LiveSQL | Converters when writing to the database | New | soon |
| 21 | DynamicSQL | New independent DynamicSQL module | New | :heavy_check_mark: |
| 22 | Nitro | Migrated to plain JDBC including Dynamic SQL | Existing | :heavy_check_mark: |
| 23 | Nitro | General Queries implemented | Existing | :heavy_check_mark: |
| 24 | Nitro | Flat SELECTs implemented| Existing | :heavy_check_mark: |
| 25 | Nitro | Entity SELECTs implemented | Existing | :heavy_check_mark: |
| 26 | Nitro | Converters when writing to the database | New | soon |
| 27 | Nitro | Query mode: Single-row | Existing | soon |
| 28 | Nitro | Graph queries | Existing | :clock9: |
| 29 | Nitro | Query mode: Cursor | Existing | :question: |
| 30 | Generator | Log4j fully removed in favor of JUL | Existing | :x: |
| 31 | Generator | MyBatis fully removed | Existing | :x: |
| 32 | Maven Arquetype | The Maven Arquetype module was removed | Existing | :x: |


## Functionality TBD

CRUD:

- New names for DAOs and VOs.
- New configuration structure, tags, and attributes for DAOs and VOs.
- New package structure for DAOs and VOs.

Nitro:

- The jdbc-type is not required for a parameter that is not applied to the query. Maybe separate in &lt;parameter> and &lt;nullable-parameter>.



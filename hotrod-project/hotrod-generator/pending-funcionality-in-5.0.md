# Hotrod 5.0 Functionality

## 1. New Functionality

CRUD:

- Optimistic locking now implements the TIMESTAMP and FULL ROW CHECK strategies in addition to the
existing VERSION NUMBER one.

LiveSQL:

- Advanced Column Typing: simple yet comprehensive control of the type of a column for CRUD, LiveSQL, and Nitro.
- Implemented missing COUNT(expression) function.

Dynamic SQL:

- New Dynamic SQL engine:
    - Used by Nitro.
    - Can be used from plain Java at runtime.


## 2. Existing Functionality Migrated To Plain JDBC

CRUD:

- SELECT BY PK/EXAMPLE, UPDATE BY PK/EXAMPLE, DELETE BY PK/EXAMPLE implemented.
- INSERT and INSERT BY EXAMPLE implemented.
- Primary key retrieval implemented on INSERTs for identities and sequences.
- SELECT/UPDATE/DELETE BY CRITERIA.
- Logging implemented with FINE/DEBUG (SQL only) and FINER/TRACE (SQL + parameters).
- Bean factory implemented for SELECTs.
- Converters when reading from the database (also in Nitro and LiveSQL).
- Optimistic Locking.

Nitro:

- Dynamic SQL.
- General Queries implemented.
- Flat SELECTs implemented.
- Entity SELECTs implemented.

LiveSQL:

- Basic POC working.

## 3. Existing Functionality Not Yet Migrated To Plain JDBC

CRUD:

- SELECT sequence.
- Enums.
- Converters when writing to the database (also in Nitro and LiveSQL).

Nitro:

- Query mode: Single-row.
- Graph queries.
- Query mode: Cursor.

LiveSQL:

- Fully test SELECTs.
- UPDATE, DELETE, INSERT.

## 4. Removed Functionality

- Mybatis persistence layer removed in favor of plain JDBC.
- The Arquetype module was removed.
- Log4j fully removed from HotRod. It was still used in the generator and was switched to JUL.
- Removed old unused classes in the hotrod library.
- SELECT BY FK(s) -- this functionality was rarely used. Plus the implementation was clunky.
- SELECT by unique indexes.

## 5. Modified Functionality

- New class writer handles references and imports for cleaners DAOs and VOs.
- Packages are changed in the hotrod library. The "runtime" segment is now removed.

## 6. Functionality TBD

CRUD:

- New names for DAOs and VOs.
- New configuration structure, tags, and attributes for DAOs and VOs.
- New package structure for DAOs and VOs.

Nitro:

- The jdbc-type is not required for a parameter that is not applied to the query. Maybe separate in &lt;parameter> and &lt;nullable-parameter>.



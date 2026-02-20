# INSERT Variations for Generated Keys

This article describes the cases that consider key generation and retrieval for INSERT clauses that include key generation in the database itself.

First, the SQL Standard defines that the INSERT clauses can take two main forms:

- Using a VALUES clause with the values to insert a single row
- Using a SELECT clause to insert the result of a query. This form can insert zero to many rows

Second, when it comes to auto generate primary key values databases implement two main solutions for it:

- Using SEQUENCES. This is how it was done in the early days
- Using IDENTITIES. This is the new solution that links the key generation to each table automatically. It's simpler to use, although less flexible that the original one. Nevertheless, it's probably the right fit for 99% of the cases compared to SEQUENCES

Considering both aspects there are four possible combinations of cases for INSERTs that include key generation. They are depicted below:

| Database   | Sequences &amp; VALUES | Sequences &amp; SELECT | Identities &amp; VALUES | Identities &amp; SELECT |
| :--------- | :---: | :---: | :---: | :---: |
| Oracle     | Yes | No *1 | Yes | No *2    |
| DB2        | Yes | Yes | Yes | Yes |
| PostgreSQL | Yes | Yes | Yes | Yes |
| SQL Server | Yes | Yes *3 | Yes | No *4 |
| MySQL      | --  | --  | Yes | Yes |
| MariaDB    | No *5  | No *5  | Yes | Yes |
| Sybase ASE | --  | --  | Yes | No  |
| H2         | Yes | Yes | Yes | Yes |
| HyperSQL   | No  | No  | Yes | Yes |
| Derby      | Yes | No*6 | Yes | No*7 |


*1 Oracle does not integrate SEQUENCES in INSERT-SELECT correctly. It works for special cases only, but not for the general case. Maybe new research will need to be done for this case.

*2 Oracle does not integrate IDENTITIES in INSERT-SELECT correctly. It shows the error "ORA-00933: SQL command not properly ended", even thought the SQL query is correct and runs in the editor. Maybe new research will need to be done for this case.

*3 SQL Server correctly reports the inserted keys, but does not report the count of inserted rows. Nonetheless, this can be deduced from the size of the list of keys.

*4 SQL Server reports the correct count of inserted rows for INSERT-SELECT with IDENTITIES, but only returns the last key (identity value) that was used.

*5 LiveSQL does not currently support SEQUENCEs (available since version 10.3) since this requires pending research.

*6 Apache Derby does not integrate SEQUENCES in INSERT-SELECT correctly.

*7 Apache Derby reports the correct count of inserted rows for INSERT-SELECT with IDENTITIES, but only returns the last key (identity value) that was used.


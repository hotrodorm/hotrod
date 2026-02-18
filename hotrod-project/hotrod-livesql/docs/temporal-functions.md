# Temporal Types and Functions

Current the generic DateTime type is used for all temporal types in the database. This is useful for the most common cases, but it reaches its limits when it comes to time arithmetics. Therefore, more specialized temporal types could prove useful for these purposes, as described below.

## Phases

Due to their usefulness and support across databases, the following phases have been declared to get the low hanging fruit faster and the diminishing-return efforts later.

- Phase 1:
    - DATE
    - TIME
    - TIMESTAMP
- Phase 2:
    - TIMESTAMP WITH TIME ZONE
- Phase 3:
    - INTERVAL
- Phase 4:
    - TIME WITH TIME ZONE

## DATE

- date TEMPORAL.date(&lt;year>, &lt;month>, &lt;day>)

**Note**: Maybe we should not implement this function. Using it could signal a deficient database model that is storing dates split up into fields rather than as true DATE types; this could lead to SQL injection vulnerabilities, if not handled properly.

| Database | Implementation |
| :-- | :-- |
| Oracle  | case when year + month + day is not null then<br/>to_date(lpad(&lt;year>, 4, 0) \|\| '-' \|\| lpad(&lt;month>, 2, 0) \|\| '-' \|\| lpad(&lt;day>, 2, 0), 'YYYY-MM-DD')<br/>end |
| DB2 LUW | |
| PostgreSQL | make_date(&lt;year>, &lt;month>, &lt;day>) -- All parameters must be int |
| H2 | N/A |

- date TEMPORAL.literalDate(&lt;year>, &lt;month>, &lt;day>)

| Database | Implementation |
| :-- | :-- |
| Oracle  | date '&lt;year>-&lt;month>-&lt;day>' |
| DB2 LUW | date '&lt;year>-&lt;month>-&lt;day>' |
| PostgreSQL | date '&lt;year>-&lt;month>-&lt;day>' |
| H2 | date '&lt;year>-&lt;month>-&lt;day>' |

- date TEMPORAL.currentDate(&lt;year>, &lt;month>, &lt;day>)

| Database | Implementation |
| :-- | :-- |
| Oracle  | trunc(current_date) |
| DB2 LUW | current date |
| PostgreSQL | current_date |
| SQL Server | getdate() |
| MariaDB | curdate() |
| MySQL | curdate() |
| Sybase ASE | current_date() |
| H2 | current_date() |
| HyperSQL | curdate() |
| Derby | current_date |

- date add(&lt;num>)

| Database | Implementation |
| :-- | :-- |
| Oracle  | &lt;date> + &lt;num> |
| DB2 LUW | &lt;date> + &lt;num> days |
| PostgreSQL | &lt;date> + &lt;num> |
| H2 | dateadd(day, &lt;num>, &lt;date>) |

- num diff(&lt;start-date>)

| Database | Implementation |
| :-- | :-- |
| Oracle  | &lt;date> - &lt;start-date> |
| DB2 LUW | &lt;date> - &lt;start-date> |
| PostgreSQL | &lt;date> - &lt;start-date> |
| H2 | datediff(day, &lt;start-date>, &lt;date>) |

- timestamp toTimestamp()

| Database | Implementation |
| :-- | :-- |
| Oracle  | &lt;date> |
| DB2 LUW | timestamp(&lt;date>, '00:00:00') |
| PostgreSQL | &lt;date> + time '00:00:00' |
| H2 | timestampadd(second, 0, &lt;date>) |

- timestamp toTimestamp(&lt;time>)

| Database | Implementation |
| :-- | :-- |
| Oracle  | N/A |
| DB2 LUW | timestamp(&lt;date>, &lt;time>) |
| PostgreSQL | &lt;date> + &lt;time> |
| H2 | &lt;date> + &lt;time> |

- num extract(&lt;field>) -- YEAR, MONTH, DAY, DOW

| Database | Implementation | Available Fields |
| :-- | :-- | :-- |
| Oracle  | extract(&lt;field> from &lt;date>) | YEAR, MONTH, DAY |
| DB2 LUW | extract(&lt;field> from &lt;date>) | YEAR, MONTH, DAY |
| PostgreSQL | extract(&lt;field> from &lt;date>) | YEAR, MONTH, DAY |
| SQL Server | | |
| MariaDB |  | |
| MySQL |  | |
| Sybase ASE |  | |
| H2 | extract(&lt;field> from &lt;date>) | YEAR, MONTH, DAY |
| HyperSQL |  | |
| Derby |  | |

<!--
- timestamptz toTimestamptz(&lt;time>, &lt;offset>)
-->

## TIMESTAMP

- timestamp TEMPORAL.timestamp(&lt;year>, &lt;month>, &lt;day>, &lt;hour>, &lt;minute>, &lt;second>, &lt;milliseconds>)
- timestamp TEMPORAL.literalTimestamp(&lt;year>, &lt;month>, &lt;day>, &lt;hour>, &lt;minute>, &lt;second>, &lt;milliseconds>)
- timestamp TEMPORAL.currentTimestamp()

| Database | Implementation |
| :-- | :-- |
| Oracle  |  |
| DB2 LUW |  |
| PostgreSQL |  |
| SQL Server |  |
| MariaDB |  |
| MySQL |  |
| Sybase ASE |  |
| H2 | localtimestamp |
| HyperSQL |  |
| Derby |  |



- timestamp add(&lt;num>, &lt;unit>)
- num diff(&lt;timestamp>, &lt;unit>)
- timestamp trunc(&lt;unit>)
- date date()
- time time()
- num extract(&lt;field>) -- YEAR, MONTH, DAY, DOW, HOUR, MINUTE, SECOND, MILLISECOND

<!--
- timestamptz at(&lt;offset>)
-->

## TIME

**Note**: Oracle does not implement the TIME data type. Therefore, all functions related to TIME are not implemented for the Oracle database.

- time TEMPORAL.time(&lt;hour>, &lt;minute>, &lt;second>, &lt;milliseconds>)
- time TEMPORAL.literalTime(&lt;hour>, &lt;minute>, &lt;second>, &lt;milliseconds>)
- time TEMPORAL.currentTime()
- timestamp toTimestamp(&lt;date>)
- time add(&lt;num>, &lt;unit>)
- num diff(&lt;time>, &lt;unit>)
- time trunc(&lt;unit>)
- num extract(&lt;field>) -- HOUR, MINUTE, SECOND, MILLISECOND

## TIMESTAMP WITH TIME ZONE

## TIME WITH TIME OFFSET


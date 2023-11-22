# Generic Execution Plan Samples

There's no standard way of obtaining the execution plan of a query across databases. Each one
provides very different techniques to get them, offer different levels of detail, in wildly different
formats.

The following LiveSQL query was run in all databases supported by HotRod:

```java
InvoiceTable i = InvoiceDAO.newTable("i");
BranchTable b = BranchDAO.newTable("b");

sql.select()
   .from(i)
   .join(b, b.id.eq(i.branchId))
   .where(b.region.eq("SOUTH").and(i.status.ne("UNPAID").and(i.amount.ge(228))))
   .orderBy(i.orderDate.desc())
   .execute();
```

Torcs retrieved the following execution plans on each one:

## Oracle Execution Plan

Notice the tabular default format for the plan. Other formats are available, but this one is simple enough,
and also useful.

```txt
Plan hash value: 3305857414
 
----------------------------------------------------------------------------------------------
| Id  | Operation                     | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT              |              |     1 |   118 |     5  (20)| 00:00:01 |
|   1 |  SORT ORDER BY                |              |     1 |   118 |     5  (20)| 00:00:01 |
|   2 |   NESTED LOOPS                |              |     1 |   118 |     4   (0)| 00:00:01 |
|   3 |    NESTED LOOPS               |              |     1 |   118 |     4   (0)| 00:00:01 |
|*  4 |     TABLE ACCESS FULL         | INVOICE      |     1 |    84 |     3   (0)| 00:00:01 |
|*  5 |     INDEX UNIQUE SCAN         | SYS_C0016733 |     1 |       |     0   (0)| 00:00:01 |
|*  6 |    TABLE ACCESS BY INDEX ROWID| BRANCH       |     1 |    34 |     1   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   4 - filter("I"."STATUS"<>:2 AND "I"."AMOUNT">=:3)
   5 - access("B"."ID"="I"."BRANCH_ID")
   6 - filter("B"."REGION"=:1)
 
Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
```

## DB2 Execution Plan

DB2 does not offer any generic format for the execution plan. It saves the plan details into a database structure across  
several tables. 

This plan was retrieved using "db2-custom-2b" format, using a ad-hoc query to retrieve it:

```txt
RETURN
  +-144 << TBSCAN (13 rows, 37 io) 
    +-144 SORT (13 rows, 37 io) 
      +-144 HSJOIN (13 rows, 37 io) 
        +-75 << FETCH *5 (320 rows, 19 io) INVOICE
        | +-27 IXSCAN (1000 rows, 4 io) SQL231114120832880 on INVOICE (+ID)
        +-68 << FETCH *7 (40 rows, 18 io) BRANCH
          +-27 IXSCAN (1000 rows, 4 io) SQL231114120832360 on BRANCH (+ID)
 
Predicates:
*5 SARG (? <= Q1.AMOUNT)
*5 SARG (Q1.STATUS <> ?)
*7 SARG (Q2.REGION = ?)
 
Legend:
<< Reads from the heap.
$ Operation is at least 20% more expensive than its combined children.
```

## PostgreSQL Execution Plan

PostgreSQL produces the execution plan in TEXT format, as well as JSON, YAML, and XML. The TEXT format is shown below:

```txt
Sort  (cost=36.80..36.80 rows=1 width=221)
  Sort Key: i.order_date DESC
  ->  Hash Join  (cost=16.79..36.79 rows=1 width=221)
        Hash Cond: (i.branch_id = b.id)
        ->  Seq Scan on invoice i  (cost=0.00..19.45 rows=209 width=100)
              Filter: (((status)::text <> 'UNPAID'::text) AND (amount >= 228))
        ->  Hash  (cost=16.75..16.75 rows=3 width=121)
              ->  Seq Scan on branch b  (cost=0.00..16.75 rows=3 width=121)
                    Filter: ((region)::text = 'SOUTH'::text)
```

## SQL Server Execution Plan

SQL Server produces the plan in TEXT format as well as XML. The TEXT format is shown below:

```txt
  |--Nested Loops(Inner Join, WHERE:([master].[dbo].[invoice].[branch_id] as [i].[branch_id]=
         [master].[dbo].[branch].[id] as [b].[id]))
       |--Sort(ORDER BY:([i].[order_date] DESC))
       |    |--Clustered Index Scan(OBJECT:([master].[dbo].[invoice].[PK__invoice__3213E83FC85FD236] AS [i]), 
                 WHERE:([master].[dbo].[invoice].[amount] as [i].[amount]>=(228) AND
                 [master].[dbo].[invoice].[status] as [i].[status]<>'UNPAID'))
       |--Clustered Index Scan(OBJECT:([master].[dbo].[branch].[PK__branch__3213E83FF5EAF6FD] AS [b]), 
            WHERE:([master].[dbo].[branch].[region] as [b].[region]='SOUTH'))
```

## MySQL Execution Plan

MySQL provides the plan in tabular format, JSON, and TREE format. The tabular format is shown below:

```txt
id select_type table partit type   possible_keys key     key_len ref         rows filtered Extra
-- ----------- ----- ------ ------ ------------- ------- ------- ----------- ---- -------- ---------------------
 1 SIMPLE      i     <null> ALL    <null>        <null>  <null>  <null>         3    33.33 Using where; filesort
 1 SIMPLE      b     <null> eq_ref PRIMARY       PRIMARY 4       i.branch_id    1       50 Using where
```

## MariaDB Execution Plan

MySQL provides the plan in tabular format and JSON format. The tabular format is shown below:

```txt
id select_type table type possible_keys key  key_len ref  rows Extra                                          
-- ----------- ----- ---- ------------- ---- ------- ---- ---- -----------------------------------------------
 1 SIMPLE      b     ALL  PRIMARY       null null    null    2 Using where                                    
 1 SIMPLE      i     ALL  null          null null    null    3 Using where; Using join buffer (flat, BNL join)
```

## Sybase ASE Execution plan

Sybase ASE provides the plan in prose-like format with a tree indentation:

```txt
QUERY PLAN FOR STATEMENT 1 (at line 1).
Optimized using Serial Mode

STEP 1
The type of query is SELECT.
 
4 operator(s) under root
 
 |ROOT:EMIT Operator (VA = 4)
 |
 |   |SORT  Operator (VA = 3)
 |   | Using Worktable1 for internal storage.
 |   |
 |   |   |NESTED LOOP JOIN Operator (VA = 2) (Join Type: Inner Join)
 |   |   |
 |   |   |   |SCAN Operator (VA = 0)
 |   |   |   |  FROM TABLE
 |   |   |   |  master.dbo.invoice
 |   |   |   |  i
 |   |   |   |  Table Scan.
 |   |   |   |  Forward Scan.
 |   |   |   |  Positioning at start of table.
 |   |   |   |  Using I/O Size 2 Kbytes for data pages.
 |   |   |   |  With LRU Buffer Replacement Strategy for data pages.
 |   |   |
 |   |   |   |SCAN Operator (VA = 1)
 |   |   |   |  FROM TABLE
 |   |   |   |  master.dbo.branch
 |   |   |   |  b
 |   |   |   |  Using Clustered Index.
 |   |   |   |  Index : branch_id_11381000641
 |   |   |   |  Forward Scan.
 |   |   |   |  Positioning by key.
 |   |   |   |  Keys are:
 |   |   |   |    id ASC
 |   |   |   |  Using I/O Size 2 Kbytes for data pages.
 |   |   |   |  With LRU Buffer Replacement Strategy for data pages.
```

## H2 Execution Plan

H2's execution plan just decorates the parsed query with the operators used to access and filter the tables, as shown below:

```txt
SELECT
    "I"."ID" AS "id",
    "I"."ACCOUNT_ID" AS "accountId",
    "I"."AMOUNT" AS "amount",
    "I"."BRANCH_ID" AS "branchId",
    "I"."ORDER_DATE" AS "orderDate",
    "I"."TYPE" AS "type",
    "I"."UNPAID_BALANCE" AS "unpaidBalance",
    "I"."STATUS" AS "status",
    "B"."ID" AS "id",
    "B"."REGION" AS "region",
    "B"."IS_VIP" AS "isVip"
FROM "PUBLIC"."INVOICE" "I"
    /* PUBLIC.INVOICE.tableScan */
    /* WHERE (I.AMOUNT >= ?3)
        AND (I.STATUS <> ?2)
    */
INNER JOIN "PUBLIC"."BRANCH" "B"
    /* PUBLIC.BRANCH.tableScan */
    ON 1=1
WHERE ("B"."ID" = "I"."BRANCH_ID")
    AND ("I"."AMOUNT" >= ?3)
    AND ("B"."REGION" = ?1)
    AND ("I"."STATUS" <> ?2)
ORDER BY 5 DESC
```

## HyperSQL Execution Plan

HyperSQL's execution plan include limited information regarding the access and filtering of data:

```txt
isDistinctSelect=[false]
isGrouped=[false]
isAggregated=[false]
columns=[  COLUMN: PUBLIC.INVOICE.ID AS ID nullable
  COLUMN: PUBLIC.INVOICE.AMOUNT AS AMOUNT nullable
  COLUMN: PUBLIC.INVOICE.BRANCH_ID AS BRANCHID nullable
  COLUMN: PUBLIC.INVOICE.STATUS AS STATUS nullable
  COLUMN: PUBLIC.INVOICE.ORDER_DATE AS ORDERDATE nullable
  COLUMN: PUBLIC.INVOICE.UNPAID_BALANCE AS UNPAIDBALANCE nullable
  COLUMN: PUBLIC.INVOICE.ACCOUNT_ID AS ACCOUNTID nullable
  COLUMN: PUBLIC.INVOICE.TYPE AS TYPE nullable
  COLUMN: PUBLIC.BRANCH.ID AS ID nullable
  COLUMN: PUBLIC.BRANCH.REGION AS REGION nullable
  COLUMN: PUBLIC.BRANCH.IS_VIP AS ISVIP nullable

]
[range variable 1
  join type=INNER
  table=INVOICE
  alias=I
  cardinality=0
  access=FULL SCAN
  join condition = [index=SYS_IDX_10513
    other condition=[
    AND arg_left=[
     AND arg_left=[
      AND arg_left=[
       NOT_EQUAL arg_left=[        COLUMN: PUBLIC.INVOICE.STATUS
] arg_right=[
        VALUE = 'UNPAID', TYPE = CHARACTER]] arg_right=[
       GREATER_EQUAL arg_left=[        COLUMN: PUBLIC.INVOICE.AMOUNT
] arg_right=[
        VALUE = 228, TYPE = INTEGER]]] arg_right=[
      NOT_EQUAL arg_left=[       COLUMN: PUBLIC.INVOICE.STATUS
] arg_right=[
       VALUE = 'UNPAID', TYPE = CHARACTER]]] arg_right=[
     GREATER_EQUAL arg_left=[      COLUMN: PUBLIC.INVOICE.AMOUNT
] arg_right=[
      VALUE = 228, TYPE = INTEGER]]]
  ]
  ][range variable 2
  join type=INNER
  table=BRANCH
  alias=B
  cardinality=0
  access=FULL SCAN
  join condition = [index=SYS_IDX_10512
    other condition=[
    AND arg_left=[
     AND arg_left=[
      AND arg_left=[
       EQUAL arg_left=[        COLUMN: PUBLIC.BRANCH.ID
] arg_right=[        COLUMN: PUBLIC.INVOICE.BRANCH_ID
]] arg_right=[
       EQUAL arg_left=[        COLUMN: PUBLIC.BRANCH.REGION
] arg_right=[
        VALUE = 'SOUTH', TYPE = CHARACTER]]] arg_right=[
      EQUAL arg_left=[       COLUMN: PUBLIC.BRANCH.ID
] arg_right=[       COLUMN: PUBLIC.INVOICE.BRANCH_ID
]]] arg_right=[
     EQUAL arg_left=[      COLUMN: PUBLIC.BRANCH.REGION
] arg_right=[
      VALUE = 'SOUTH', TYPE = CHARACTER]]]
  ]
  ]]
order by=[
COLUMN: PUBLIC.INVOICE.ORDER_DATE
DESC
]
PARAMETERS=[]
SUBQUERIES[]
```

## Apache Derby

Apache Derby does not produce execution plans, but can provide statistics of recently executed queries. For a simple query, like:

```sql
select * from countries;
```

The statistics take the form:

```txt
Statement Name:
        null
Statement Text:
        select * from countries
Parse Time: 20
Bind Time: 10
Optimize Time: 50
Generate Time: 20
Compile Time: 100
Execute Time: 10
Begin Compilation Timestamp : 2005-05-25 09:16:21.24
End Compilation Timestamp : 2005-05-25 09:16:21.34
Begin Execution Timestamp : 2005-05-25 09:16:21.35
End Execution Timestamp : 2005-05-25 09:16:21.4
Statement Execution Plan Text:
Table Scan ResultSet for COUNTRIES at read committed isolation
level using instntaneous share row 
locking chosen by the optimizer
Number of opens = 1
Rows seen = 114
Rows filtered = 0
Fetch Size = 16
        constructor time (milliseconds) = 0
        open time (milliseconds) = 0
        next time (milliseconds) = 10
        close time (milliseconds) = 0
        next time in milliseconds/row = 0

scan information:
        Bit set of columns fetched=All
        Number of columns fetched=3
        Number of pages visited=3
        Number of rows qualified=114
        Number of rows visited=114
        Scan type=heap
        start position:
null    stop position:
null    qualifiers:
None
        optimizer estimated row count:          119.00
        optimizer estimated cost:           69.35
```


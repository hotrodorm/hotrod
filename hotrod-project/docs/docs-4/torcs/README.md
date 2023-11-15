# The Torcs Module

Torcs records queries run by the application instance and consolidates statistical data
to detect slow running queries and their execution plans.

Even though it's a part of HotRod, Torcs can be used separatedly without HotRod ORM, in any Spring or SpringBoot application.

Torcs does not aim to replace the official database statistical information. This information is much more comprehensive that what Torcs can provide. Nevertheless, Torcs can easily provide a wealth of information to a savvy developer (e.g using the application logs), who can start detecting and improving queries, without the assistance of an expensive or elusive DBA.

Torcs is local to the application instance and only sees queries ran by the application instance. It does not see the queries ran by other instances or queries executed by other applications that may slow down the remote database for other unrelated causes.

## Enabling Torcs

The module is enabled by adding the dependency in Maven, as in:

```xml
  <dependency>
    <groupId>org.hotrodorm.hotrod</groupId>
    <artifactId>hotrod-torcs</artifactId>
    <version>${hotrod.version}</version>
  </dependency>
```

This module comes with auto-start configuration and there's no need to do anything to start it up apart
from the dependency declaration above. To use Torcs, any Spring bean can declare an autowired property such as:

```java
  @Autowired
  private Torcs torcs;
```

This is the starting point to manage the module, its observers, and to retrieve statistical information and execution plans.

## Example

To get the Top 10 slowest queries, the application can do as simple as:

```java
  for (RankingEntry e : this.torcs.getDefaultRanking().getRanking()) {
    System.out.println(e);
  }
```

Assuming only two queries were executed (but adding up to 16 total executions together) the ranking could display something like:

```txt
4 executions, 0 errors, avg 47 ms, σ 3 [38-55 ms], TET 188 ms, executed: Mon Nov 13 19:44:08 EST 2023 - Mon
  Nov 13 19:48:10 EST 2023, last exception: N/A -- [ds0] SELECT name FROM invoice WHERE client_id = ? ORDER BY
  created_at
12 executions, 0 errors, avg 6 ms, σ 2 [4-9 ms], TET 87 ms, executed: Mon Nov 13 19:44:08 EST 2023 - Mon Nov 13
  19:47:35 EST 2023, last exception: N/A -- [ds0] SELECT name FROM client WHERE id = ?
```


## Multi Data Source Aware

Queries are recorded including the data source were they were executed in. The data source information can be useful if later on an execution plan is of interest for the application.

Two identical queries ran into different datasources (same or different engine type) are always
considered separate and occupy separate entries in the rankings. This can prove useful if one database endpoint is slow for some reason but not the other one(s). Torcs will clearly identify it from the others.

In the example above the section `[ds0]` indicates that both queries were executed in the same data source: the data source #0.

## Query Execution Observers

Torcs uses observers to handle query execution statistics. By default there's only one observer included and active when the application start: the `HighestResponseTimeRanking`. This observer ranks the top 10 slowest queries in the application instance, and provide the details of them.

The application can register more observers for any purpose, and can also enable or disable them
programatically, including the default observer.

Torcs comes with three built-in observers:

| Observer | Default | Description |
| -- | -- | -- |
| HighestResponseTimeRanking | Registered &amp; active | Records the top 10 slowest queries in the application instance |
| InitialQueriesRanking | Not registered &amp; inactive | Records the first 10 queries run in the application instance and discard the next ones |
| LatestQueriesRanking | Not Registered &amp; inactive | Records the last 10 queries in the application instance discarding earlier ones |

The application can register more observers for any other need. An observer must implement the `org.hotrod.torcs.QueryExecutionObserver` interface.

## Restarting Observers

Torcs restarts the observers periodically to record fresh data. By default it restarts all observers every 60 minutes. This can be changed using:

```java
  this.torcs.setRestartPeriodInMinutes(60 * 24); // Restart observers once a day
```

## Retrieving Statistical Query Data

The built-in rankings can provide crucial information on which queries are slowing down an application. Again, the application is not limited to the following observers and can register any custom observers as needed.

### The HighestResponseTimeRanking Observer

As the name implies this ranking records a limited number of queries that took the longest time to execute.

By default this ranking records the 10 slowest queries. To change the number of recorded queries (the size of it) the application can use the `Ranking.setSize(int)` method. For example, to increase the size to 60 entries:

```java
  this.torcs.getDefaultRanking().setSize(60);
```

Changing the size of a ranking restarts it automatically.

Also, consider that multiple query executions of the same query still use a single entry in this ranking. This means that parameterized queries use a single entry in the ranking even if they are execution thousands of times with different parameters.

The method `List<RankingEntry> getRanking()` provides the list of ranked queries in order, starting with the slowest ones.

Again, each ranking entry correspond to a summary of all recorded execution of a query, not to a single execution of it. The entry provides the following data:

| Property | Description |
| -- | -- |
| DataSourceReference getDataSourceReference() | The datasource reference and ID |
| String getSQL() | The SQL query |
| String getCompactSQL() | A compacted form of the SQL query, in a single line |
| long getMinTime() | The minimum execution time for this query |
| long getMaxTime() | The maximum execution time for this query |
| long getExecutions() | The number of executions |
| long getErrors() | The number of failed executions |
| long getFirstExecutionAt() | The timestamp of the first recorded execution |
| long getLastExecutionAt() |  The timestamp of the last recorded execution |
| Throwable getLastException() | The last recorded exception, if any |
| long getLastExceptionTimestamp() | The last recorded exception timestamp, if any |
| long getAverageTime() | The average execution time for the successful executions |
| long getTotalElapsedTime() | The total elapsed time. This is the sum of all elapsed times of all recorded execution. The TET value can be considered the *load* a query put in the database, and can be important to determine which queries deserve to be optimized
| toString() | Provides a summary of the ranking entry |
| double getTimeStandardDeviation() | The standard deviation of the execution time |
| QueryExecution getSlowestExecution() | The slowest recorded execution |
| QueryExecution getFastestExecution() | The fastest recorded execution |
| QueryExecution getFirstExecution() | The first recorded execution |
| QueryExecution getLastExecution() | The last recorded execution |

### The InitialQueriesRanking Observer

This ranking records the first queries in the application and then ignored the rest of them. Repeated executions of recorded query are consolidated into the stats.

The size of the ranking can be set when instantiating it, or later on, using the
`Ranking.setSize(int)` method.

This ranking provides the entries in multiple orderings, as shown below:

| Method | Description |
| -- | -- |
| getRankingByHighestResponseTime() | Provides ranking entries sorted by maximum response time, from highest to lowest |
| getRankingByHighestAvgResponseTime() | Provides ranking entries sorted by average response time, from highest to lowest |
| getRankingByMostExecuted() | Provides ranking entries sorted by number of executions, from highest to lowest |
| getRankingByTotalElapsedTime() | Provides ranking entries sorted by TET (total elapsed time, from highest to lowest |
| getRankingByMostRecentlyExecuted() | Provides ranking entries sorted by the last execution timestamp, from latest to earliest |
| getRankingByMostErrors() | Provides ranking entries sorted by number of errors, from highest to lowest |
| getRankingErrorsByMostRecent() | Provides only the ranking entries with errors, sorted by their last timestamp, from latest to earliest |

Each ranking entry includes all the information described in the previous section.

### The LatestQueriesRanking Observer

This ranking records the latest queries ran by the application and ignores the earliest ones.

The size of the ranking can be set when instantiating it, or later on, using the
`Ranking.setSize(int)` method.

This ranking provides the entries in multiple orderings, using the same methods as the previous ranking.

Each ranking entry includes all the information described in the previous section.

### Retrieving Generic Execution Plans

Torcs can retrieve the generic execution plan of a ranking entry. Specifically, of one of the recorded executions the ranking entry keeps.

Each ranking entry keeps four query executions:

- Slowest execution (maximum response time).
- Fastest execution (minimum response time).
- First execution.
- Last execution.

For example, to retrieve the estimated execution plan for the slowest execution of a ranking entry the application can do:

```java
  RankingEntry re = ...
  System.out.println("Execution Plan:\n"
    + this.torcs.getEstimatedExecutionPlan(re.getSlowestExecution()));
```

Since Torcs is data source-aware, it automatically retrieves execution plan, tailored to the specific ranking entry's database.

The Java code above can display something like (e.g. Oracle):

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

For the same query in PostgreSQL it could display:

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

The generic format of the execution plan depends on each database. High end databases tend to produce more useful information than simpler databases. See [Examples of Generic Execution Plans](./generic-execution-plan-samples.md).

### Saving a Ranking in XLSX Format

Torcs can use Apache POI to save a ranking in XLSX format. To do this add the Apache POI dependencies to the application:

```xml
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml-full</artifactId>
    <version>5.2.0</version>
  </dependency>

  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.0</version>
  </dependency>
```

Then the application can use the `Ranking.saveAsXLSX(os)` method to save any ranking in XLSX format. For example:

```java
  try (OutputStream os = new FileOutputStream(new File("ranking1.xlsx"))) {
    torcs.getDefaultRanking().saveAsXLSX(os);
  }
```

## Configuring Torcs

Torcs can be configured programatically to activate and deactivate the whole Torcs module, a subset of the observers, to register more observers, and to set configuration properties to them.

### Disabling and Enablig Torcs

The application can disable or enable Torcs at any time by doing:

```java
  this.torcs.deactivate();
  this.torcs.activate();
```

### Access the Manage The Default Observer

The application can access the default observer with `this.torcs.getDefaultRanking()`. It can them deactivate it or activate it using:

```java
  this.torcs.getDefaultRanking().deactivate();
  this.torcs.getDefaultRanking().activate();
```

Other observers follow the same strategy for activation/deactivation.

### Registering Another Built-In Observer

The application can also register other built-in observers. For example, to register the `LatestQueriesRanking` the application can do:

```java
  LatestQueriesRanking lqrObserver = new LatestQueriesRanking(50);
  this.torcs.register(lqrObserver);
```

Observers start activated. To activate/deactivate them the application can do so in the same way as above:

```java
  lqrObserver.deactivate();
  lqrObserver.activate();
```

### Registering A Custom Observer

The application can register any class that implements the `org.hotrod.torcs.QueryExecutionObserver` interface. For example, if the application needs to log all query executions it can do so by adding the following observer:

```java
  this.torcs.register(new QueryExecutionObserver() {

    @Override
    public String getTitle() {
      return "Console Query Logger";
    }

    @Override
    public void apply(QueryExecution execution) {
      System.out.println(execution.getResponseTime() + " ms" + " (exception: "
        + execution.getException() + ")" + ": " + QueryExecution.compactSQL(execution.getSQL()));
    }

    @Override
    public void restart() {
      // Nothing to do
    }
  });
```

The `QueryExecution` parameter includes the following information:

| Property | Description |
| -- | -- |
| getSQL() | The SQL query that was executed |
| getResponseTime() | The response time of the query in milliseconds |
| getException() | The exception thrown, if any |

The static method `QueryExecution.compactSQL(sql)` can be used to trim and compress a multi-line query into a single line.






























# The Torcs CTP Module

Torcs CTP produces execution plans in *Check The Plan* format, to be visualized in the https://checktheplan.com web site.

It's available for Oracle, DB2 LUW, PostgreSQL, and SQL Server databases. Behind the scenes, it uses Torcs to find slow queries and their samples. With that Torcs CTP can generate plans using the databases internal information.

Torcs CTP is available since HotRod version 4.4.

Even though it's a part of HotRod, Torcs CTP only requires Torcs and can be used separatedly without the entire HotRod ORM, in any Spring or SpringBoot application.

Torcs CTP is not to be confused with the Torcs module. The latter is the engine that finds slow queries and generates generic execution plans, while Torcs CTP generated enhanced plans. It's shipped separately from Torcs, since many applications may want to use the plain Torcs module and not Torcs CTP.


## Limitations

Torcs CTP only produces plans for four databases. Namely: Oracle, DB2 LUW, PostreSQL, and SQL Server.

The strategy to generate plans depends on the database account having privileges to SELECT from the information schema tables. Some highly secured database setups could prevent this access.


## Enabling Torcs CTP

The module is enabled by adding the dependency to Maven's pom.xml, as in:

```xml
  <dependency>
    <groupId>org.hotrodorm.hotrod</groupId>
    <artifactId>hotrod-torcs-ctp</artifactId>
    <version>${hotrod.version}</version>
  </dependency>
```

Torcs CTP automatically includes Torcs behind the scenes, so there's no need to declare the Torcs dependency. Torcs in its own, also comes with auto-start configuration and there's no need to do anything to start it up apart
from the dependency declaration above. To use Torcs CTP and Torcs, any Spring bean can declare an autowired properties, such as:

```java
  @Autowired
  private Torcs torcs;

  @Autowired
  private TorcsCTP torcsCTP;
```

These are the starting points to identify slow queries and to generate plans for Check The Plan.


## Example

To visualize the execution plan for the slowest query &mdash; the first one in the Top 10 rank of slowest queries &mdash; the application can do:

```java
  RankingEntry re = this.torcs.getDefaultRanking().getEntries().get(0);
  System.out.println("CTP Plan:");
  List<String> plan = this.torcsCTP.getEstimatedCTPExecutionPlan(re.getSlowestExecution());
  plan.stream().forEach(l -> System.out.println(l));
```

The plan is generated as a list of line strings. For convenience the string has been compressed into the web safe Base-64 format, to look like:

```txt
{1/9:7VTNbzUoj4uTSVd2d2bfeWn00bkbRWYlokRC3XdksgjoPjqEJVn4Gn4EW4ceGAOMCdE2/Ajdm106ZphHgAFDmJv/nmm8/jmb2o1U+zPA2L+k6t}
{2/9:HhWzRVt2r1PHm3SOZFkuvo85EaGrQYpwSG6Uyj3OLQYKzBscZwB/iOJTSH8vL3RXimKWHisMhKImB2hG5incQOak42S/KwyPI5kV7eu7i6D8Yx}
{3/9:IWyFE/+XDfmMiKcBJE2Vw7h220LMd2uLRtxlypGoIo42wZt5RF99FssQTYNlecoNNxPi+CPDtfBvi2EJYUju0yGwCxVMqT4B/qEWu9JEEbqkZZ}
{4/9:mt7knemhVrRW9EbkvMwjyZFmXvpovJhKAwP9OSF/Wg1e/01aHq6x4iIgfOLVsCA4srEHSVvxY0CQfCkS4GHBzd62BPNT16/TobJCC3qEdM2lLY}
{5/9:YOJevMSK6kPyAqGQEW3UEl5w2V5/cGqkqgurIjXdmmj2uDFBJkVyqJlfpIDdRhJW/99cNKvj/wtWOTwAnGqq4OVDpSUg/AltqllEgUV/sFBxwp}
{6/9:wK1fX0kn5r1G7+H8lNIzkv8iRMr3u3OqLsOh5li2nppWpKNlmk02tYZ+ZJPI5CqmKSGxoLKt40pONIvwneEjR99DyIAJZ+I/GqA5MTZydvkqgI}
{7/9:5tHrJXOXbqqxSta/bFRoFKcOwKe1V03Xa85ttgG43Ha85vreug+cJr7qqRKc+wje3l3nSV394zsMIuXeawDYYHR/2D9rPycdBBRm5pq7iDtHm8}
{8/9:Kzh9ujIFFHbMPGMuop1alijBpTon6ztVSnNimBph/6FLiqVfJavdFB/7A8ZEAIXVaX0ERKYtRhl57FFShKM/7e8MD3++YcIJALUSkd9ka91gb8}
{9/9:aNj0vry1/n0duPT46ffHr27cfnB96Xhz/v33kU/25/P+59ePr47tc/}
```

Go to https://checktheplan.com, copy these lines in it, and click on "Check". The plan is displayed in tree format.


## Generic Visualization

Nonwithstanding the source database of the plan, they are all visualized in the same way. In the end, all plans are trees of operators with leaf nodes that represent data access into the tablespaces of the database.

Details on the different available views, operator properties, and highlights are available in the Help of the web s


## Log Resistant Format

The execution plans produced by Torcs CTP take the form of a list of lines. This is suitable to save it in log files or other kind of media for later retrieval. The size of the line can be configured to suit the specific log tool.

The plan lines include:

- Line number
- Total lines
- Payload (plan)

Considering some log tools can paginate log entries and can also display them in reverse order, the log resistant format is prepared to:

- Sort rows in the right order, even if they are placed unordered.
- Remove extra prefixes and suffixes text, poduced by the log tools. For example removing timestamps, log level, source code references, and anything in general outside the `{line/total:payload}` format.
- Deduplicate the same line (by line number) if they are copied multiple times by the user. This will work as long as the lines include the exact same payload.
- Separate lines that may have been joined together in the same line, maybe by faulty end-of-line conversion tools or clipboard copies.
- Check there are no missing lines.
- Check the consistency of the entire payload. Using a SHA256 hash, the format is able to detect unintentiional changes or errors (such as placing a line from another plan in this one).


For example if the log tool produces the following log lines (notice the reverse ordering):

```txt
2023-11-24 18:14:05.022 INFO o.p.e.Log(22) {6/6:wK1fXt7bqq2PJN80kn5r1G7+H8lNIzkv8iRMr3u3OqLsO} (saved)
2023-11-24 18:14:05.017 INFO o.p.e.Log(22) {5/6:YOJev7nf65gwA5CMSK6kPyAqGQEW3UEl5w2V5/cGqkqgu} (saved)
2023-11-24 18:14:05.015 INFO o.p.e.Log(22) {4/6:mt7knSxOTxPTiGVemhVrRW9EbkvMwjyZFmXvpovJhKAwP} (saved)
2023-11-24 18:14:05.014 INFO o.p.e.Log(22) {3/6:IWyFERTvZ4mWGir/+XDfmMiKcBJE2Vw7h220LMd2uLRtx} (saved)
2023-11-24 18:14:05.009 INFO o.p.e.Log(22) {2/6:HhWzRnzCG5PFeYPVt2r1PHm3SOZFkuvo85EaGrQYpwSG6} (saved)
2023-11-24 18:14:05.007 INFO o.p.e.Log(22) {1/6:7VTNbtNAEL4hxJUzUoj4uTSVd2d2bfeWn00bkbRWYlokR} (saved)
```

This is still a valid format, since the prefixes and suffixes will be automatically removed and the lines will be sorted and deduplicated (if needed).


## Torcs CTP Configuration

There's only one configuration parameter Torcs CTP accepts: the payload size in characters.

To fit different log tools (Log4j, Java loggers, etc.) and logging infrastructure (Splunk, SolarWinds, AppDynamics, AWS, Grafana, etc.) the payload size can be configured between 40 and 100,000 characters. By default, the payload size per line is 120 characters.

For example, to change the payload size to 80 characters, use the `setSegmentSeize(int)` method:

```java
  this.torcsCTP.setSegmentSize(80);
```




























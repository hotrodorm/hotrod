package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Random;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.runtime.livesql.queries.select.ExecutableCriteriaSelect;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.QueryExecutionObserver;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.hotrod.torcs.rankings.InitialQueriesRanking;
import org.hotrod.torcs.rankings.LatestQueriesRanking;
import org.hotrod.torcs.rankings.RankingEntry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@PropertySource(value = { "file:application.properties",
    "classpath:application.properties" }, ignoreResourceNotFound = true)
public class App {

  @Autowired
  private LiveSQL sql;

  @Autowired
  private Torcs torcs;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
//      crud();
//      join();
//      join();
      livesql();
//      selectByCriteria();
//      torcs();
//      star();
//      noFrom();
      System.out.println("[ Example complete ]");
    };
  }

  private void livesql() throws SQLException, UnsupportedTorcsDatabaseException {
//    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
//      System.out.println("r=" + r);
//    }
//    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
//      System.out.println("r=" + r);
//    }

//    NumbersTable n = NumbersDAO.newTable("n");
//    List<Row> rows = sql.select(n.id) //
//        .from(n) //
//        .where(n.id.eq(1).and(sql.enclose(n.int1.lt(4).or(n.dec1.ne(2))).and(n.dec2.ge(3))))//
//        .execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }

    // Subqueries
//    joinedTableExpressions();
//    nestedTableExpressions();
//    ctesWithoutNamedColumns();
//    ctesWithNamedColumns();
//    lateralJoins();
//    recursiveCTEs();

    liveSQLExamples();

  }
//
//  private void joinedTableExpressions() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    // Two separate table expressions
//    Subquery x = sql.subquery("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    Subquery y = sql.subquery("y", sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//
//    ExecutableSelect<Row> q = sql.select(x.num("id"), x.num("subtotal").mult(2).as("total"), y.str("title")).from(x)
//        .leftJoin(y, y.num("id").eq(x.num("branchId")));
//    System.out.println("1. Table Expressions: " + q.getPreview());
//    q.execute().forEach(r -> System.out.println("r=" + r));
//  }
//
//  private void nestedTableExpressions() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Subquery x = sql.subquery("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    Subquery y = sql.subquery("y", sql.select(x.star(), x.num("id").plus(x.num("amount")).as("subtotal2")).from(x));
////  ExecutableSelect<Row> q1 = sql.select().from(x);
////    ExecutableSelect<Row> q1 = sql.select(x.num("id"), x.num("subtotal").mult(2).as("total")).from(x);
//    ExecutableSelect<Row> q1 = sql.select(y.star(), sql.val(123).as("sample")).from(y);
//    System.out.println("1.b: " + q1.getPreview());
//    q1.execute().forEach(r -> System.out.println("r=" + r));
//  }
//
//  private void ctesWithoutNamedColumns() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    // 2. CTEs without named columns
//
//    CTE a = sql.cte("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    CTE f = sql.cte("y", sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//    ExecutableSelect<Row> q2 = sql.with(a, f).select().from(a).crossJoin(f);
//    System.out.println("2. CTEs without named columns: " + q2.getPreview());
//    q2.execute().forEach(r -> System.out.println("r=" + r));
//
//  }
//
//  private void ctesWithNamedColumns() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    CTE h = sql.cte("h").columnNames("abc", "def", "ghi", "jkl")
//        .as(sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    CTE k = sql.cte("k").as(sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//    ExecutableSelect<Row> q3 = sql.with(h, k).select().from(h).crossJoin(k);
//    System.out.println("3. CTEs with names columns: " + q3.getPreview());
//    q3.execute().forEach(r -> System.out.println("r=" + r));
//
//  }
//
//  private void lateralJoins() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Subquery l = sql.subquery("l", //
//        sql.select(i.star()) //
//            .from(i) //
//            .where(i.branchId.eq(b.id)) //
//            .orderBy(i.amount.desc()) //
//            .limit(1) //
//    );
//
//    Subquery l2 = sql.subquery("l2", //
//        sql.select(i.star()) //
//            .from(i) //
//            .where(i.branchId.eq(b.id)) //
//            .orderBy(i.amount.desc()) //
//            .limit(1) //
//    );
//
//    ExecutableSelect<Row> q = sql.select().from(b).joinLateral(l).leftJoinLateral(l2);
//
//    System.out.println("lateral join: " + q.getPreview());
//    q.execute().forEach(r -> System.out.println("r=" + r));
//
//  }

  private void liveSQLExamples() throws SQLException, UnsupportedTorcsDatabaseException {

//    livesql1();

    // Set Operators

//    union();

    // Subqueries
//    example1InNotIn();
//    example2ExistsNotExists();
//    example3AssymmetricOperators();
//    example4ScalarSubqueries();
//    example5TableExpressions();
//    example5NestedTableExpressions();
//    example5JoinedTableExpressions();
//    example5NamedTableExpressions();
//    example6CTEs();
//    example7RecursiveCTEs();
//    example8LateralJoins();

    torcs();

  }
  // TODO: Nothing to do, just a marker.

  private void torcs() throws SQLException, UnsupportedTorcsDatabaseException {
//  disableTorcs();
//  enableTorcs();
//  deactivateDefaultObserverTorcs();
//  changeDefaultObserverSize();
//  changeTorcsResetPeriodTo24Hours();
//  addingAnInitialQueriesRankingToTorcs();
//  addingALatestQueriesRankingToTorcs();
//  getARankingByNonDefaultOrdering();
//  saveARankingAsXLSX();
//  addingAQueryLogger();
    getSlowestQueryExecutionPlan();
  }

  private void disableTorcs() {
    // Torcs starts enabled by default
    this.torcs.deactivate();
  }

  private void enableTorcs() {
    this.torcs.activate();
  }

  private void deactivateDefaultObserverTorcs() {
    // Torcs starts with one observer active (the Default Ranking)
    this.torcs.getDefaultRanking().deactivate();
  }

  private void changeDefaultObserverSize() {
    // Reduce the ranking size to 4 entries. The default size of the default ranking
    // is 10 (that records the Top 10 slowest queries)
    // Setting the size of the ranking automatically resets and empties it.
    this.torcs.getDefaultRanking().setSize(4);
  }

  private void changeTorcsResetPeriodTo24Hours() {
    // Changes the reset period of time (minutes). Upon reaching this period of
    // time, all rankings and observers are reset/emptied. By default all Torcs
    // observers are reset every 60 minutes.
    this.torcs.setResetPeriodInMinutes(60 * 24);
  }

  private void addingAnInitialQueriesRankingToTorcs() {
    // The following InitialQueriesRanking will keep the first 50 queries ran
    InitialQueriesRanking iqr = new InitialQueriesRanking(50);
    this.torcs.register(iqr);

    // After some time the queries will be run the ranking will have recorded them
    System.out.println("--- " + "Ranking: " + iqr.getTitle() + " Execution Order ---");
    for (RankingEntry re : iqr.getRanking()) {
      System.out.println(re);
    }
    System.out.println("--- End of Ranking ---");

  }

  private void addingALatestQueriesRankingToTorcs() {
    // The following LatestQueriesRanking will keep the last 20 queries ran
    LatestQueriesRanking lqr = new LatestQueriesRanking(20);
    this.torcs.register(lqr);

    // After some time the queries will be run the ranking will have recorded them
    System.out.println("--- " + "Ranking: " + lqr.getTitle() + " Execution Order ---");
    for (RankingEntry re : lqr.getRanking()) {
      System.out.println(re);
    }
    System.out.println("--- End of Ranking ---");
  }

  private void getARankingByNonDefaultOrdering() {
    InitialQueriesRanking iqr = new InitialQueriesRanking(50);
    this.torcs.register(iqr);
    // Get the ranking entries sorted by total impact/TET (total elapsed time)
    System.out.println("--- " + "Ranking: " + iqr.getTitle() + " TET ---");
    for (RankingEntry re : iqr.getRankingByTotalElapsedTime()) {
      System.out.println(re);
    }
    System.out.println("--- End of Ranking ---");

  }

  private void saveARankingAsXLSX() {
    String xlsxName = "ranking-by-max-response-time.xlsx";
    try (OutputStream os = new FileOutputStream(new File(xlsxName))) {
      torcs.getDefaultRanking().saveAsXLSX(os);
      System.out.println("Ranking saved as: " + xlsxName);
    } catch (IOException e) {
      System.out.println("Could not save ranking as XLSX");
      e.printStackTrace();
    }
  }

  private void addingAQueryLogger() {
    this.torcs.register(new QueryExecutionObserver() {

      @Override
      public String getTitle() {
        return "Console Query Logger";
      }

      @Override
      public void apply(final QueryExecution sample) {
        System.out.println("[query] " + sample.getResponseTime() + " ms" + " (exception: "
            + (sample.getException() == null ? "N/A" : sample.getException().getClass().getName()) + ")" + ": "
            + QueryExecution.compactSQL(sample.getSQL()));
      }

      @Override
      public void reset() {
        // Nothing to do
      }
    });

  }

  private void getSlowestQueryExecutionPlan() throws SQLException, UnsupportedTorcsDatabaseException {

    Random rand = new Random(1234);
    for (int x = 0; x < 3; x++) { // run three times with different parameters
      int k = 10 + rand.nextInt(10);
      sql.select(sql.literal(3).mult(k)) //
          .execute();
    }

    System.out.println("--- Torcs Ranking ---");
    int pos = 1;
    for (RankingEntry e : this.torcs.getDefaultRanking().getRanking()) {
      System.out.println("#" + pos++ + " " + e);
      String plan = this.torcs.getEstimatedExecutionPlan(e.getSlowestExecution());
      System.out.println("Execution Plan:\n" + plan);
    }
    System.out.println("--- End of Torcs Ranking ---");

  }

}

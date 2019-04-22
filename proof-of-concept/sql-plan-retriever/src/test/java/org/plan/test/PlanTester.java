package org.plan.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.plan.ExecutionPlan;
import org.plan.exceptions.InvalidPlanException;
import org.plan.metrics.Metrics;
import org.plan.metrics.MetricsFactory;
import org.plan.operator.IndexRangeScanOperator;
import org.plan.operator.Operator;
import org.plan.operator.Operator.IndexColumn;
import org.plan.operator.Operator.SourceSet;
import org.plan.operator.SortOperator;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;
import org.plan.renderer.DotPlanRenderer;
import org.plan.renderer.TextRenderer;
import org.plan.retriever.postgresql.PostgreSQLXMLPlanParser;

public class PlanTester {

  public static void main(final String[] args) throws SQLException, InvalidPlanException, IOException {

    // ExecutionPlan<?> plan = retrieveHardCodedTestPlan();
    ExecutionPlan<?> plan = retrieveLivePlan();

    String textReport = TextRenderer.render(plan, false);
    System.out.println(textReport);

    System.out.println("\n--------------------\n");

    String dotReport = DotPlanRenderer.render(plan, false);
    System.out.println(dotReport);

    save(dotReport, "testdata/postgresql-11.1/p1.dot");
    System.out.println("DOT file saved.");

  }

  // Live Plan

  private static ExecutionPlan<Long> retrieveLivePlan() throws SQLException, InvalidPlanException {

    Connection conn = null;
    try {
      conn = getConnection();

      StringBuilder sb = new StringBuilder();

      sb.append(" \n");
      sb.append("select \n");
      sb.append("  * \n");
      sb.append("from account a \n");
      sb.append("  join transaction t4 on t4.account_id = a.id \n");
      sb.append("  join federal_branch b5 on b5.id = t4.fed_branch_id \n");
      sb.append("  join \n");
      sb.append("  ( \n");
      sb.append("    select \n");
      sb.append("      max(account_id) as account_id \n");
      sb.append("    from transaction t7 \n");
      sb.append("  ) \n");
      sb.append("  t6 on t6.account_id = a.id \n");
      sb.append("where a.current_balance < 3 * \n");
      sb.append("  ( \n");
      sb.append("    select \n");
      sb.append("      avg(amount) \n");
      sb.append("    from transaction t \n");
      sb.append("      join federal_branch b on b.id = t.fed_branch_id \n");
      sb.append("    where t.account_id = a.id \n");
      sb.append("      and b.name in (select name from federal_branch b7 where name like '%ar%') \n");
      sb.append("  ) \n");
      sb.append("  and a.current_balance < 5 * \n");
      sb.append("  ( \n");
      sb.append("    select \n");
      sb.append("      avg(amount) \n");
      sb.append("    from transaction t2 \n");
      sb.append("      join federal_branch b2 on b2.id = t2.fed_branch_id \n");
      sb.append("    where b2.name not in (select name from federal_branch b8 where name like '%y%') \n");
      sb.append("  ) \n");

      String sql = sb.toString();

      boolean executeForActualMetrics = true;

      ExecutionPlan<Long> plan = PostgreSQLXMLPlanParser.retrievePlan(conn, sql, executeForActualMetrics);

      return plan;

      // String p = PostgreSQLPlanRetriever.retrieveXMLPlan(conn, sql);
      // System.out.println("Plan:\n" + p);
      //
      // Operator op = OldPostgreSQLXMLPlanParser.parse(conn, p);
      //
      // PlanRenderer r = new TextPlanRenderer();
      // String plan = r.render(op);
      // System.out.println("Rendered Plan:\n" + plan);
      //
      // r = new DotPlanRenderer2();
      // plan = r.render(op);
      // System.out.println("Rendered Plan:\n" + plan);
      // save(plan, "testdata/databases/postgresql-9.4/plan/p1.dot");

    } finally {
      if (conn != null) {
        conn.close();
      }
    }

  }

  // Hard coded plan

  private static ExecutionPlan<Integer> retrieveHardCodedTestPlan() {

    boolean includesEstimatedMetrics = true;
    boolean includesActualMetrics = false;
    MetricsFactory factory = MetricsFactory.instantiate(includesEstimatedMetrics, includesActualMetrics);

    LinkedHashMap<String, Object> parameterValues = new LinkedHashMap<String, Object>();
    parameterValues.put("id", new Integer(10));
    parameterValues.put("minDate", new Date());

    // Index Scan

    Operator<Integer> is;
    {
      List<IndexColumn> sourceIndexColumns = new ArrayList<IndexColumn>();
      sourceIndexColumns.add(new IndexColumn("ID", null, true));
      sourceIndexColumns.add(new IndexColumn("VERSION", null, false));
      SourceSet sourceSet = new SourceSet("ACCOUNT", "a", "IX1_ACCOUNT_ID", sourceIndexColumns, true);

      List<AccessPredicate> accessPredicates = new ArrayList<AccessPredicate>();
      accessPredicates.add(new AccessPredicate("START: ID = $p001"));
      accessPredicates.add(new AccessPredicate("STOP: ID <> $p001"));

      List<FilterPredicate> filterPredicates = new ArrayList<FilterPredicate>();
      filterPredicates.add(new FilterPredicate("SARG: STARTED_AT > $p002"));

      List<Operator<Integer>> children = new ArrayList<Operator<Integer>>();

      Metrics metrics = factory.getMetrics(1234.5678, 87.456, null, null, null);

      is = new IndexRangeScanOperator<Integer>(new Integer(101), "Index Scan Ranged", sourceSet, accessPredicates,
          filterPredicates, children, metrics);
    }

    // Sort

    Operator<Integer> rootOperator;
    {
      SourceSet sourceSet = null;

      List<AccessPredicate> accessPredicates = new ArrayList<AccessPredicate>();

      List<FilterPredicate> filterPredicates = new ArrayList<FilterPredicate>();
      filterPredicates.add(new FilterPredicate("ORDER BY: NAME"));

      List<Operator<Integer>> children = new ArrayList<Operator<Integer>>();
      children.add(is);

      Metrics metrics = factory.getMetrics(2345.6789, 187.456, null, null, null);

      rootOperator = new SortOperator<Integer>(new Integer(104), "Sort", sourceSet, accessPredicates, filterPredicates,
          children, metrics);
    }

    ExecutionPlan<Integer> plan = ExecutionPlan.instantiate("query-001", new Date(),
        "select *\nfrom account\nwhere id = #{id} and started_at > #{minDate}\norder by name", parameterValues,
        rootOperator, true, false);
    return plan;
  }

  private static Connection getConnection() throws SQLException {

    String databaseURL = "jdbc:postgresql://192.168.56.213:5432/postgres";
    String user = "postgres";
    String password = "mypassword";

    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection(databaseURL, user, password);
      return conn;
    } catch (ClassNotFoundException e) {
      throw new SQLException("Driver class not found");
    } catch (SQLException e) {
      throw new SQLException("Could not connect to database: " + e.getMessage());
    }
  }

  private static void save(final String s, final String filename) throws SQLException, IOException {
    BufferedWriter w = null;
    try {
      w = new BufferedWriter(new FileWriter(filename));
      w.write(s);
    } finally {
      if (w != null) {
        w.close();
      }
    }
  }

}

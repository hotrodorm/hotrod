package explain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import explain.postgresql.PostgreSQLPlanRetriever;
import explain.postgresql.OldPostgreSQLXMLPlanParser;
import explain.renderers.DotPlanRenderer2;
import explain.renderers.PlanRenderer;
import explain.renderers.TextPlanRenderer;

public class PostgreSQLExplainer {

  public static void main(final String[] args) throws SQLException, InvalidPlanException, IOException {
    Connection conn = null;
    try {
      System.out.println("Will connect");
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

      String p = PostgreSQLPlanRetriever.retrieveXMLPlan(conn, sql);
      System.out.println("Plan:\n" + p);

      Operator op = OldPostgreSQLXMLPlanParser.parse(conn, p);

      PlanRenderer r = new TextPlanRenderer();
      String plan = r.render(op);
      System.out.println("Rendered Plan:\n" + plan);

      r = new DotPlanRenderer2();
      plan = r.render(op);
      System.out.println("Rendered Plan:\n" + plan);
      save(plan, "testdata/databases/postgresql-9.4/plan/p1.dot");

    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }

  private static Connection getConnection() throws SQLException {

    String databaseURL = "jdbc:postgresql://192.168.56.46:5432/postgres";
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

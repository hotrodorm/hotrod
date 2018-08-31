package explain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import explain.postgresql.PostgreSQLPlanRetriever;
import explain.postgresql.PostgreSQLXMLPlanParser;
import explain.renderers.TextPlanRenderer;

public class PostgreSQLExplainer {

  public static void main(final String[] args) throws SQLException, InvalidPlanException {
    Connection conn = null;
    try {
      System.out.println("Will connect");
      conn = getConnection();

      StringBuilder sb = new StringBuilder();

      sb.append(" ");
      sb.append("select ");
      sb.append("  * ");
      sb.append("from account a ");
      sb.append("  join transaction t4 on t4.account_id = a.id ");
      sb.append("  join federal_branch b5 on b5.id = t4.fed_branch_id ");
      sb.append("  join ");
      sb.append("  ( ");
      sb.append("    select ");
      sb.append("      max(account_id) as account_id ");
      sb.append("    from transaction t7 ");
      sb.append("  ) ");
      sb.append("  t6 on t6.account_id = a.id ");
      sb.append("where a.current_balance < 3 * ");
      sb.append("  ( ");
      sb.append("    select ");
      sb.append("      avg(amount) ");
      sb.append("    from transaction t ");
      sb.append("      join federal_branch b on b.id = t.fed_branch_id ");
      sb.append("    where t.account_id = a.id ");
      sb.append("      and b.name in (select name from federal_branch b7 where name like '%ar%') ");
      sb.append("  ) ");
      sb.append("  and a.current_balance < 5 * ");
      sb.append("  ( ");
      sb.append("    select ");
      sb.append("      avg(amount) ");
      sb.append("    from transaction t2 ");
      sb.append("      join federal_branch b2 on b2.id = t2.fed_branch_id ");
      sb.append("    where b2.name not in (select name from federal_branch b8 where name like '%y%') ");
      sb.append("  ) ");

      String sql = sb.toString();

      String p = PostgreSQLPlanRetriever.retrieveXMLPlan(conn, sql);
      System.out.println("Plan:\n" + p);

      Operator op = PostgreSQLXMLPlanParser.parse(conn, p);

      TextPlanRenderer r = new TextPlanRenderer();
      String plan = r.render(op);

      System.out.println("Rendered Plan:\n" + plan);

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

}

package explain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import explain.mysql8.MySQLPlanRetriever;
import explain.mysql8.parser.MySQLJSONPlanParser;
import explain.renderers.TextPlanRenderer;

public class MySQL8Explainer {

  public static void main(final String[] args) throws SQLException, InvalidPlanException {
    Connection conn = null;
    try {
      System.out.println("Will connect");
      conn = getConnection();

      StringBuilder sb = new StringBuilder();

      sb.append("select * ");
      sb.append("  from account a ");
      sb.append("  join transaction t4 on t4.account_id = a.id ");
      sb.append("  join federal_branch b5 on b5.id = t4.fed_branch_id ");
      sb.append("  join (select max(account_id) as account_id from transaction t7) t6 on t6.account_id = a.id ");
      sb.append("  where a.current_balance < 3 * ( ");
      sb.append("    select avg(amount) ");
      sb.append("      from transaction t ");
      sb.append("      join federal_branch b on b.id = t.fed_branch_id ");
      sb.append("      where t.account_id = a.id ");
      sb.append("        and b.name in (select name from federal_branch b7 where name like '%ar%') ");
      sb.append("  ) and a.current_balance < 5 * ( ");
      sb.append("    select avg(amount) ");
      sb.append("      from transaction t2 ");
      sb.append("      join federal_branch b2 on b2.id = t2.fed_branch_id ");
      sb.append("      where b2.name not in (select name from federal_branch b8 where name like '%y%') ");
      sb.append("  ) ");

      String sql = sb.toString();

      String p = MySQLPlanRetriever.retrieveJSONPlan(conn, sql);
      System.out.println("Plan:\n" + p);

      Operator op = MySQLJSONPlanParser.parse(conn, p);

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

    String databaseURL = "jdbc:mysql://192.168.56.29:3306/hotrod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    String user = "user1";
    String password = "pass1";

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

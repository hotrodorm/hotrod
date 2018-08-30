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

      // sb.append("select l.*, b.* ");
      // sb.append(" from library l ");
      // sb.append(" left join lateral ( ");
      // sb.append(" select * from book b where b.lib = l.id ");
      // sb.append(" order by hits desc ");
      // sb.append(" fetch first 2 rows only ");
      // sb.append(" ) b on true ");

      sb.append("select c.*, o.placed ");
      sb.append("  from customer c ");
      sb.append("  left join lateral ( ");
      sb.append("    select * from \"order\" o where o.customer_id = c.id ");
      sb.append("    order by o.placed desc ");
      sb.append("    fetch first 2 rows only ");
      sb.append("  ) o on true ");
      sb.append("  where ");
      sb.append("    c.first_name = 'JENNIFER' and ");
      sb.append("    lower(c.last_name) = 'campbell' and phone_number like '2025551%' ");

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

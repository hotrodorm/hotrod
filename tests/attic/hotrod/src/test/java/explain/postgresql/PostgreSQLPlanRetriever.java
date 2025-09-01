package explain.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQLPlanRetriever {

  public static String retrieveXMLPlan(final Connection conn, final String query) throws SQLException {
    String sql = "explain (analyze true, timing true, format xml) " + query;
    Statement st = conn.createStatement();
    System.out.println("sql=" + sql);
    ResultSet rs = st.executeQuery(sql);
    StringBuilder sb = new StringBuilder();
    while (rs.next()) {
      sb.append(rs.getString(1));
      sb.append("\n");
    }
    return sb.toString();
  }

  public static String retrieveTextPlan(final Connection conn, final String query) throws SQLException {
    String sql = "explain " + query;
    Statement st = conn.createStatement();
    System.out.println("sql=" + sql);
    ResultSet rs = st.executeQuery(sql);
    StringBuilder sb = new StringBuilder();
    while (rs.next()) {
      sb.append(rs.getString(1));
      sb.append("\n");
    }
    return sb.toString();
  }

}

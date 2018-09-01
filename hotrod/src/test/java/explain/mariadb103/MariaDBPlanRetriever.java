package explain.mariadb103;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDBPlanRetriever {

  public static String retrieveJSONPlan(final Connection conn, final String query) throws SQLException {
    String sql = "explain format=json " + query;
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

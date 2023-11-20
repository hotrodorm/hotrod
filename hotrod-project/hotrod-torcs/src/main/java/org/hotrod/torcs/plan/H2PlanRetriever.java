package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;

public class H2PlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int variation) throws SQLException {
    if (variation != 0) {
      throw new SQLException("Invalid H2 plan variation " + "'" + variation + "'. The only valid value is 0.");
    }
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain " + execution.getSQL());
          ResultSet rs = ps.executeQuery();) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (rs.next()) {
          sb.append((first ? "" : "\n") + rs.getString(1));
          first = false;
        }
        return sb.toString();
      } finally {
        conn.rollback();
      }
    }
  }

}

package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;

public class SybaseASEPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
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

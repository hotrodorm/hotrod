package org.hotrod.torcs.ctp.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.hotrod.torcs.setters.Setter;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLCTPPlanRetriever implements CTPPlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain (format json)\n" + execution.getSQL());) {
        for (Setter s : execution.getSetters()) {
          s.applyTo(ps);
        }
        try (ResultSet rs = ps.executeQuery();) {
          StringBuilder sb = new StringBuilder();
          boolean first = true;
          while (rs.next()) {
            sb.append((first ? "" : "\n") + rs.getString(1));
            first = false;
          }
          return sb.toString();
        }
      } finally {
        conn.rollback();
      }
    }
  }

}
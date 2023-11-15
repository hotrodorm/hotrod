package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;

public class SybaseASEPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement psIni = conn.prepareStatement("set showplan on");) {
        psIni.execute();
        try (PreparedStatement ps = conn.prepareStatement(execution.getSQL()); ResultSet rs = ps.executeQuery();) {
          StringBuilder sb = new StringBuilder();
          boolean first = true;
          SQLWarning w;
          while ((w = ps.getWarnings()) != null) {
            sb.append((first ? "" : "\n") + w.getMessage());
          }
          return sb.toString();
        }
      } finally {
        try (PreparedStatement psEnd = conn.prepareStatement("set showplan off");) {
          psEnd.execute();
        }
        conn.rollback();
      }
    }
  }

}

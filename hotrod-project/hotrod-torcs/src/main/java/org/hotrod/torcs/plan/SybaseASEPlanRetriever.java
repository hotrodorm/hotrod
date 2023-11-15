package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;

public class SybaseASEPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (Statement psIni = conn.createStatement();) {
        psIni.execute("set showplan on");
        try (PreparedStatement ps = conn.prepareStatement(execution.getSQL());) {
          for (Setter s : execution.getSetters()) {
            s.applyTo(ps);
          }
          ps.execute();
          StringBuilder sb = new StringBuilder();
          SQLWarning w = ps.getWarnings();
          sb.append(w.getMessage());
          while ((w = w.getNextWarning()) != null) {
            sb.append(w.getMessage());
          }
          return sb.toString();
        }
      } finally {
        try (Statement psEnd = conn.createStatement();) {
          psEnd.execute("set showplan off");
        }
        conn.rollback();
      }
    }
  }

}

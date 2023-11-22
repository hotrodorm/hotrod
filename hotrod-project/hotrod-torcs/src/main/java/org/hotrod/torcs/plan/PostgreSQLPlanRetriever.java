package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;

public class PostgreSQLPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int format) throws SQLException {

    String formatOption;
    switch (format) {
    case 0:
      formatOption = " (format text)";
      break;
    case 1:
      formatOption = " (format xml)";
      break;
    case 2:
      formatOption = " (format json)";
      break;
    case 3:
      formatOption = " (format yaml)";
      break;
    default:
      throw new SQLException("Invalid PostgreSQL plan format '" + format
          + "'. Valid values are: 0 (TEXT), 1 (XML), 2 (JSON) and 3 (YAML).");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain" + formatOption + " " + execution.getSQL());) {
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

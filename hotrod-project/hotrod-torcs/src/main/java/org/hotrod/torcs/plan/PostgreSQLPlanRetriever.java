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
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int variation) throws SQLException {

    String variationOption;
    switch (variation) {
    case 0:
      variationOption = " (format text)";
      break;
    case 1:
      variationOption = " (format xml)";
      break;
    case 2:
      variationOption = " (format json)";
      break;
    case 3:
      variationOption = " (format yaml)";
      break;
    default:
      throw new SQLException("Invalid PostgreSQL plan variation " + "'" + variation
          + "'. Valid values are: 0 (TEXT), 1 (XML), 2 (JSON) and 3 (YAML).");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain" + variationOption + " " + execution.getSQL());) {
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

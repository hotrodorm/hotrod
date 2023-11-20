package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;

public class OraclePlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int variation) throws SQLException {

    String variationName;
    switch (variation) {
    case 0:
      variationName = "typical";
      break;
    case 1:
      variationName = "basic";
      break;
    case 2:
      variationName = "all";
      break;
    default:
      throw new SQLException("Invalid Oracle plan variation " + "'" + variation + "'. Valid values are between 0 and 2.");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain plan for\n" + execution.getSQL());) {
        for (Setter s : execution.getSetters()) {
          s.applyTo(ps);
        }
        ps.execute();
        try (
            PreparedStatement psr = conn.prepareStatement(
                "select plan_table_output from table(dbms_xplan.display('sys.plan_table$', null,'" + variationName + "'))");
            ResultSet rs = psr.executeQuery();) {
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

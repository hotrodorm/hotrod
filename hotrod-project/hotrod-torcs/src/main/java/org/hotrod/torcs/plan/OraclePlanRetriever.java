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
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
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
                "select plan_table_output from table(dbms_xplan.display('sys.plan_table$', null,'typical'))");
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
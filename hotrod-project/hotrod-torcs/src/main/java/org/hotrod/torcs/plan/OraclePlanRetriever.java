package org.hotrod.torcs.plan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;

public class OraclePlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int format) throws SQLException {

    String formatName;
    switch (format) {
    case 0:
      formatName = "typical";
      break;
    case 1:
      formatName = "basic";
      break;
    case 2:
      formatName = "all";
      break;
    default:
      throw new SQLException("Invalid Oracle plan format '" + format + "'. Valid values are between 0 and 2.");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);

      if (execution.getNameSetters().isEmpty()) {

        try (PreparedStatement ps = conn.prepareStatement("explain plan for\n" + execution.getSQL());) {
          for (IndexSetter s : execution.getIndexSetters()) {
            s.applyTo(ps);
          }
          ps.execute();
          try (PreparedStatement psr = conn.prepareStatement(
              "select plan_table_output from table(dbms_xplan.display('sys.plan_table$', null,'" + formatName + "'))");
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

      } else {

        try (CallableStatement cs = conn.prepareCall("explain plan for\n" + execution.getSQL());) {
          for (IndexSetter s : execution.getIndexSetters()) {
            s.applyTo(cs);
          }
          for (NameSetter s : execution.getNameSetters()) {
            s.applyTo(cs);
          }
          cs.execute();
          try (PreparedStatement psr = conn.prepareStatement(
              "select plan_table_output from table(dbms_xplan.display('sys.plan_table$', null,'" + formatName + "'))");
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

}

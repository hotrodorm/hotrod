package org.hotrod.torcs.ctp.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLCTPPlanRetriever implements CTPPlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);

      if (execution.getNameSetters().isEmpty()) {

        try (PreparedStatement ps = conn.prepareStatement("explain (format json)\n" + execution.getSQL());) {
          for (IndexSetter s : execution.getIndexSetters()) {
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

      } else {

        try (CallableStatement cs = conn.prepareCall("explain (format json)\n" + execution.getSQL());) {
          for (IndexSetter s : execution.getIndexSetters()) {
            s.applyTo(cs);
          }
          for (NameSetter s : execution.getNameSetters()) {
            s.applyTo(cs);
          }
          try (ResultSet rs = cs.executeQuery();) {
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

package org.hotrod.torcs.plan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;

public class SybaseASEPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int format) throws SQLException {
    if (format != 0) {
      throw new SQLException("Invalid Sybase ASE plan format '" + format + "'. The only valid value is 0.");
    }
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (Statement psIni = conn.createStatement();) {
        psIni.execute("set showplan on");

        if (execution.getNameSetters().isEmpty()) {

          try (PreparedStatement ps = conn.prepareStatement(execution.getSQL());) {
            for (IndexSetter s : execution.getIndexSetters()) {
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

        } else {

          try (CallableStatement cs = conn.prepareCall(execution.getSQL());) {
            for (IndexSetter s : execution.getIndexSetters()) {
              s.applyTo(cs);
            }
            for (NameSetter s : execution.getNameSetters()) {
              s.applyTo(cs);
            }
            cs.execute();
            StringBuilder sb = new StringBuilder();
            SQLWarning w = cs.getWarnings();
            sb.append(w.getMessage());
            while ((w = w.getNextWarning()) != null) {
              sb.append(w.getMessage());
            }
            return sb.toString();
          }

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

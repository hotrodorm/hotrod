package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;

public class SQLServerPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (Statement psIni = conn.createStatement();) {
        boolean p1 = psIni.execute("set showplan_text on");
        System.out.println(">>> p1=" + p1);
        try (PreparedStatement ps = conn.prepareStatement(execution.getSQL());) {
          for (Setter s : execution.getSetters()) {
            System.out.println(">>> applying setter #" + s.getIndex() + " value=" + s.value());
            s.applyTo(ps);
          }
          System.out.println(">>> sql=" + execution.getSQL());
          boolean more1 = ps.execute();

          System.out.println("more1=" + more1);
          read(ps);

          boolean more2 = ps.getMoreResults();
          System.out.println("more2=" + more2);
          read(ps);

          return "no plan yet";

//          System.out.println("--- more1=" + more1);
//          StringBuilder sb = new StringBuilder();
//          while (more1) {
//            System.out.println("--- RESULT SET STARTS ---");
//            sb.append("--- RESULT SET STARTS ---\n");
//            ResultSet rs = ps.getResultSet();
//            boolean first = true;
//            while (rs.next()) {
//              String s = rs.getString(1);
//              System.out.println("--- LINE: " + s);
//              sb.append((first ? "" : "\n") + rs.getString(1));
//              first = false;
//            }
//            more1 = ps.getMoreResults();
//            System.out.println("--- more2=" + more1);
//          }
//          return sb.toString();

//          while (rsx.next()) {
//            String s1 = rsx.getString(1);
//            System.out.println(">>> s1=" + s1);
//          }
//          System.out.println(">>> more=" + more);
//
//          return "abc";

//          try (ResultSet rs = ps.executeQuery();) {
//            boolean moreRS = ps.getMoreResults();
//            System.out.println(">>> moreRS=" + moreRS);
//            StringBuilder sb = new StringBuilder();
//            boolean first = true;
//            while (rs.next()) {
//              sb.append((first ? "" : "\n") + rs.getString(1));
//              first = false;
//            }
//            return sb.toString();
//          }
        }
      } finally {
        try (Statement psEnd = conn.createStatement();) {
          psEnd.execute("set showplan_text off");
        } finally {
          conn.rollback();
        }
      }
    }
  }

  private void read(PreparedStatement ps) throws SQLException {
    ResultSet rs = ps.getResultSet();

    while (rs.next()) {
      int val = rs.getInt(1);
      System.out.println("val=" + val);
    }
  }

}

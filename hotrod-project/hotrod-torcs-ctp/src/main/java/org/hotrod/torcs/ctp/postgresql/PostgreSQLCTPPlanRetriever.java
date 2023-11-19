package org.hotrod.torcs.ctp.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.hotrod.torcs.setters.Setter;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLCTPPlanRetriever implements CTPPlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain " + execution.getSQL());) {
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

//private AtomicLong n = new AtomicLong();

//  @Override
//  public String getEstimatedCTPExecutionPlan(final QueryExecution execution) {
//    PreparedSQL ps = new PreparedSQL(execution.getSQL());
//    System.out.println("ps=" + ps);
//    Map<String, Object> params = new HashMap<>();
//    params.put("planName", "plan" + n.getAndIncrement());
//    params.put("paramTypes", String.join(",", Collections.nCopies(ps.getNumberOfParams(), "unknown")));
//    params.put("sql", ps.getPreparedSQL());
//    params.put("paramValues", String.join(",", Collections.nCopies(ps.getNumberOfParams(), "null")));
//
//    List<String> r = this.mapper.getEstimatedPlan(params);
//    return r.stream().collect(Collectors.joining(" "));
//    return null;
//  }

//  @Override
//  public String getActualCTPExecutionPlan(final QueryExecution execution) {
//    Map<String, Object> params = new HashMap<>();
//    params.put("sql", execution.getSQL());
//    List<String> r = this.mapper.getActualPlan(params);
//    return r.stream().collect(Collectors.joining(" "));
//  }
//
//  public class PreparedSQL {
//
//    private String sql;
//    private int numberOfParams;
//
//    public PreparedSQL(final String sql) {
//      this.numberOfParams = 0;
//      StringBuilder sb = new StringBuilder();
//      int pos = 0;
//      boolean inApos = false;
//      boolean inQuotes = false;
//      while (pos < sql.length()) {
//        String s = null;
//        int c = sql.charAt(pos);
//        if (inApos) {
//          if (c == '\'') {
//            inApos = false;
//          }
//        } else if (inQuotes) {
//          if (c == '"') {
//            inQuotes = false;
//          }
//        } else { // plain text
//          if (c == '\'') {
//            inApos = true;
//          } else if (c == '"') {
//            inQuotes = true;
//          } else if (c == '?') {
//            s = "$" + (++this.numberOfParams);
//          }
//        }
//        if (s == null) {
//          sb.append((char) c);
//        } else {
//          sb.append(s);
//        }
//        pos++;
//      }
//      this.sql = sb.toString();
//    }
//
//    public String getPreparedSQL() {
//      return sql;
//    }
//
//    public int getNumberOfParams() {
//      return numberOfParams;
//    }
//
//    public String toString() {
//      return "[" + this.numberOfParams + "]" + this.sql;
//    }
//
//  }

}

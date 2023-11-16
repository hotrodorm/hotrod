package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.CouldNotToGuessDataTypeException;
import org.hotrod.torcs.setters.DataTypeNotImplementedException;
import org.hotrod.torcs.setters.Setter;

public class SQLServerPlanRetriever implements PlanRetriever {

  private enum PlanType {
    TEXT, XML
  };

  private static final PlanType PLAN_TYPE = PlanType.XML;

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (Statement stIni = conn.createStatement();) {
        boolean p1 = stIni.execute("set showplan_" + PLAN_TYPE + " on");
//        System.out.println(">>> p1=" + p1);

        try (Statement st = conn.createStatement();) {

          SQLServerPlanSQL pp = new SQLServerPlanSQL(execution.getSQL(), execution.getSetters());
          String sql = pp.render();

//          System.out.println(">>> --- sql ---\n" + execution.getSQL() + "\n---");
          boolean more1 = st.execute(sql);
          int ct1 = st.getUpdateCount();
//          System.out.println("more1=" + more1 + " ct1=" + ct1);

          if (PLAN_TYPE == PlanType.XML) {

            String plan = getResult(st);
            return plan;

          } else {

            String engineSQL = getResult(st);
//          System.out.println("engineSQL:\n" + engineSQL);

            boolean more2 = st.getMoreResults();
            int ct2 = st.getUpdateCount();
//            System.out.println("more2=" + more2 + " ct2=" + ct2);

            String plan = getResult(st);
//          System.out.println("plan:\n" + plan);

            return plan;
          }
        }
      } finally {
        try (Statement stEnd = conn.createStatement();) {
          stEnd.execute("set showplan_" + PLAN_TYPE + " off");
        } finally {
          conn.rollback();
        }
      }
    }
  }

  public class SQLServerPlanSQL {

    private Collection<Setter> setters;
    private List<String> parameters = new ArrayList<>();
    private String psql;

    private SQLServerPlanSQL(final String sql, final Collection<Setter> setters) {
//      System.out.println("--- ORIGINAL SQL\n" + sql + "\n---");
      if (sql == null) {
        throw new IllegalArgumentException("SQL statement cannot be null");
      }

      this.setters = setters;
      StringBuilder sb = new StringBuilder();
      int pos = 0;

      while (pos < sql.length()) {
        int apos = sql.indexOf('\'', pos);
        int qm = sql.indexOf('?', pos);
//        System.out.println("::: pos=" + pos + " apos=" + apos + " qm=" + qm);
        if (apos == -1 && qm == -1) { // none
//          System.out.println("::: none");
          sb.append(sql.substring(pos));
          pos = sql.length();
        } else if (apos != -1 && qm == -1) { // apos
//          System.out.println("::: apos");
          pos = processApostrophe(sql, sb, pos, apos);
        } else if (apos == -1 && qm != -1) { // qm
//          System.out.println("::: qm");
          pos = processQuestionMark(sql, sb, pos, qm);
        } else { // both
//          System.out.println("::: both - apos < qm = " + (apos < qm));
          if (apos < qm) {
            pos = processApostrophe(sql, sb, pos, apos);
          } else {
            pos = processQuestionMark(sql, sb, pos, qm);
          }
        }
      }

      this.psql = sb.toString();
    }

    private int processApostrophe(final String sql, final StringBuilder sb, final int pos, final int apos) {
      int ipos = apos + 1;
      sb.append(sql.substring(pos, ipos));
      while (ipos < sql.length()) {
        int ix = sql.indexOf('\'', ipos);
        if (ix == -1) { // no more apos
          sb.append(sql.substring(ipos));
          ipos = sql.length();
          return ipos;
        } else {
          if (ix + 1 < sql.length() && sql.charAt(ix + 1) == '\'') { // double apos
            sb.append(sql.substring(ipos, ix + 2));
            ipos = ix + 2;
          } else { // apos (ending)
            sb.append(sql.substring(ipos, ix + 1));
            ipos = ix + 1;
            return ipos;
          }
        }
      }
      return sql.length();
    }

    private int processQuestionMark(final String sql, final StringBuilder sb, final int pos, final int qm) {
      sb.append(sql.substring(pos, qm));
      String p = "@p" + this.parameters.size();
      this.parameters.add(p);
      sb.append(p);
      return qm + 1;
    }

//    declare @abc varchar; declare @def varchar; select amount_granted + @abc + @def from account

    private String render() {
//      System.out.println("||| " + this.parameters.size() + " parameters to render");
      StringBuilder sb = new StringBuilder();
      Iterator<Setter> it = this.setters.iterator();
      for (String p : this.parameters) {
        Setter s = it.hasNext() ? it.next() : null;
        String dbType = guessDBType(s);
        sb.append("declare " + p + " " + dbType + ";\n");
      }
      sb.append(this.psql);
      return sb.toString();
    }

    private String guessDBType(final Setter s) {
      if (s == null) {
        return "varchar";
      }
      try {
        return s.guessSQLServerDataType();
      } catch (DataTypeNotImplementedException e) {
        return "varchar";
      } catch (CouldNotToGuessDataTypeException e) {
        return "varchar";
      }
    }

  }

  private static String getResult(final Statement ps) throws SQLException {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    ResultSet rs = ps.getResultSet();
    while (rs.next()) {
      String line = rs.getString(1);
      if (!first) {
        sb.append("\n");
      }
      sb.append(line);
      first = false;
    }
    return sb.toString();
  }

}

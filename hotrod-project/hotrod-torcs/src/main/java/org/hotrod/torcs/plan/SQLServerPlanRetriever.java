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
import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;
import org.hotrod.torcs.setters.index.DataTypeNotImplementedException;
import org.hotrod.torcs.setters.index.IndexSetter;

public class SQLServerPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int format) throws SQLException {

    String formatKeyword;
    switch (format) {
    case 0:
      formatKeyword = "TEXT";
      break;
    case 1:
      formatKeyword = "XML";
      break;
    default:
      throw new SQLException("Invalid SQL Server plan format '" + format + "'. Valid values are: 0 (TEXT), 1 (XML).");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (Statement stIni = conn.createStatement();) {
        stIni.execute("set showplan_" + formatKeyword + " on");

        if (execution.getNameSetters().isEmpty()) {

          try (Statement st = conn.createStatement();) {
            SQLServerPlanSQL pp = new SQLServerPlanSQL(execution.getSQL(), execution.getIndexSetters());
            String sql = pp.render();
            st.execute(sql);
            if (formatKeyword.equals("XML")) {
              return getResult(st);
            } else {
              getResult(st);
              st.getMoreResults();
              return getResult(st);
            }
          }

        } else {

          throw new UnsupportedOperationException("Cannot retrieve SQL Server plan that use CallableStatements, "
              + "or that use parameter setting using names instead of indexes.");
          // It may be possible to do but requires more testing

        }

      } finally {
        try (Statement stEnd = conn.createStatement();) {
          stEnd.execute("set showplan_" + formatKeyword + " off");
        } finally {
          conn.rollback();
        }
      }
    }
  }

  public class SQLServerPlanSQL {

    private Collection<IndexSetter> setters;
    private List<String> parameters = new ArrayList<>();
    private String psql;

    private SQLServerPlanSQL(final String sql, final Collection<IndexSetter> setters) {
      if (sql == null) {
        throw new IllegalArgumentException("SQL statement cannot be null");
      }

      this.setters = setters;
      StringBuilder sb = new StringBuilder();
      int pos = 0;

      while (pos < sql.length()) {
        int apos = sql.indexOf('\'', pos);
        int qm = sql.indexOf('?', pos);
        if (apos == -1 && qm == -1) { // none
          sb.append(sql.substring(pos));
          pos = sql.length();
        } else if (apos != -1 && qm == -1) { // apos
          pos = processApostrophe(sql, sb, pos, apos);
        } else if (apos == -1 && qm != -1) { // qm
          pos = processQuestionMark(sql, sb, pos, qm);
        } else { // both
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

    private String render() {
      StringBuilder sb = new StringBuilder();
      Iterator<IndexSetter> it = this.setters.iterator();
      for (String p : this.parameters) {
        IndexSetter s = it.hasNext() ? it.next() : null;
        String dbType = guessDBType(s);
        sb.append("declare " + p + " " + dbType + ";\n");
      }
      sb.append(this.psql);
      return sb.toString();
    }

    private String guessDBType(final IndexSetter s) {
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

package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.SQLParameterWriter.QueryParameter;
import org.hotrod.livesql.queries.typesolver.TShield;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.utils.CUtil;
import org.hotrod.utils.HexaUtils;

public class LiveSQLPreparedQuery {

  private String sql;
  private List<QueryParameter<?, ?>> parameters;
  private List<Expression> queryColumns;

  public LiveSQLPreparedQuery(final String sql, final List<QueryParameter<?, ?>> parameters,
      final List<Expression> queryColumns) {
    this.sql = sql;
    this.parameters = parameters;
    this.queryColumns = queryColumns;
  }

  public String getSQL() {
    return sql;
  }

  public List<Expression> getQueryColumns() {
    return this.queryColumns;
  }

  public void setFirstParameter(Object value) {
    if (this.parameters.size() > 0) {
      QueryParameter<?, ?> first = this.parameters.iterator().next();
      first.setValue(value);
    }
  }

  public void applyParameters(PreparedStatement ps, Connection conn) throws SQLException {
    int n = 1;
    for (QueryParameter<?, ?> p : this.parameters) {
      int i = n++;
      Object value = p.getValue(conn);
      ps.setObject(i, value);
    }
  }

  public String getPreview(boolean includeParameters) {
    if (!includeParameters) {

      return this.sql;

    } else {

      StringBuilder sb = new StringBuilder();
      sb.append("--- SQL ------------------\n");
      sb.append(this.sql);
      sb.append("\n--- Parameters (" + this.parameters.size() + ") -------\n");

      for (QueryParameter<?, ?> p : this.parameters) {
        String name = p.getName();
        Object value = p.getDomainValue();

        // Compute text-format preview of the value, including length if applicable
        Integer length = null;
        String preview = null;
        if (value instanceof String) {
          String v = (String) value;
          length = v.length();
          if (length <= 250) {
            preview = v;
          } else {
            preview = v.substring(0, 250) + "...";
          }
        } else if (value instanceof byte[]) {
          byte[] v = (byte[]) value;
          length = v.length;
          if (v.length < 100) {
            preview = HexaUtils.toHexa(v);
          } else {
            preview = HexaUtils.toHexa(v, 0, 100) + "...";
          }
        } else {
          preview = "" + value;
        }

        sb.append(" * " + name
            + (value == null ? ""
                : " (" + CUtil.renderObjectClass(value) + (length == null ? "" : ", length=" + length) + ")")
            + ": " + preview + (p.getConverter() != null ? " - converter will be applied during execution" : "")
            + "\n");

      }

      if (this.queryColumns != null) {
        sb.append("--- Query Columns (" + this.queryColumns.size() + ") ---\n");
        int ordinal = 1;
        for (Expression expr : this.queryColumns) {
          String name = Shield.getReferenceName(expr);
          TypeHandler<?, ?> th = Shield.getTypeHandler(expr);
          String TNA = "Runtime type not available during preview, but only when executing the query -- use logging to display it";
          sb.append(" * " + ordinal + " " + name + ": " //
              + (th == null ? TNA
                  : TShield.render(th) + (th.getRuleNumber() == null ? "" : ", rule #" + th.getRuleNumber()))//
              + "\n");
          ordinal++;
        }
      }

      sb.append("---------------------");
      return sb.toString();
    }

  }

}

package org.hotrod.livesql.queries;

import java.util.LinkedHashMap;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.typesolver.TShield;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.utils.CUtil;
import org.hotrod.utils.HexaUtils;

public class LiveSQLPreparedQuery {

  private String sql;
  private LinkedHashMap<String, Object> parameters;
  private List<Expression> queryColumns;

  public LiveSQLPreparedQuery(final String sql, final LinkedHashMap<String, Object> parameters,
      final List<Expression> queryColumns) {
    this.sql = sql;
    this.parameters = parameters;
    this.queryColumns = queryColumns;
  }

  public String getSQL() {
    return sql;
  }

  public LinkedHashMap<String, Object> getParameters() {
    return this.parameters;
  }

  public List<Expression> getQueryColumns() {
    return this.queryColumns;
  }

  public LinkedHashMap<String, Object> getConsolidatedParameters() {
    LinkedHashMap<String, Object> c = new LinkedHashMap<String, Object>();
    c.putAll(this.parameters);
    c.put("sql", this.sql);
    return c;
  }

  public String getPreview(boolean includeParameters) {
    if (!includeParameters) {

      return this.sql;

    } else {

      StringBuilder sb = new StringBuilder();
      sb.append("--- SQL -------------\n");
      sb.append(this.sql);
      sb.append("\n--- Parameters ------\n");
      for (String name : this.parameters.keySet()) {
        Object value = this.parameters.get(name);
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
            + ": " + preview + "\n");

      }

      if (this.queryColumns != null) {
        sb.append("--- Query Columns ---\n");
        int ordinal = 1;
        for (Expression expr : this.queryColumns) {
          String name = Shield.getReferenceName(expr);
          TypeHandler<?, ?> th = Shield.getTypeHandler(expr);
          sb.append(" * " + ordinal + " " + name + ": " //
              + (th != null ? TShield.render(th)
                  : "class N/A, source: " + TypeSource.RUNTIME_JDBC_DRIVER_DEFAULT.name()) //
              + (th.getRuleNumber() == null ? "" : ", rule #" + th.getRuleNumber()) //
              + "\n");
          ordinal++;
        }
      }

      sb.append("---------------------");
      return sb.toString();
    }

  }

}

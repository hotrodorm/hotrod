package org.hotrod.livesql.queries;

import java.util.LinkedHashMap;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.typesolver.THelper;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.utils.CUtil;
import org.hotrod.utils.HexaUtils;

public class LiveSQLPreparedQuery {

  private String sql;
  private LinkedHashMap<String, Object> parameters;
  private LinkedHashMap<String, Expression> queryColumns;

  public LiveSQLPreparedQuery(final String sql, final LinkedHashMap<String, Object> parameters,
      final LinkedHashMap<String, Expression> queryColumns) {
    this.sql = sql;
    this.parameters = parameters;
    this.queryColumns = queryColumns;
  }

  public String getSQL() {
    return sql;
  }

  public LinkedHashMap<String, Object> getParameters() {
    return parameters;
  }

  public LinkedHashMap<String, Expression> getQueryColumns() {
    return queryColumns;
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
      sb.append("--- SQL ----------\n");
      sb.append(this.sql);
      sb.append("\n--- Parameters ---\n");
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

      if (queryColumns != null) {
        sb.append("--- Query Columns ---\n");
        for (String name : queryColumns.keySet()) {
          Expression expr = queryColumns.get(name);
          TypeHandler<?, ?> th = Shield.getTypeHandler(expr);
          sb.append(" * " + name + ": " + (th != null ? THelper.render(th)
              : "(type to be determined by query metadata or by <type-solver> rules)") + "\n");
        }
      }

      sb.append("------------------\n");
      return sb.toString();
    }

  }

}

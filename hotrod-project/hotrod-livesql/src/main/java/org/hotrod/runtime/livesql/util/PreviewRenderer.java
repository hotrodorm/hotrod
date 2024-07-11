package org.hotrod.runtime.livesql.util;

import java.util.LinkedHashMap;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrodorm.hotrod.utils.CUtil;
import org.hotrodorm.hotrod.utils.HexaUtils;

public class PreviewRenderer {

  public static String render(final LiveSQLPreparedQuery q) {
    StringBuilder sb = new StringBuilder();
    sb.append("--- SQL ----------\n");
    sb.append(q.getSQL());
    sb.append("\n--- Parameters ---\n");
    for (String name : q.getParameters().keySet()) {
      Object value = q.getParameters().get(name);
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

    LinkedHashMap<String, Expression> queryColumns = q.getQueryColumns();
    if (queryColumns != null) {
      sb.append("--- Query Columns ---\n");
      for (String name : queryColumns.keySet()) {
        Expression expr = queryColumns.get(name);
        TypeHandler th = Helper.getTypeHandler(expr);
        sb.append(" * " + name + ": "
            + (th != null ? Helper.render(th) : "(type to be determined by query metadata or by <type-solver> rules)")
            + "\n");
      }
    }

    sb.append("------------------\n");
    return sb.toString();
  }

}

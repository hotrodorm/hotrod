package org.hotrod.runtime.livesql.util;

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
    sb.append("------------------");
    return sb.toString();
  }

}

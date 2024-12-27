package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.TypedParameterSegment;
import org.hotrod.dynamic.segments.VariableInstanceValueSegment;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.SUtil;

public abstract class PreparedQuery {

  protected String sql;
  protected List<ParameterSegment> parameters;

  public PreparedQuery(String sql, List<ParameterSegment> parameters) {
    this.sql = sql;
    this.parameters = parameters;
  }

  private static final int MAX_DISPLAY_VALUE = 100;

  public String getPreview() {
    return this.getPreview(false);
  }

  public String getPreview(boolean includeParameters) {
    if (!includeParameters) {
      return this.sql;
    } else {
      StringBuilder p = new StringBuilder();
      p.append(this.sql);
      p.append("\n=== Parameters (" + this.parameters.size() + ") ===\n");
      int pos = 1;
      for (ParameterSegment ps : this.parameters) {

        String type = "";
        if (ps instanceof TypedParameterSegment) {
          type = JDBCTypes.codeToShortName(((TypedParameterSegment) ps).getSQLType());
        } else if (ps instanceof VariableInstanceValueSegment) {
          type = "inner variable";
        }

        Object value = ps.getValue();
        String tostring = "" + value;
        if (tostring.length() > MAX_DISPLAY_VALUE) {
          tostring = tostring.substring(0, MAX_DISPLAY_VALUE - 3) + "...";
        }
        String name = ps.getName();
        p.append("" + pos++ + ". " + name + " (" + SUtil.coalesce(type, "OTHER") + "): " + tostring
            + (value == null ? "" : " (" + value.getClass().getName() + ")") + "\n");
      }
      if (!this.parameters.isEmpty()) {
        p.append("======================\n");
      }
      return p.toString();
    }
  }

}

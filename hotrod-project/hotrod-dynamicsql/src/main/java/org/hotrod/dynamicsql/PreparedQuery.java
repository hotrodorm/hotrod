package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.parameters.ParameterNullableInstance;

public abstract class PreparedQuery {

  protected String sql;
  protected List<ParameterInstance> parameters;

  public PreparedQuery(String sql, List<ParameterInstance> parameters) {
    this.sql = sql;
    this.parameters = parameters;
  }

  public static final int MAX_DISPLAY_VALUE = 100;

  public String getPreview() {
    return this.getPreview(true);
  }

  public String getPreview(boolean includeParameters) {
    if (!includeParameters) {
      return this.sql;
    } else {
      StringBuilder p = new StringBuilder();
      p.append(this.sql);
      p.append("\n=== JDBC Parameters (" + this.parameters.size() + ") ===\n");
      int pos = 1;
      for (ParameterInstance ps : this.parameters) {

        String jdbcType = null;
        if (ps instanceof ParameterNullableInstance) {
          jdbcType = JDBCTypes.codeToShortName(((ParameterNullableInstance) ps).getSQLType());
        }

        Object value = ps.getValue();
        String tostring = "" + value;
        if (tostring.length() > MAX_DISPLAY_VALUE) {
          tostring = tostring.substring(0, MAX_DISPLAY_VALUE - 3) + "...";
        }
        String name = ps.getName();
        p.append("" + pos++ + ". " + name + (jdbcType == null ? "" : " (" + Utl.coalesce(jdbcType, "OTHER") + ")")
            + ": " + tostring + (value == null ? "" : " (" + value.getClass().getName() + ")") + "\n");
      }
      if (!this.parameters.isEmpty()) {
        p.append("===========================\n");
      }
      return p.toString();
    }
  }

}

package org.hotrod.dynamicsql;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.parameters.ParameterNullableInstance;

public abstract class PreparedQuery {

  protected String sql;
  protected List<ParameterInstance> parameters;

  protected String formattedSQL = null;

  public PreparedQuery(String sql, List<ParameterInstance> parameters) {
    if (sql == null) {
      throw new RuntimeException("Invalid empty SQL query");
    }
    this.sql = sql;
    this.parameters = parameters;
  }

  public static final int MAX_DISPLAY_VALUE = 100;

  public String getPreview() {
    return this.getPreview(false);
  }

  public String getPreview(boolean includeParameters) {
    if (!includeParameters) {
      formatSQL();
      return this.formattedSQL;
    } else {
      StringBuilder p = new StringBuilder();
      formatSQL();
      p.append(this.formattedSQL);
      p.append("\n\nJDBC Parameters (" + this.parameters.size() + "):\n");
      int pos = 1;
      if (this.parameters.isEmpty()) {
        p.append("  N/A\n");
      } else {
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
          p.append("  " + pos++ + ". " + name + (jdbcType == null ? "" : " (" + Utl.coalesce(jdbcType, "OTHER") + ")")
              + ": " + tostring + (value == null ? "" : " (" + value.getClass().getName() + ")") + "\n");
        }
      }
      return p.toString();
    }
  }

  private void formatSQL() {
    if (this.formattedSQL == null) {
      String[] lines = this.sql.split("\n");
      if (lines != null) {
        this.formattedSQL = Arrays.stream(lines).filter(l -> !l.trim().isEmpty())
            .collect(Collectors.joining("\n"));
      } else {
        this.formattedSQL = "";
      }
    }
  }

}

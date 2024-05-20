package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;
import java.util.List;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.QueryParameter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class QueryWriter {

  private static final String INDENT = "  "; // two spaces to indent each level

  private LiveSQLContext context;
  private ColumnsCollector columnsCollector;

  private StringBuilder sb;
  private int level;
  private int col;

  private SQLParameterWriter paramWriter;

  public QueryWriter(final LiveSQLContext context, final ColumnsCollector columnsCollector) {
    this.columnsCollector = columnsCollector;
    this.context = context;
    this.sb = new StringBuilder();
    this.level = 0;
    this.col = 0;
    if (context.usePlainJDBC()) {
      this.paramWriter = new JDBCParameterWriter();
    } else {
      this.paramWriter = new MyBatisParameterWriter();
    }
  }

  public RenderedParameter registerParameter(final Object value) {
    return this.paramWriter.registerParameter(value);
  }
  
  public void registerQueryColumns(final List<ResultSetColumn> queryColumns) {
    this.columnsCollector.register(queryColumns);
  }

  public void enterLevel() {
    this.level++;
  }

  public void exitLevel() {
    this.level--;
  }

  public int getLevel() {
    return this.level;
  }

  public void write(final String txt) {
    if (txt != null) {
      String[] lines = txt.split("\n");
      for (int i = 0; i < lines.length; i++) {
        String line = lines[i];
        if (i > 0) {
          newLine();
        }
        indent();
        this.sb.append(line);
        this.col = this.col + line.length();
      }
      if (txt.endsWith("\n")) {
        newLine();
      }
    }
  }

  private void newLine() {
    this.sb.append("\n");
    this.col = 0;
  }

  private void indent() {
    int missingIndents = (this.level * INDENT.length() - this.col) / INDENT.length();
    for (int i = 0; i < missingIndents; i++) {
      this.sb.append(INDENT);
      this.col = this.col + INDENT.length();
    }
  }

  public void write(final ComparableExpression expression) {
    if (expression != null) {
      expression.renderTo(this);
    }
  }

  public LiveSQLDialect getSQLDialect() {
    return this.context.getLiveSQLDialect();
  }

  public LiveSQLPreparedQuery getPreparedQuery() {
    LinkedHashMap<String, Object> p = new LinkedHashMap<String, Object>();
    for (QueryParameter qp : this.paramWriter.getParameters()) {
      p.put(qp.getName(), qp.getValue());
    }
    return new LiveSQLPreparedQuery(this.sb.toString(), p);
  }

  // Prepared Query

  public static class LiveSQLPreparedQuery {

    private String sql;
    private LinkedHashMap<String, Object> parameters;

    public LiveSQLPreparedQuery(final String sql, final LinkedHashMap<String, Object> parameters) {
      this.sql = sql;
      this.parameters = parameters;
    }

    public String getSQL() {
      return sql;
    }

    public LinkedHashMap<String, Object> getParameters() {
      return parameters;
    }

    public LinkedHashMap<String, Object> getConsolidatedParameters() {
      LinkedHashMap<String, Object> c = new LinkedHashMap<String, Object>();
      c.putAll(this.parameters);
      c.put("sql", this.sql);
      return c;
    }

    public String render() {
      StringBuilder sb = new StringBuilder();
      sb.append("--- SQL ---\n");
      sb.append(this.sql);
      sb.append("\n--- Parameters ---\n");
      for (String name : this.parameters.keySet()) {
        Object value = this.getParameters().get(name);
        sb.append(" * " + name + (value == null ? "" : " (" + value.getClass().getName() + ")") + ": " + value + "\n");
      }
      sb.append("------------------\n");
      return sb.toString();
    }

  }

}

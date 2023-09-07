package org.hotrod.runtime.livesql.queries.select;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;

public class QueryWriter {

  private static final String INDENT = "  "; // two spaces to indent each level

  private LiveSQLDialect sqlDialect;

  private AtomicLong n;
  private StringBuilder sb;
  private LinkedHashMap<String, Object> params;
  private int level;
  private int col;

  public QueryWriter(final LiveSQLDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
    this.n = new AtomicLong();
    this.sb = new StringBuilder();
    this.params = new LinkedHashMap<String, Object>();
    this.level = 0;
    this.col = 0;
  }

  public String registerParameter(final Object value) {
    String name = "p" + this.n.incrementAndGet();
    this.params.put(name, value);
    return name;
  }

  public void enterLevel() {
    this.level++;
  }

  public void exitLevel() {
    this.level--;
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

  public void write(final Expression expression) {
    if (expression != null) {
      expression.renderTo(this);
    }
  }

  public LiveSQLDialect getSqlDialect() {
    return sqlDialect;
  }

  public LiveSQLPreparedQuery getPreparedQuery() {
    LinkedHashMap<String, Object> p = new LinkedHashMap<String, Object>();
    for (String name : this.params.keySet()) {
      p.put(name, this.params.get(name));
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

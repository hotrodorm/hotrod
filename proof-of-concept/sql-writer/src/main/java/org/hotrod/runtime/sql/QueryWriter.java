package org.hotrod.runtime.sql;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hotrod.runtime.sql.dialects.SQLDialect;
import org.hotrod.runtime.sql.expressions.Expression;

public class QueryWriter {

  private SQLDialect sqlDialect;

  private AtomicLong n;

  private StringBuilder sb;
  private LinkedHashMap<String, Object> params;

  public QueryWriter(final SQLDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
    this.n = new AtomicLong();
    this.sb = new StringBuilder();
    this.params = new LinkedHashMap<String, Object>();
  }

  public String registerParameter(final Object value) {
    String name = "p" + this.n.incrementAndGet();
    this.params.put(name, value);
    return name;
  }

  public void write(final String txt) {
    if (txt != null) {
      this.sb.append(txt);
    }
  }

  public void write(final Expression<?> expression) {
    if (expression != null) {
      expression.renderTo(this);
    }
  }

  public SQLDialect getSqlDialect() {
    return sqlDialect;
  }

  public PreparedQuery getPreparedQuery() {
    LinkedHashMap<String, Object> p = new LinkedHashMap<String, Object>();
    for (String name : this.params.keySet()) {
      p.put(name, this.params.get(name));
    }
    return new PreparedQuery(this.sb.toString(), p);
  }

  // Prepared Query

  public static class PreparedQuery {

    private String sql;
    private LinkedHashMap<String, Object> parameters;

    public PreparedQuery(final String sql, final LinkedHashMap<String, Object> parameters) {
      this.sql = sql;
      this.parameters = parameters;
    }

    public String getSQL() {
      return sql;
    }

    public LinkedHashMap<String, Object> getParameters() {
      return parameters;
    }

  }

}

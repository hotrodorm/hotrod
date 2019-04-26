package sql;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

import sql.predicates.Expression;

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

  public void write(final Expression expr) {
    if (expr != null) {
      expr.renderTo(this);
    }
  }

  public SQLDialect getSqlDialect() {
    return sqlDialect;
  }

  public String getSQL() {
    return this.sb.toString();
  }

  public LinkedHashMap<String, Object> getParameters() {
    return this.params;
  }

}

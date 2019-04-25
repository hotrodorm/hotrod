package sql;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PreparedQuery {

  private String sql;
  private LinkedHashMap<String, Object> params;

  private QueryWriter w;
  private AtomicLong n;

  public PreparedQuery(final QueryWriter w) {
    this.sql = null;
    this.params = new LinkedHashMap<String, Object>();
    this.w = w;
    this.n = new AtomicLong();
  }

  public String registerParameter(final Object value) {
    String name = "p" + this.n.incrementAndGet();
    this.params.put(name, value);
    return name;
  }

  public QueryWriter getWriter() {
    return this.w;
  }

  public void close() {
    this.sql = this.w.render();
  }

  public String getSql() {
    return sql;
  }

  public LinkedHashMap<String, Object> getParameters() {
    return params;
  }

  
}

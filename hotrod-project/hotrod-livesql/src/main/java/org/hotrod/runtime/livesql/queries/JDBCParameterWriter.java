package org.hotrod.runtime.livesql.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class JDBCParameterWriter implements SQLParameterWriter {

  private AtomicLong n;
  private List<QueryParameter> params;

  public JDBCParameterWriter() {
    this.n = new AtomicLong();
    this.params = new ArrayList<>();
  }

  @Override
  public RenderedParameter registerParameter(Object value) {
    String name = "" + this.n.incrementAndGet();
    this.params.add(new QueryParameter(name, value));
    return new RenderedParameter(name, "?");
  }

  @Override
  public List<QueryParameter> getParameters() {
    return params;
  }

}

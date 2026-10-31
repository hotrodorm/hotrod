package org.hotrod.livesql.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.hotrod.converter.TypeConverter;

public class JDBCParameterWriter implements SQLParameterWriter {

  private AtomicLong n;
  private List<QueryParameter<?, ?>> params;

  public JDBCParameterWriter() {
    this.n = new AtomicLong();
    this.params = new ArrayList<>();
  }

  @Override
  public <R> RenderedParameter registerParameter(Object value, TypeConverter<R, Object> converter) {
    String name = "" + this.n.incrementAndGet();
    this.params.add(QueryParameter.of(name, value, converter));
    return new RenderedParameter(name, "?");
  }

  @Override
  public List<QueryParameter<?, ?>> getParameters() {
    return params;
  }

}

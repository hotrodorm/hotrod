package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.util.List;

import org.hotrod.converter.TypeConverter;

public interface SQLParameterWriter {

  public class QueryParameter<R, D> {

    private String name;
    private Object domain;
    private TypeConverter<R, Object> converter;

    private QueryParameter(String name, Object domain, TypeConverter<R, Object> converter) {
      this.name = name;
      this.domain = domain;
      this.converter = converter;
    }

    public static <R> QueryParameter<R, Object> of(String name, Object domain) {
      return new QueryParameter<>(name, domain, null);
    }

    public static <R> QueryParameter<R, Object> of(String name, Object domain, TypeConverter<R, Object> converter) {
      return new QueryParameter<>(name, domain, converter);
    }

    public String getName() {
      return name;
    }

    public Object getValue(Connection conn) {
      if (converter == null) {
        return domain;
      } else {
        return converter.encode(domain, conn);
      }
    }

    public Object getDomainValue() {
      return domain;
    }

    public TypeConverter<R, Object> getConverter() {
      return converter;
    }

    void setValue(Object domain) {
      this.domain = domain;
    }

  }

  public class RenderedParameter {

    private String name;
    private String placeholder;

    public RenderedParameter(String name, String placeholder) {
      super();
      this.name = name;
      this.placeholder = placeholder;
    }

    public String getName() {
      return name;
    }

    public String getPlaceholder() {
      return placeholder;
    }

  }

  // 1. Record the parameter in the internal list
  // 2. Return the parameter name: p4
  // 3. Return the rendering of it: #{p4} , ?
  public <R> RenderedParameter registerParameter(Object value, TypeConverter<R, Object> converter);

  public List<QueryParameter<?, ?>> getParameters();

}

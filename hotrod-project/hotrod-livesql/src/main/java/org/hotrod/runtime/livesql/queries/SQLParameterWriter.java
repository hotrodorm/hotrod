package org.hotrod.runtime.livesql.queries;

import java.util.List;

public interface SQLParameterWriter {

  public class QueryParameter {

    private String name;
    private Object value;

    public QueryParameter(String name, Object value) {
      super();
      this.name = name;
      this.value = value;
    }

    public String getName() {
      return name;
    }

    public Object getValue() {
      return value;
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
  public RenderedParameter registerParameter(final Object value);

  public List<QueryParameter> getParameters();

}

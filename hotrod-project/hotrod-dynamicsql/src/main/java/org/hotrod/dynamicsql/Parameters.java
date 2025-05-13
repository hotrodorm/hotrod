package org.hotrod.dynamicsql;

import java.util.HashMap;
import java.util.Map;

public abstract class Parameters {

  protected Map<String, Object> params = new HashMap<>();

  public void add(String key, Object value) {
    this.params.put(key, value);
  }

  protected abstract boolean hasParameter(String name);

  protected abstract Object getParameterValue(String name);

  protected abstract void bind(String name, Object obj) throws DynamicExpressionException;

  protected abstract Object unbind(String name);

}

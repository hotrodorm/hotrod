package org.hotrod.dynamic;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterContext {

  protected Map<String, Object> params = new HashMap<>();

  public void add(String key, Object value) {
    this.params.put(key, value);
  }

  public abstract boolean hasParameter(String name);

  public abstract Object getParameterValue(String name);

}

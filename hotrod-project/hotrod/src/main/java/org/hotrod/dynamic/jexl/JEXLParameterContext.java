package org.hotrod.dynamic.jexl;

import org.apache.commons.jexl3.JexlContext;
import org.hotrod.dynamic.ParameterContext;

public class JEXLParameterContext extends ParameterContext implements JexlContext {

  @Override
  public Object get(String name) {
    return super.params.get(name);
  }

  @Override
  public void set(String name, Object value) {
    throw new UnsupportedOperationException("The parameter context cannot be modified");
  }

  @Override
  public boolean has(String name) {
    return super.params.containsKey(name);
  }

  // ParameterContext

  @Override
  public boolean hasParameter(String name) {
    return this.has(name);
  }

  @Override
  public Object getParameterValue(String name) {
    return this.get(name);
  }

}

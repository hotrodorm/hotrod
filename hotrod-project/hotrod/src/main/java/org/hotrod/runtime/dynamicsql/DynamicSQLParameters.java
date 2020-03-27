package org.hotrod.runtime.dynamicsql;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl3.JexlContext;

public class DynamicSQLParameters implements JexlContext {

  private Map<String, Object> vars = new HashMap<String, Object>();

  @Override
  public Object get(final String name) {
    return this.vars.get(name);
  }

  @Override
  public boolean has(final String name) {
    return this.vars.containsKey(name);
  }

  @Override
  public void set(final String name, final Object value) {
    this.vars.put(name, value);
  }

  public void remove(final String name) {
    this.vars.remove(name);
  }

}

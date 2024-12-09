package org.hotrod.dynamic.jexl;

import java.util.logging.Logger;

import org.apache.commons.jexl3.JexlContext;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;

public class JEXLParameterContext extends ParameterContext implements JexlContext {

  private static final Logger log = Logger.getLogger(PreparedQuery.class.getName());

  @Override
  public Object get(String name) {
    log.info("## get('" + name + "')");
    return super.params.get(name);
  }

  @Override
  public void set(String name, Object value) {
    log.info("## set('" + name + "')");
    throw new UnsupportedOperationException("The parameter context cannot be modified");
  }

  @Override
  public boolean has(String name) {
    log.info("## has('" + name + "')");
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

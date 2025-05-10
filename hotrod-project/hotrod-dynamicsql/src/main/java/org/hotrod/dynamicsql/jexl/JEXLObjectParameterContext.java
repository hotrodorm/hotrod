package org.hotrod.dynamicsql.jexl;

import java.util.logging.Logger;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.ObjectContext;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Parameters;

public class JEXLObjectParameterContext extends Parameters implements JexlContext {

  private static final Logger log = Logger.getLogger(JEXLObjectParameterContext.class.getName());

  private ObjectContext<?> context;

  public static JEXLObjectParameterContext of(ObjectContext<?> c) {
    return new JEXLObjectParameterContext(c);
  }

  private JEXLObjectParameterContext(ObjectContext<?> context) {
    super();
    this.context = context;
  }

  @Override
  public Object get(String name) {
    Object v = this.context.get(name);
    return v;
  }

  @Override
  public boolean has(String name) {
    return this.context.has(name);
  }

  @Override
  public void set(String name, Object value) {
    throw new UnsupportedOperationException("Setting values is not supported in plain objects.");
  }

  @Override
  public boolean hasParameter(String name) {
    return this.context.has(name);
  }

  @Override
  public Object getParameterValue(String name) {
    return this.context.get(name);
  }

  @Override
  public void bind(String name, Object obj) throws DynamicExpressionException {
    throw new UnsupportedOperationException("Binding is not supported in plain objects.");
  }

  @Override
  public Object unbind(String name) {
    throw new UnsupportedOperationException("Unbinding is not supported in plain objects.");
  }

}

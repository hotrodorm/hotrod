package org.hotrod.dynamicsql.jexl;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.jexl3.JexlContext;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public class JEXLParameterContext extends ParameterContext implements JexlContext {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(JEXLParameterContext.class.getName());

  private static final Pattern VALID_SIMPLE_NAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");

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

  @Override
  public void bind(String name, Object value) throws DynamicExpressionException {
    if (!VALID_SIMPLE_NAME.matcher(name).matches()) {
      throw new RuntimeException(
          "Invalid variable name '" + name + "': must a letter followed by alphanumeric characters or underscores.");
    }
    super.params.put(name, value);
  }

  @Override
  public Object unbind(String name) {
    return super.params.remove(name);
  }

}

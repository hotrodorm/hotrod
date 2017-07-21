package org.hotrod.config.dynamicsql;

import org.apache.log4j.Logger;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.VariableExpression;

public class VariableOccurrence implements SQLSegment {

  // Constants

  private static final Logger log = Logger.getLogger(VariableOccurrence.class);

  // Properties

  private String name;

  // Constructor

  public VariableOccurrence(final String name) {
    log.debug("init");
    this.name = name;
  }

  // Behavior

  @Override
  public boolean isEmpty() {
    return false;
  }

  // Rendering

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.name;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    return this.name;
  }

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    return new VariableExpression(this.name);
  }

}

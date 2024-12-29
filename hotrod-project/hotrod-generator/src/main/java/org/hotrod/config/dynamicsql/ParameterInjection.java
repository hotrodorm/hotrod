package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.existing.expressions.OldDynamicExpression;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidJavaExpressionException;
import org.hotrod.generator.ParameterRenderer;

public class ParameterInjection implements SQLSegment {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ParameterInjection.class.getName());

  // Properties

  private String name;

  // Constructor

  // Java Parameter
  public ParameterInjection(final String name) throws InvalidConfigurationFileException {
    log.fine("init");
    this.name = name;
  }

  // Setters

  // Getters

  public String getName() {
    return name;
  }

  @Override
  public boolean isEmpty() {
    return this.name.isEmpty();
  }

  @Override
  public String renderStatic(ParameterRenderer parameterRenderer) {
    return "${" + this.name + "}";
  }

  @Override
  public String renderXML(ParameterRenderer parameterRenderer) {
    return "${" + this.name + "}";
  }

  @Override
  public OldDynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    return null;
  }

}

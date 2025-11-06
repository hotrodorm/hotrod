package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import org.hotrod.generator.ParameterRenderer;

public class VariableOccurrence implements SQLSegment {

  // Constants

  private static final Logger log = Logger.getLogger(VariableOccurrence.class.getName());

  // Properties

  private String name;

  // Constructor

  public VariableOccurrence(final String name) {
    log.fine("init");
    this.name = name;
  }

  // Behavior

  @Override
  public boolean isEmpty() {
    return false;
  }

  // Rendering

  @Override
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return "?";
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.name;
  }

  public String getName() {
    return name;
  }

}

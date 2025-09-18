package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class ParameterInjection implements SQLSegment {

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
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return "";
  }

  @Override
  public String renderStatic(ParameterRenderer parameterRenderer) {
    return "$INJECT{" + this.name + "}";
  }

}

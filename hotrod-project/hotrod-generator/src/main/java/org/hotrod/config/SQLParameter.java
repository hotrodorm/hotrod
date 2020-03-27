package org.hotrod.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.dynamicsql.expressions.VariableExpression;
import org.hotrod.utils.identifiers.Id;

public class SQLParameter implements SQLSegment {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SQLParameter.class);

  private static final String VALID_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  public static final String PREFIX = "#{";
  public static final String SUFFIX = "}";

  private static enum VariableType {
    PARAMETER_REFERENCE, //
    VARIABLE
  };

  // Properties

  private String name;
  private VariableType type;
  private ParameterTag definition;

  private Id id;

  // Constructor

  // Java Parameter
  public SQLParameter(final String name, final AbstractConfigurationTag tag, final boolean isVariable)
      throws InvalidConfigurationFileException {
    log.debug("init");
    initialize(name, tag, false);
  }

  private void initialize(final String name, final AbstractConfigurationTag tag, final boolean isVariable)
      throws InvalidConfigurationFileException {

    this.name = name;
    this.type = isVariable ? VariableType.VARIABLE : VariableType.PARAMETER_REFERENCE;
    this.definition = null;

    // name

    if (!this.name.matches(VALID_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(tag, //
          "Invalid parameter name '" + this.name + "'. Must start with a letter and "
              + "continue with letters, digits, and/or underscores", //
          "Invalid parameter name '" + this.name + "'. Must start with a letter and "
              + "continue with letters, digits, and/or underscores.");
    }

    try {
      this.id = Id.fromJavaMember(this.name);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid parameter name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(tag, msg, msg);
    }

  }

  public String getErrorMessage(final String paramDefinition, final String tagIdentification,
      final String extraMessage) {
    return "Invalid parameter " + PREFIX + paramDefinition + SUFFIX + " in SQL query on the tag " + tagIdentification
        + (extraMessage == null ? "." : ":\n" + extraMessage);
  }

  // Setters

  public void setDefinition(final ParameterTag definition) {
    this.definition = definition;
  }

  // Getters

  public String getName() {
    return name;
  }

  public Id getId() {
    return id;
  }

  public boolean isVariable() {
    return this.type == VariableType.VARIABLE;
  }

  public boolean isParameterReference() {
    return this.type == VariableType.PARAMETER_REFERENCE;
  }

  public String getJavaType() {
    return this.definition.getJavaType();
  }

  public String getJdbcType() {
    return this.definition.getJdbcType();
  }

  // Behavior

  @Override
  public boolean isEmpty() {
    return false;
  }

  // Rendering

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    String r = parameterRenderer.render(this);
    return r;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    return parameterRenderer.render(this);
  }

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    if (this.isVariable()) {
      return new VariableExpression(this.name);
    } else {
      return new LiteralExpression(parameterRenderer.render(this));
    }
  }

}

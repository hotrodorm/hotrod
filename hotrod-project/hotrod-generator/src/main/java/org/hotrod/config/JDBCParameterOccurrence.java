package org.hotrod.config;

import java.util.logging.Logger;

import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.identifiers.Id;

public class JDBCParameterOccurrence implements SQLSegment {

  // Constants

  private static final Logger log = Logger.getLogger(JDBCParameterOccurrence.class.getName());

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
  public JDBCParameterOccurrence(final String name, final AbstractConfigurationTag tag, final boolean isVariable)
      throws InvalidConfigurationFileException {
    log.fine("init");
    initialize(name, tag, false);
  }

  private void initialize(final String name, final AbstractConfigurationTag tag, final boolean isVariable)
      throws InvalidConfigurationFileException {

    this.name = name;
    this.type = isVariable ? VariableType.VARIABLE : VariableType.PARAMETER_REFERENCE;
    this.definition = null;

    // name

    if (!this.name.matches(VALID_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(tag, "Invalid parameter name '" + this.name
          + "'. Must start with a letter and " + "continue with letters, digits, and/or underscores.");
    }

    try {
      this.id = Id.fromJavaMember(this.name);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid parameter name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(tag, msg);
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

  public ParameterTag getDefinition() {
    return definition;
  }

  public String getJavaType() {
    return this.definition.getType();
  }

  public String getJdbcType() {
    return this.definition.getJDBCType().getShortTypeName();
  }

  public boolean isInternal() {
    return this.getDefinition().isInternal();
  }

  // Behavior

  @Override
  public boolean isEmpty() {
    return false;
  }

  // Rendering

  @Override
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return parameterRenderer.render(this);
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    String r = parameterRenderer.render(this);
    return r;
  }

}

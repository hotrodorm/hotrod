package org.hotrod.config;

import org.hotrod.config.dynamicsql.SQLChunk;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.VariableExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.JdbcTypes;

public class SQLParameter implements SQLChunk {

  // Constants

  private static final String VALID_PARAM_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";
  private static final String JAVA_TYPE_PREFIX = "javaType=";
  private static final String JDBC_TYPE_PREFIX = "jdbcType=";

  public static final String PREFIX = "#{";
  public static final String SUFFIX = "}";

  private static enum VariableType {
    PARAMETER_DEFINITION, //
    PARAMETER_REFERENCE, //
    VARIABLE
  };

  // Properties

  @SuppressWarnings("unused")
  private String paramDefinition;

  private String name;
  private VariableType type;
  private SQLParameter definition;
  private String javaType;
  private String jdbcType;
  private boolean inTag;

  // Constructor

  // Java Parameter
  public SQLParameter(final String paramDefinition, final String tagIdentification, final boolean inTag)
      throws InvalidConfigurationFileException {
    initialize(paramDefinition, tagIdentification, inTag, false);
  }

  // Variable
  public SQLParameter(final String paramDefinition, final String tagIdentification)
      throws InvalidConfigurationFileException {
    initialize(paramDefinition, tagIdentification, true, true);
  }

  private void initialize(final String paramDefinition, final String tagIdentification, final boolean inTag,
      boolean isVariable) throws InvalidConfigurationFileException {
    this.paramDefinition = paramDefinition;

    String[] parts = paramDefinition.split(",");
    if (parts == null || !(parts.length == 1 || parts.length == 3)) {
      throw new InvalidConfigurationFileException(
          getErrorMessage(paramDefinition, tagIdentification, "the parameter must take the form " + PREFIX
              + "name,javaType=<JAVA-TYPE>,jdbcType=<JDBC-TYPE>" + SUFFIX + "."));
    }

    this.type = isVariable ? VariableType.VARIABLE
        : (parts.length == 3 ? VariableType.PARAMETER_DEFINITION : VariableType.PARAMETER_REFERENCE);

    this.definition = null;
    this.inTag = inTag;

    // name

    this.name = parts[0].trim();
    if (!this.name.matches(VALID_PARAM_PATTERN)) {
      throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
          "must start with a letter and " + "continue with letters digits or underscore."));
    }

    if (this.type == VariableType.PARAMETER_DEFINITION) {

      // javaType

      String p1 = parts[1].trim();
      if (!p1.startsWith(JAVA_TYPE_PREFIX)) {
        throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
            "the second parameter must take the form " + "'javaType=<JAVA-CLASS>'."));
      }
      this.javaType = p1.substring(JAVA_TYPE_PREFIX.length());
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
            "the second parameter must take the form " + "'javaType=<JAVA-CLASS>'."));
      }

      // jdbcType

      String p2 = parts[2].trim();
      if (!p2.startsWith(JDBC_TYPE_PREFIX)) {
        throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
            "the third parameter must take the form " + "'jdbcType=<JDBC-TYPE>'."));
      }
      this.jdbcType = p2.substring(JDBC_TYPE_PREFIX.length());
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
            "the third parameter must take the form " + "'jdbcType=<JDBC-TYPE>'."));
      }
      Integer jdbcCode = JdbcTypes.nameToCode(this.jdbcType);
      if (jdbcCode == null) {
        throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
            "the third parameter must be a JDBC type as defined in the class java.sql.Types."));
      }

    } else {
      this.javaType = null;
      this.jdbcType = null;
    }
  }

  public String getErrorMessage(final String paramDefinition, final String tagIdentification,
      final String extraMessage) {
    return "Invalid parameter " + PREFIX + paramDefinition + SUFFIX + " in SQL query on the tag " + tagIdentification
        + (extraMessage == null ? "." : ":\n" + extraMessage);
  }

  // Setters

  public void setDefinition(final SQLParameter definition) {
    this.definition = definition;
  }

  // Getters

  public String getName() {
    return name;
  }

  public boolean isDefinition() {
    return this.type == VariableType.PARAMETER_DEFINITION;
  }

  public boolean isVariable() {
    return this.type == VariableType.VARIABLE;
  }

  public boolean isParameterReference() {
    return this.type == VariableType.PARAMETER_REFERENCE;
  }

  public SQLParameter getDefinition() {
    return this.definition;
  }

  public String getJavaType() {
    return javaType;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public boolean isInTag() {
    return inTag;
  }

  // Behavior

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return parameterRenderer.render(this);
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

  @Override
  public boolean isEmpty() {
    return false;
  }

}

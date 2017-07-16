package org.hotrod.config;

import org.hotrod.config.dynamicsql.SQLChunk;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.JdbcTypes;

public class SQLParameter implements SQLChunk {

  private static final String VALID_PARAM_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";
  private static final String JAVA_TYPE_PREFIX = "javaType=";
  private static final String JDBC_TYPE_PREFIX = "jdbcType=";

  public static final String PREFIX = "#{";
  public static final String SUFFIX = "}";

  @SuppressWarnings("unused")
  private String paramDefinition;

  private String name;
  private boolean isDefinition;
  private SQLParameter definition;
  private String javaType;
  private String jdbcType;
  private boolean inTag;

  public SQLParameter(final String paramDefinition, final String tagIdentification, final boolean inTag)
      throws InvalidConfigurationFileException {

    this.paramDefinition = paramDefinition;

    String[] parts = paramDefinition.split(",");
    if (parts == null || !(parts.length == 1 || parts.length == 3)) {
      throw new InvalidConfigurationFileException(
          getErrorMessage(paramDefinition, tagIdentification, "the parameter must take the form " + PREFIX
              + "name,javaType=<JAVA-TYPE>,jdbcType=<JDBC-TYPE>" + SUFFIX + "."));
    }

    this.isDefinition = parts.length == 3;
    this.definition = null;
    this.inTag = inTag;

    // name

    this.name = parts[0].trim();
    if (!this.name.matches(VALID_PARAM_PATTERN)) {
      throw new InvalidConfigurationFileException(getErrorMessage(paramDefinition, tagIdentification,
          "must start with a letter and " + "continue with letters digits or underscore."));
    }

    if (this.isDefinition) {

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
    return isDefinition;
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
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return parameterRenderer.render(this);
  }

}

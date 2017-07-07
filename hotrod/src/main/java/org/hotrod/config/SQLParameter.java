package org.hotrod.config;

import org.hotrod.config.sql.AbstractSQLSection;
import org.hotrod.config.sql.SQLChunk;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.SUtils;

public class SQLParameter implements SQLChunk {

  private static final String VALID_PARAM_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";
  private static final String JAVA_TYPE_PREFIX = "javaType=";
  private static final String JDBC_TYPE_PREFIX = "jdbcType=";

  private String augmentedSQL;
  private String name;
  private boolean isDefinition;
  private SQLParameter definition;
  private String javaType;
  private String jdbcType;
  private boolean inTag;

  public SQLParameter(final String augmentedSQL, final AbstractSQLDAOTag tag, final AbstractSQLSection sps, final boolean inTag,
      final String attName, final String name) throws InvalidConfigurationFileException {

    this.augmentedSQL = augmentedSQL;

    String[] parts = augmentedSQL.split(",");
    if (parts == null || !(parts.length == 1 || parts.length == 3)) {
      throw new InvalidConfigurationFileException(sps.getErrorMessage(augmentedSQL, tag, attName, name));
    }

    this.isDefinition = parts.length == 3;
    this.definition = null;
    this.inTag = inTag;

    // name

    this.name = parts[0].trim();
    if (!this.name.matches(VALID_PARAM_PATTERN)) {
      throw new InvalidConfigurationFileException(
          sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter name '" + this.name
              + "': must start with a letter and " + "continue with letters digits or underscore."));
    }

    if (this.isDefinition) {

      // javaType

      String p1 = parts[1].trim();
      if (!p1.startsWith(JAVA_TYPE_PREFIX)) {
        throw new InvalidConfigurationFileException(
            sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter javaType '" + this.name
                + "': the second parameter must take the form " + "'javaType=<JAVA-CLASS>'."));
      }
      this.javaType = p1.substring(JAVA_TYPE_PREFIX.length());
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(
            sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter javaType '" + this.name
                + "': the second parameter must take the form " + "'javaType=<JAVA-CLASS>'."));
      }

      // jdbcType

      String p2 = parts[2].trim();
      if (!p2.startsWith(JDBC_TYPE_PREFIX)) {
        throw new InvalidConfigurationFileException(
            sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter jdbcType '" + this.name
                + "': the third parameter must take the form " + "'jdbcType=<JDBC-TYPE>'."));
      }
      this.jdbcType = p2.substring(JDBC_TYPE_PREFIX.length());
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(
            sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter jdbcType '" + this.name
                + "': the third parameter must take the form " + "'jdbcType=<JDBC-TYPE>'."));
      }
      Integer jdbcCode = JdbcTypes.nameToCode(this.jdbcType);
      if (jdbcCode == null) {
        throw new InvalidConfigurationFileException(
            sps.getErrorMessage(augmentedSQL, tag, attName, name, "invalid parameter jdbcType '" + this.name
                + "': the third parameter must be a JDBC type " + "as in the class java.sql.Types."));
      }

    } else {
      this.javaType = null;
      this.jdbcType = null;
    }

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

  @Override
  public String renderAugmentedSQL() {
    return "#{" + this.augmentedSQL + "}";
  }

}

package org.hotrod.config;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class QueryTag extends AbstractSQLDAOTag {

  private static final Logger log = Logger.getLogger(QueryTag.class);

  private static final String TAG_NAME = "query";

  protected String javaMethodName = null;

  public void validate() throws InvalidConfigurationFileException {

    // method-name

    if (this.javaMethodName == null) {
      throw new InvalidConfigurationFileException("Attribute 'java-method-name' of tag <" + getTagName()
          + "> cannot be empty. " + "Must specify a unique name.");
    } else if (!this.javaMethodName.matches(SequenceTag.VALID_JAVA_METHOD_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-method-name' of tag <" + TAG_NAME + "> specifies '"
          + this.javaMethodName + "' but must specify a valid java method name. "
          + "Valid method names must start with a lowercase letter, "
          + "and continue with letters, digits, dollarsign, and/or underscores.");
    }

    super.validateCore("java-method-name", this.javaMethodName);
  }

  @Override
  protected String getTagName() {
    return TAG_NAME;
  }

  // Setters (digester)

  public void setJavaMethodName(String javaMethodName) {
    this.javaMethodName = javaMethodName;
  }

  // Getters

  public String getJavaMethodName() {
    return javaMethodName;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier("a", this.javaMethodName);
  }

  // Rendering

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    log.debug("this.sections.size()=" + this.sections.size());
    for (SQLSection s : this.sections) {
      sb.append(s.renderSQLSentence(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public ClassPackage getPackage() {
    // Unused
    return null;
  }

  @Override
  public String getJavaClassName() {
    // Unused
    return null;
  }

}

package org.hotrod.config;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;

public class CustomDAOTag extends AbstractCompositeDAOTag {

  private static final Logger log = Logger.getLogger(CustomDAOTag.class);

  static final String TAG_NAME = "dao";
  private static final String VALID_JAVA_CLASS_PATTERN = "[A-Z][a-zA-Z0-9_$]*";

  private String javaClassName = null;

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  public void validate(final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig)
      throws InvalidConfigurationFileException {
    log.debug("validate");

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    // java-class-name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class-name' of tag <" + TAG_NAME
          + "> cannot be empty. " + "You must specify a dao java class name.");
    }
    if (!this.javaClassName.matches(VALID_JAVA_CLASS_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class-name' of tag <" + TAG_NAME + "> with value '"
          + this.javaClassName + "' must be a valid java class name. "
          + "Valid java class names start with an uppercase letter and continue with "
          + "letters, digits, dollar signs, and/or underscores.");
    }

    // sequences and updates

    super.validate(TAG_NAME, "java-class-name", this.javaClassName);

  }

  // Setters (digester)

  public void setJavaClassName(final String javaClassName) {
    this.javaClassName = javaClassName;
  }

  // Getters

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.daosTag.getDaoPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    return this.javaClassName;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

}

package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;

@XmlRootElement(name = "dao")
public class CustomDAOTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(CustomDAOTag.class);

  private static final String VALID_JAVA_CLASS_PATTERN = "[A-Z][a-zA-Z0-9_$]*";

  // Properties

  private String javaClassName = null;

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor

  public CustomDAOTag() {
    super("dao");
  }

  // JAXB Setters

  @XmlAttribute(name = "java-class-name")
  public void setJavaClassName(final String javaClassName) {
    this.javaClassName = javaClassName;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig)
      throws InvalidConfigurationFileException {
    log.debug("validate");

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    // java-class-name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'java-class-name' of tag <"
          + super.getTagName() + "> cannot be empty. " + "You must specify a dao java class name.");
    }
    if (!this.javaClassName.matches(VALID_JAVA_CLASS_PATTERN)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'java-class-name' of tag <" + super.getTagName() + "> must be a valid java class name, but found '"
              + this.javaClassName
              + "'. "
              + "Valid java class names start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores.");
    }

    // sequences and updates

    super.validate(super.getTagName(), "java-class-name", this.javaClassName);

  }

  // Getters

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

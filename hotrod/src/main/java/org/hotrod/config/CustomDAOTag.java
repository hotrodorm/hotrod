package org.hotrod.config;

import java.sql.Connection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;

@XmlRootElement(name = "dao")
public class CustomDAOTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(CustomDAOTag.class);

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

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {
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
    if (!this.javaClassName.matches(Patterns.VALID_JAVA_CLASS)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'java-class-name' of tag <" + super.getTagName() + "> must be a valid java class name, but found '"
              + this.javaClassName + "'. " + "Valid java class names start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores.");
    }

    // sequences, queries, selects

    super.validate(daosTag, config, fragmentConfig);

  }

  public void validateAgainstDatabase(final HotRodGenerator generator, final Connection conn)
      throws InvalidConfigurationFileException {

    // TODO: add validations to <select> methods

    for (SelectMethodTag s : this.getSelects()) {
      // s.va

    }

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

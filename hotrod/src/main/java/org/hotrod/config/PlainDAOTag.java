package org.hotrod.config;

import java.sql.Connection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;

// TODO: rename to ExecutorDAOTag

@XmlRootElement(name = "dao")
public class PlainDAOTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(PlainDAOTag.class);

  // Properties

  private String javaClassName = null;

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor

  public PlainDAOTag() {
    super("dao");
  }

  // JAXB Setters

  @XmlAttribute(name = "class")
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

    // class

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'class' of tag <"
          + super.getTagName() + "> cannot be empty. " + "You must specify a dao java class name.");
    }
    if (!this.javaClassName.matches(Patterns.VALID_JAVA_CLASS)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'class' of tag <" + super.getTagName() + "> must be a valid java class name, but found '"
              + this.javaClassName + "'. " + "Valid java class names start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores.");
    }

    // sequences, queries, selects

    super.validate(daosTag, config, fragmentConfig);

  }

  public void validateAgainstDatabase(final HotRodGenerator generator, final Connection conn)
      throws InvalidConfigurationFileException {

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(generator);
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      PlainDAOTag f = (PlainDAOTag) fresh;
      return this.javaClassName.equals(f.javaClassName);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      PlainDAOTag f = (PlainDAOTag) fresh;
      boolean different = !same(fresh);

      this.javaClassName = f.javaClassName;
      this.daosTag = f.daosTag;
      this.fragmentConfig = f.fragmentConfig;
      this.fragmentPackage = f.fragmentPackage;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      PlainDAOTag f = (PlainDAOTag) fresh;
      return Compare.same(this.javaClassName, f.javaClassName);
    } catch (ClassCastException e) {
      return false;
    }
  }

}

package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtils;

@XmlRootElement(name = "dao")
public class ExecutorTag extends AbstractDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ExecutorTag.class);

  // Properties

  private String name = null;

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private String javaClassName = null;

  // Constructor

  public ExecutorTag() {
    super("dao");
  }

  // Duplicate

  public ExecutorTag duplicate() {
    ExecutorTag d = new ExecutorTag();

    d.copyCommon(this);

    d.name = this.name;

    d.daosTag = this.daosTag;
    d.fragmentConfig = this.fragmentConfig;
    d.fragmentPackage = this.fragmentPackage;

    return d;
  }

  // JAXB Setters

  @XmlAttribute(name = "name")
  public void setName(final String name) {
    this.name = name;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    log.debug("validate Table tag: " + daosTag.getInternalCaption());

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a dao name.");
    }
    if (!this.name.matches(Patterns.VALID_JAVA_CLASS)) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid attribute 'class' with value '" + this.name
              + "': mut start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores", //
          "Attribute 'class' of tag <" + super.getTagName() + "> must be a valid java class name, but found '"
              + this.name + "'. " + "Valid java class names start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores.");
    }
    this.javaClassName = daosTag.generateDAOName(this.name);

    // sequences, queries, selects

    super.validate(daosTag, config, fragmentConfig, adapter);

  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {

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
      ExecutorTag f = (ExecutorTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ExecutorTag f = (ExecutorTag) fresh;
      boolean different = !same(fresh);

      this.name = f.name;
      this.daosTag = f.daosTag;
      this.fragmentConfig = f.fragmentConfig;
      this.fragmentPackage = f.fragmentPackage;
      this.javaClassName = f.javaClassName;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ExecutorTag f = (ExecutorTag) fresh;
      return Compare.same(this.name, f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}

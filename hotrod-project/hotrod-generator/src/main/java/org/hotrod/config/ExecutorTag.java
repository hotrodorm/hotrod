package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.ClassPackage;
import org.hotrodorm.hotrod.utils.SUtil;

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
    super("dao", false);
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

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a dao name.");
    }
    if (!this.name.matches(Patterns.VALID_JAVA_CLASS)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'class' of tag <" + super.getTagName() + "> must be a valid java class name, but found '"
              + this.name + "'. " + "Valid java class names start with an uppercase letter and continue with "
              + "letters, digits, dollar signs, and/or underscores.");
    }
    this.javaClassName = daosTag.generateNitroDAOName(this.name);

    // sequences, queries, selects

    super.validate(daosTag, config, fragmentConfig, adapter);

  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(metadata);
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

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}

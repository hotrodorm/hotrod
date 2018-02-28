package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "parameter")
public class ParameterTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterTag.class);

  static final String TAG_NAME = "parameter";

  // Properties

  private String name = null;
  private String javaType = null;
  private String jdbcType = null;

  // Constructor

  public ParameterTag() {
    super("parameter");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaType = javaType;
  }

  @XmlAttribute(name = "jdbc-type")
  public void setJdbcType(final String jdbcType) {
    this.jdbcType = jdbcType;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (this.name == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <parameter> tag -- the 'name' attribute must be specified.");
    }
    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <parameter> tag -- the 'name' attribute cannot be blank.");
    }
    if (!this.name.matches(Patterns.VALID_JAVA_VARIABLE)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid Java parameter name '" + this.name
          + "'. The 'name' attribute must start with a lower case letter and continue with letters, digits, and/or underscores.");
    }

    // java-type

    if (this.javaType == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- the 'java-type' attribute cannot be empty.");
    }
    if (SUtils.isEmpty(this.javaType)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- the 'java-type' attribute cannot be blank.");
    }
    if (!this.javaType.matches(Patterns.VALID_JAVA_TYPE)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- "
              + "the 'java-type' attribute must start with a letter and continue with letters, digits, and/or underscores, but found '"
              + this.javaType + "'.");
    }

    // jdbc-type

    if (this.jdbcType == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- the 'jdbc-type' attribute cannot be empty.");
    }
    if (SUtils.isEmpty(this.jdbcType)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- the 'jdbc-type' attribute cannot be blank.");
    }
    if (!this.jdbcType.matches(Patterns.VALID_JDBC_TYPE)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "invalid <parameter> tag -- "
              + "the 'jdbc-type' attribute must include uppercase letters, digits, and/or underscores, but found '"
              + this.jdbcType + "'.");
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaType() {
    return javaType;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier(this.name);
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      ParameterTag f = (ParameterTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ParameterTag f = (ParameterTag) fresh;
      boolean different = !same(fresh);

      this.javaType = f.javaType;
      this.jdbcType = f.jdbcType;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ParameterTag f = (ParameterTag) fresh;
      return //
      Compare.same(this.name, f.name) && //
          Compare.same(this.javaType, f.javaType) && //
          Compare.same(this.jdbcType, f.jdbcType) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

}

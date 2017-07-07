package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

@XmlRootElement(name = "config")
public class ConfigTag extends AbstractConfigurationTag {

  // Properties

  private String sGenBaseDir = null;
  private String relativeDir = null;
  private String prefix = null;

  private File genBaseDir;

  // Constructor

  public ConfigTag() {
    super("config");
  }

  // JAXB Setters

  @XmlAttribute(name = "gen-base-dir")
  public void setSgenBaseDir(final String sGenBaseDir) {
    this.sGenBaseDir = sGenBaseDir;
  }

  @XmlAttribute(name = "relative-dir")
  public void setRelativeDir(final String relativeDir) {
    this.relativeDir = relativeDir;
  }

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the base dir.");
    }
    this.genBaseDir = new File(this.sGenBaseDir);
    if (!this.genBaseDir.exists()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sGenBaseDir + "' must point to an existing dir.");
    }
    if (!this.genBaseDir.isDirectory()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sGenBaseDir + "' is not a directory.");
    }

    // relative-dir

    if (this.relativeDir == null) {
      throw new InvalidConfigurationFileException(
          "Invalid relative-dir attribute on tag <" + super.getTagName() + ">. Must be a non-empty value");
    }
    if (SUtils.isEmpty(this.relativeDir)) {
      throw new InvalidConfigurationFileException("Invalid relative-dir '" + this.relativeDir
          + "' on attribute 'relative-dir' of tag <" + super.getTagName() + ">. Must be a non-empty value");
    }

    // prefix

    if (this.prefix == null) {
      throw new InvalidConfigurationFileException(
          "Invalid prefix attribute value. " + "Must be specified with a non-empty value.");
    }
    if (SUtils.isEmpty(this.prefix)) {
      throw new InvalidConfigurationFileException(
          "Invalid dao-prefix value '" + this.prefix + "'. Must be specified with a non-empty value..");
    }

  }

  // Getters

  public String getGenBaseDir() {
    return sGenBaseDir;
  }

  public String getRelativeDir() {
    return relativeDir;
  }

  public String getPrefix() {
    return prefix;
  }

}

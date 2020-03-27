package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtils;

@XmlRootElement(name = "config")
public class ConfigTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

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

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'gen-base-dir' cannot be empty", //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the base dir.");
    }
    this.genBaseDir = new File(basedir, this.sGenBaseDir);
    if (!this.genBaseDir.exists()) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'gen-base-dir' directory not found:\n  " + this.genBaseDir.getPath(), //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> with value '" + this.sGenBaseDir
              + "' must point to an existing dir.");
    }
    if (!this.genBaseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'gen-base-dir' must point to a directory, but other type of file was found", //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> with value '" + this.sGenBaseDir
              + "' is not a directory.");
    }

    // relative-dir

    if (this.relativeDir == null) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'relative-dir' cannot be empty", //
          "Invalid 'relative-dir' attribute on tag <" + super.getTagName() + ">. Must be a non-empty value");
    }
    if (SUtils.isEmpty(this.relativeDir)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'relative-dir' cannot be empty", //
          "Invalid relative-dir '" + this.relativeDir + "' on attribute 'relative-dir' of tag <" + super.getTagName()
              + ">. Must be a non-empty value");
    }

    // prefix

    if (this.prefix == null) {
      throw new InvalidConfigurationFileException(this, //
          "'prefix' attribute cannot be empty", //
          "Invalid 'prefix' attribute value. " + "Must be specified with a non-empty value.");
    }
    if (SUtils.isEmpty(this.prefix)) {
      throw new InvalidConfigurationFileException(this, //
          "'dao-prefix' cannot be empty", //
          "Invalid 'dao-prefix' value '" + this.prefix + "'. Must be specified with a non-empty value..");
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ConfigTag f = (ConfigTag) fresh;
      boolean different = !same(fresh);
      this.sGenBaseDir = f.sGenBaseDir;
      this.relativeDir = f.relativeDir;
      this.prefix = f.prefix;
      this.genBaseDir = f.genBaseDir;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ConfigTag f = (ConfigTag) fresh;
      return //
      Compare.same(this.sGenBaseDir, f.sGenBaseDir) && //
          Compare.same(this.relativeDir, f.relativeDir) && //
          Compare.same(this.prefix, f.prefix);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

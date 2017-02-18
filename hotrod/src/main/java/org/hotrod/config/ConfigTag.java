package org.hotrod.config;

import java.io.File;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class ConfigTag {

  public static final String TAG_NAME = "config";

  private String sGenBaseDir = null;
  private String relativeDir = null;
  private String prefix = null;

  private File genBaseDir;

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> cannot be empty. " + "Must specify the base dir.");
    }
    this.genBaseDir = new File(this.sGenBaseDir);
    if (!this.genBaseDir.exists()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' must point to an existing dir.");
    }
    if (!this.genBaseDir.isDirectory()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' is not a directory.");
    }

    // relative-dir

    if (this.relativeDir == null) {
      throw new InvalidConfigurationFileException(
          "Invalid relative-dir attribute on tag <" + TAG_NAME + ">. Must be a non-empty value");
    }
    if (SUtils.isEmpty(this.relativeDir)) {
      throw new InvalidConfigurationFileException("Invalid relative-dir '" + this.relativeDir
          + "' on attribute 'relative-dir' of tag <" + TAG_NAME + ">. Must be a non-empty value");
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

  // Setters (digester)

  public void setSgenBaseDir(final String sGenBaseDir) {
    this.sGenBaseDir = sGenBaseDir;
  }

  public void setRelativeDir(final String relativeDir) {
    this.relativeDir = relativeDir;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
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

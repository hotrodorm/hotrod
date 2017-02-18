package org.hotrod.config;

import java.io.File;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class MappersTag {

  private static final String PRIMITIVES_MAPPERS_DIR = "primitives";
  private static final String CUSTOM_MAPPERS_DIR = "custom";

  private static final String TAG_NAME = "mappers";

  private String sGenBaseDir = null;
  private String sRelativeDir = null;

  private File baseDir;
  private File fullDir;

  private File primitivesDir;
  private File customDir;
  private boolean primitivesDirVerified = false;
  private boolean customDirVerified = false;

  // Validate

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'gen-base-dir' of tag <"
              + TAG_NAME
              + "> cannot be empty. "
              + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.baseDir = new File(this.sGenBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(
          "Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
              + this.sGenBaseDir + "' points to a non existent directory.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(
          "Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
              + this.sGenBaseDir + "' points to an file system entry "
              + "that is not a directory. ");
    }

    // relative-dir

    if (SUtils.isEmpty(this.sRelativeDir)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'base-dir' of tag <"
              + TAG_NAME
              + "> cannot be empty. "
              + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.fullDir = new File(this.baseDir, this.sRelativeDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(
          "Attribute 'base-dir' of tag <" + TAG_NAME + "> with value '"
              + this.sRelativeDir + "' points to a non existent directory.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(
          "Attribute 'base-dir' of tag <" + TAG_NAME + "> with value '"
              + this.sRelativeDir + "' points to an file system entry "
              + "that is not a directory. ");
    }

    this.primitivesDir = new File(this.fullDir, PRIMITIVES_MAPPERS_DIR);
    this.customDir = new File(this.fullDir, CUSTOM_MAPPERS_DIR);

  }

  // Setters (digester)

  public void setGenBaseDir(final String genBaseDir) {
    this.sGenBaseDir = genBaseDir;
  }

  public void setRelativeDir(final String sRelativeDir) {
    this.sRelativeDir = sRelativeDir;
  }

  // Getters

  public File getPrimitivesDir() {
    if (!this.primitivesDirVerified) {
      if (!this.primitivesDir.exists()) {
        this.primitivesDir.mkdirs();
      }
      this.primitivesDirVerified = true;
    }
    return primitivesDir;
  }

  public File getCustomDir() {
    if (!this.customDirVerified) {
      if (!this.customDir.exists()) {
        this.customDir.mkdirs();
      }
      this.customDirVerified = true;
    }
    return customDir;
  }

  public String getRelativeCustomDir() {
    return sRelativeDir + "/" + CUSTOM_MAPPERS_DIR;
  }

  public String getRelativePrimitivesDir() {
    return sRelativeDir + "/" + PRIMITIVES_MAPPERS_DIR;
  }

}

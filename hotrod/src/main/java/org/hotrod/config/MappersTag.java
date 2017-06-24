package org.hotrod.config;

import java.io.File;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;

public class MappersTag {

  private static final String PRIMITIVES_MAPPERS_DIR = "primitives";
  private static final String CUSTOM_MAPPERS_DIR = "custom";

  private static final String TAG_NAME = "mappers";

  private String sGenBaseDir = null;
  private String sRelativeDir = null;

  private File baseDir;
  private File fullRelativeDir;

  private File customDir;
  private boolean customDirVerified = false;

  // Validate

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.baseDir = new File(this.sGenBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' points to a non existent directory.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' points to an file system entry " + "that is not a directory. ");
    }

    // relative-dir

    if (SUtils.isEmpty(this.sRelativeDir)) {
      throw new InvalidConfigurationFileException("Attribute 'relative-dir' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.fullRelativeDir = new File(this.baseDir, this.sRelativeDir);
    if (!this.fullRelativeDir.exists()) {
      throw new InvalidConfigurationFileException("Attribute 'relative-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sRelativeDir + "' points to a non existent directory.");
    }
    if (!this.fullRelativeDir.isDirectory()) {
      throw new InvalidConfigurationFileException("Attribute 'relative-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sRelativeDir + "' points to an file system entry " + "that is not a directory. ");
    }

    this.customDir = new File(this.fullRelativeDir, CUSTOM_MAPPERS_DIR);

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
    return getPrimitivesDir(null);
  }

  public File getPrimitivesDir(final ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      File fragmentDir = fragmentPackage.getPackageDir(this.fullRelativeDir);
      File dir = new File(fragmentDir, PRIMITIVES_MAPPERS_DIR);
      dir.mkdirs();
      return dir;
    } else {
      File dir = new File(this.fullRelativeDir, PRIMITIVES_MAPPERS_DIR);
      dir.mkdirs();
      return dir;
    }
  }

  public File getRuntimeDir(final ClassPackage fragmentPackage) {
    File relDir = new File(this.sRelativeDir);
    if (fragmentPackage != null) {
      File fragmentDir = fragmentPackage.getPackageDir(relDir);
      File dir = new File(fragmentDir, PRIMITIVES_MAPPERS_DIR);
      return dir;
    } else {
      File dir = new File(relDir, PRIMITIVES_MAPPERS_DIR);
      return dir;
    }
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

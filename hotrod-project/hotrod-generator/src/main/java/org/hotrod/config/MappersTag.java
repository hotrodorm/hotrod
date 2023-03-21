package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "mappers")
public class MappersTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(MappersTag.class);

  private static final String DEFAULT_BASE_DIR = "src/main/resources";
  private static final String DEFAULT_DIR = "mappers";

  private static final String PRIMITIVES_MAPPERS_DIR = "primitives";
  private static final String CUSTOM_MAPPERS_DIR = "custom";

  public static final MappersTag DEFAULT_MAPPERS_TAG = new MappersTag();
  static {
    DEFAULT_MAPPERS_TAG.sBaseDir = DEFAULT_BASE_DIR;
    DEFAULT_MAPPERS_TAG.sDir = DEFAULT_DIR;
  }

  // Properties

  private String sBaseDir = null;
  private String sDir = null;

  private File baseDir;
  private File fullRelativeDir;

  private File customDir;
  private boolean customDirVerified = false;

  // Constructor

  public MappersTag() {
    super("mappers");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "base-dir")
  public void setBaseDir(final String sBaseDir) {
    this.sBaseDir = sBaseDir;
  }

  @XmlAttribute(name = "dir")
  public void setDir(final String sDir) {
    this.sDir = sDir;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // base-dir

    if (this.sBaseDir == null) {
      this.sBaseDir = DEFAULT_BASE_DIR;
    }
    if (SUtil.isEmpty(this.sBaseDir)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.baseDir = new File(basedir, this.sBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' points to a non existent directory.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' points to a file that is not a directory. ");
    }

    // dir

    if (SUtil.isEmpty(this.sDir)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'dir' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the base dir to generate the MyBatis mapper files.");
    }
    this.fullRelativeDir = new File(this.baseDir, this.sDir);
    if (!this.fullRelativeDir.exists()) {
      if (!this.fullRelativeDir.mkdirs()) {
        throw new InvalidConfigurationFileException(this, "Could not create mappers dir '"
            + this.fullRelativeDir.getPath() + "' specified in tag <" + super.getTagName() + ">.");
      }
    } else if (!this.fullRelativeDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'dir' of tag <" + super.getTagName()
          + "> with value '" + this.sDir + "' points to an file system entry " + "that is not a directory. ");
    }

    this.customDir = new File(this.fullRelativeDir, CUSTOM_MAPPERS_DIR);

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
    File relDir = new File(this.sDir);
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
    return sDir + "/" + CUSTOM_MAPPERS_DIR;
  }

  public String getRelativePrimitivesDir() {
    return sDir + "/" + PRIMITIVES_MAPPERS_DIR;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

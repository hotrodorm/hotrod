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

  private static final String VALID_NAMESPACE_PATTERN = "[a-z0-9][a-z0-9_]*";
  private static final String DEFAULT_NAMESPACE = "mappers";

  public static final MappersTag DEFAULT_MAPPERS_TAG = new MappersTag();
  static {
    DEFAULT_MAPPERS_TAG.sBaseDir = DEFAULT_BASE_DIR;
    DEFAULT_MAPPERS_TAG.sDir = DEFAULT_DIR;
  }

  // Properties

  private String sBaseDir = null;
  private String sDir = null;
  private String namespace = null;

  private File baseDir;
  private File fullRelativeDir;

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

  @XmlAttribute(name = "namespace")
  public void setNamespace(final String namespace) {
    this.namespace = namespace;
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

    // namespace

    if (this.namespace == null) {
      this.namespace = DEFAULT_NAMESPACE;
    } else {
      if (!this.namespace.matches(VALID_NAMESPACE_PATTERN)) {
        String msg = "Invalid attribute 'namespace' with value '" + this.namespace
            + "'. Must be a lower case alphanumeric value.";
        throw new InvalidConfigurationFileException(this, msg);
      }
    }

  }

  // Getters

  public String getNamespace() {
    return namespace;
  }

  public File getPrimitivesDir() {
    return getPrimitivesDir(null);
  }

  public File getPrimitivesDir(final ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      File dir = fragmentPackage.getPackageDir(this.fullRelativeDir);
      dir.mkdirs();
      return dir;
    } else {
      File dir = this.fullRelativeDir;
      dir.mkdirs();
      return dir;
    }
  }

  public File getRuntimeDir(final ClassPackage fragmentPackage) {
    File relDir = new File(this.sDir);
    if (fragmentPackage != null) {
      File dir = fragmentPackage.getPackageDir(relDir);
      return dir;
    } else {
      File dir = relDir;
      return dir;
    }
  }


//  public String getRelativePrimitivesDir() {
//    return sDir ;
//  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

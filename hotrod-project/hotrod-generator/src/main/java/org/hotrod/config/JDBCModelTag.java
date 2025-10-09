package org.hotrod.config;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtil;

@XmlRootElement(name = "model")
public class JDBCModelTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(JDBCModelTag.class.getName());

  private static final String DEFAULT_PREFIX = "";
  private static final String DEFAULT_SUFFIX = "";
  private static final String DEFAULT_SUBPACKAGE = "model";

  private static final Pattern PREFIX_SUFFIX_PATTERN = Pattern.compile("^[A-Za-z0-9_]*$");

  // Properties

  private String sBaseDir = null;
  private String sPackage = null;
  private String sSubPackage = null;
  private String prefix = null;
  private String suffix = null;

  private File baseDir;
  private ClassPackage basePackage;
  private ClassPackage subpackage;

  // Constructor

  public JDBCModelTag() {
    super("model");
    log.fine("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "base-dir")
  public void setBaseDir(final String sBaseDir) {
    this.sBaseDir = sBaseDir;
  }

  @XmlAttribute(name = "package")
  public void setSPackage(final String sPackage) {
    this.sPackage = sPackage;
  }

  @XmlAttribute(name = "sub-package")
  public void setSSubPackage(final String sSubPackage) {
    this.sSubPackage = sSubPackage;
  }

  @XmlAttribute(name = "prefix")
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  @XmlAttribute(name = "suffix")
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  // Behavior

  public void validate(final File currentDir, final File jdbcBaseDir, final ClassPackage jdbcPackage)
      throws InvalidConfigurationFileException {

    // base-dir

    if (this.sBaseDir == null) {
      this.baseDir = jdbcBaseDir;
    } else {
      if (SUtil.isEmpty(this.sBaseDir)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'base-dir' of the tag <" + super.getTagName()
                + "> cannot be empty. Use '.' to indicate the project dir "
                + "or leave unspecified to use the base.dir value of the <jdbc> tag.");
      }
      this.baseDir = new File(currentDir, this.sBaseDir);
      if (!this.baseDir.exists()) {
        throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of the tag <" + super.getTagName()
            + "> with value '" + this.sBaseDir + "' must point to an existing dir.");
      }
      if (!this.baseDir.isDirectory()) {
        throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of the tag <" + super.getTagName()
            + "> with value '" + this.sBaseDir + "' points to a file entry that is not a directory.");
      }
    }

    // package

    this.basePackage = jdbcPackage;
    if (this.sPackage != null) {
      try {
        this.basePackage = ClassPackage.parse(this.sPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this,
            "Invalid package '" + this.sPackage + "' in the attribute 'package' of the tag <" + super.getTagName()
                + ">: when specified it must be a valid package: " + e.getMessage());
      }
    }

    // sub-package

    if (this.sSubPackage == null) {
      String ds = DEFAULT_SUBPACKAGE;
      this.subpackage = null;
      try {
        this.subpackage = ClassPackage.parse(ds);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this,
            "Invalid default subpackage '" + ds
                + "'. Please specify a subpackage on the attribute 'sub-package' of the tag <" + super.getTagName()
                + ">: " + e.getMessage());
      }
    } else {
      try {
        this.subpackage = ClassPackage.parse(this.sSubPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, "Invalid sub-package '" + this.sSubPackage
            + "' on attribute 'sub-package' of the tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

    // prefix

    if (this.prefix == null) {
      this.prefix = DEFAULT_PREFIX;
    } else {
      Matcher m = PREFIX_SUFFIX_PATTERN.matcher(this.prefix);
      if (!m.matches()) {
        throw new InvalidConfigurationFileException(this, "Invalid prefix value '" + this.prefix
            + "'. When specified, it can be blank or include letters, digits, or underscores.");
      }
    }

    // suffix

    if (this.suffix == null) {
      this.suffix = DEFAULT_SUFFIX;
    } else {
      Matcher m = PREFIX_SUFFIX_PATTERN.matcher(this.suffix);
      if (!m.matches()) {
        throw new InvalidConfigurationFileException(this, "Invalid prefix value '" + this.suffix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

  }

  // Getters

  public String getName(final ObjectId id) {
    return this.prefix + id.getJavaClassName() + this.suffix;
  }

  public String getNitroName(final String baseName) {
    return this.prefix + baseName + this.suffix;
  }

  public ClassPackage getPackage(ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      return this.basePackage.append(fragmentPackage).append(this.subpackage);
    } else {
      return this.basePackage.append(this.subpackage);
    }
  }

  public File getPackageDir(final ClassPackage fragmentPackage) {
    ClassPackage p = this.getPackage(fragmentPackage);
    File dir = p.getPackageDir(this.baseDir);
    dir.mkdirs();
    return dir;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

package org.hotrod.config;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtil;

@XmlRootElement(name = "layout")
public class JDBCLayoutTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(JDBCLayoutTag.class.getName());

  private static final String DEFAULT_PREFIX = "";
  private static final String DEFAULT_SUFFIX = "Layout";
  private static final String DEFAULT_SUBPACKAGE = "layout";

  private static final Pattern PREFIX_SUFFIX_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$");

  // Properties

  private String prefix = null;
  private String suffix = null;
  private String sBaseDir = null;
  private String sSubPackage = null;

  private File baseDir;
  private ClassPackage itemPackage;

  // Constructor

  public JDBCLayoutTag() {
    super("layout");
    log.fine("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "prefix")
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  @XmlAttribute(name = "suffix")
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  @XmlAttribute(name = "base-dir")
  public void setBaseDir(final String sBaseDir) {
    this.sBaseDir = sBaseDir;
  }

  @XmlAttribute(name = "sub-package")
  public void setSSubPackage(final String sSubPackage) {
    this.sSubPackage = sSubPackage;
  }

  // Behavior

  public void validate(final File currentDir, final File mainBaseDir, final ClassPackage mainPackage)
      throws InvalidConfigurationFileException {

    // prefix

    if (this.prefix == null) {
      this.prefix = DEFAULT_PREFIX;
    } else {
      Matcher m = PREFIX_SUFFIX_PATTERN.matcher(this.prefix);
      if (!m.matches()) {
        throw new InvalidConfigurationFileException(this, "Invalid prefix value '" + this.prefix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
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

    // base-dir

    if (this.sBaseDir == null) {
      this.baseDir = mainBaseDir;
    } else {
      if (SUtil.isEmpty(this.sBaseDir)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'base-dir' of the tag <" + super.getTagName() + "> cannot be empty.");
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

    // sub-package

    if (this.sSubPackage == null) {
      ClassPackage sp = null;
      try {
        sp = new ClassPackage(DEFAULT_SUBPACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this,
            "Invalid default sub-package '" + DEFAULT_SUBPACKAGE
                + "'. Please specify a sub-package on the attribute 'sub-package' of the tag <" + super.getTagName()
                + ">: " + e.getMessage());
      }
      this.itemPackage = mainPackage.append(sp);
    } else {
      try {
        this.itemPackage = new ClassPackage(this.sSubPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sSubPackage
            + "' on attribute 'sub-package' of the tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

  }

  // Getters

  public String generateName(final String baseName) {
    return this.prefix + baseName + this.suffix;
  }

  public ClassPackage getPackage(final ClassPackage mainPackage) {
    return this.itemPackage;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

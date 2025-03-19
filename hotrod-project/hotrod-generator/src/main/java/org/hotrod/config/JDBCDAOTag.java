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

@XmlRootElement(name = "dao")
public class JDBCDAOTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(JDBCDAOTag.class.getName());

  private static final String DEFAULT_PREFIX = "";
  private static final String DEFAULT_SUFFIX = "DAO";
  private static final String DEFAULT_SUBPACKAGE = "dao";

  private static final Pattern PREFIX_SUFFIX_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$");

  // Properties

  private String prefix = null;
  private String suffix = null;
  private String sBaseDir = null;
  private String sSubPackage = null;

  private File baseDir;
  private ClassPackage itemPackage;

  // Constructor

  public JDBCDAOTag() {
    super("dao");
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
      String ds = getDefaultSubPackage();
      if (ds == null) {
        this.itemPackage = mainPackage;
        log.info("DAO 1 ds=" + ds );
      } else {
        ClassPackage sp = null;
        try {
          sp = new ClassPackage(ds);
          log.info("DAO 2 ds=" + ds + " -- sp=" + sp);
        } catch (InvalidPackageException e) {
          throw new InvalidConfigurationFileException(this,
              "Invalid default sub-package '" + ds
                  + "'. Please specify a sub-package on the attribute 'sub-package' of the tag <" + super.getTagName()
                  + ">: " + e.getMessage());
        }
        this.itemPackage = mainPackage.append(sp);
      }
    } else {
      log.info("DAO 3");
      try {
        this.itemPackage = new ClassPackage(this.sSubPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sSubPackage
            + "' on attribute 'sub-package' of the tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

  }

  private String getDefaultSubPackage() {
    return DEFAULT_SUBPACKAGE;
  }

  // Getters

  public String getName(ObjectId id) {
    if (id.wasJavaNameSpecified()) {
      if (id.isRelatedToDatabase()) { // database object
        return this.prefix + id.getJavaClassName() + this.suffix;
      } else { // executor
        return id.getJavaClassName();
      }
    } else {
      return this.prefix + id.getJavaClassName() + this.suffix;
    }
  }

  public ClassPackage getPackage(ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      return this.itemPackage.append(fragmentPackage);
    } else {
      return this.itemPackage;
    }
  }

  public File getPackageDir(ClassPackage fragmentPackage) {
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

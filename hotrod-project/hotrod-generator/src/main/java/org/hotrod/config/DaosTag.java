package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.utils.ClassPackage;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "daos")
public class DaosTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(DaosTag.class);

  private static final String DEFAULT_PRIMITIVES_RELATIVE_PACKAGE = "primitives";

  private static final String DEFAULT_BASE_DIR = "src/main/java";

  private static final String DEFAULT_VO_PREFIX = "";
  private static final String DEFAULT_VO_SUFFIX = "VO";
  private static final String DEFAULT_ABSTRACT_VO_PREFIX = "";
  private static final String DEFAULT_ABSTRACT_VO_SUFFIX = "";
  private static final String DEFAULT_DAO_PREFIX = "";
  private static final String DEFAULT_DAO_SUFFIX = "DAO";

  private static final String DEFAULT_PRIMITIVES_PREFIX = "";
  private static final String DEFAULT_PRIMITIVES_SUFFIX = "Primitives";

  private static final String PREFIX_SUFFIX_PATTERN = "[A-Za-z0-9_]*";

  // Properties

  private String sBaseDir = null;
  private String sPackage = null;
  private String sPrimitivesPackage = null;

  private String voPrefix = null;
  private String voSuffix = null;
  private String abstractVoPrefix = null;
  private String abstractVoSuffix = null;
  private String daoPrefix = null;
  private String daoSuffix = null;
  private String primitivesPrefix = null;
  private String primitivesSuffix = null;

  private String sqlSessionBeanQualifier = null;
  private String liveSQLDialectBeanQualifier = null;

  private File baseDir;
  private ClassPackage daoPackage;
  private ClassPackage primitivesTailPackage;

  // Constructor

  public DaosTag() {
    super("daos");
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

  @XmlAttribute(name = "primitives-package")
  public void setSprimitivesPackage(final String sPrimitivesPackage) {
    this.sPrimitivesPackage = sPrimitivesPackage;
  }

  @XmlAttribute(name = "vo-prefix")
  public void setVoPrefix(final String voPrefix) {
    this.voPrefix = voPrefix;
  }

  @XmlAttribute(name = "vo-suffix")
  public void setVoSuffix(final String voSuffix) {
    this.voSuffix = voSuffix;
  }

  @XmlAttribute(name = "abstract-vo-prefix")
  public void setAbstractVoPrefix(final String abstractVoPrefix) {
    this.abstractVoPrefix = abstractVoPrefix;
  }

  @XmlAttribute(name = "abstract-vo-suffix")
  public void setAbstractVoSuffix(final String abstractVoSuffix) {
    this.abstractVoSuffix = abstractVoSuffix;
  }

  @XmlAttribute(name = "dao-prefix")
  public void setDaoPrefix(final String daoPrefix) {
    this.daoPrefix = daoPrefix;
  }

  @XmlAttribute(name = "dao-suffix")
  public void setDaoSuffix(final String daoSuffix) {
    this.daoSuffix = daoSuffix;
  }

  @XmlAttribute(name = "java-class-name")
  public void setPrimitivesPrefix(final String primitivesPrefix) {
    this.primitivesPrefix = primitivesPrefix;
  }

  @XmlAttribute(name = "primitives-suffix")
  public void setPrimitivesSuffix(final String primitivesSuffix) {
    this.primitivesSuffix = primitivesSuffix;
  }

  @XmlAttribute(name = "sql-session-bean-qualifier")
  public void setSQLSessionBeanQualifier(final String sqlSessionBeanQualifier) {
    this.sqlSessionBeanQualifier = sqlSessionBeanQualifier;
  }

  @XmlAttribute(name = "live-sql-dialect-bean-qualifier")
  public void setLiveSQLDialectBeanQualifier(final String liveSQLDialectBeanQualifier) {
    this.liveSQLDialectBeanQualifier = liveSQLDialectBeanQualifier;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // gen-base-dir

    if (this.sBaseDir == null) {
      this.sBaseDir = DEFAULT_BASE_DIR;
    }
    if (SUtil.isEmpty(this.sBaseDir)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the base dir to generate the DAO classes.");
    }
    this.baseDir = new File(basedir, this.sBaseDir);
    log.debug("this.baseDir=" + this.baseDir + " , " + this.baseDir.getAbsolutePath());
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' must point to an existing dir.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'gen-base-dir' of tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' is not a directory.");
    }

    // dao-package

    try {
      this.daoPackage = new ClassPackage(this.sPackage);
    } catch (InvalidPackageException e) {
      throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sPackage
          + "' on attribute 'dao-package' of tag <" + super.getTagName() + ">: " + e.getMessage());
    }

    // primitives-package

    if (this.sPrimitivesPackage == null) {
      try {
        this.primitivesTailPackage = new ClassPackage(DEFAULT_PRIMITIVES_RELATIVE_PACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'primitives-package' attribute value on tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    } else {
      try {
        this.primitivesTailPackage = new ClassPackage(this.sPrimitivesPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sPrimitivesPackage
            + "' on attribute 'primitives-package' of tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

    // =========================================================================

    // vo-prefix

    if (this.voPrefix == null) {
      this.voPrefix = DEFAULT_VO_PREFIX;
    } else {
      if (!this.voPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid vo-prefix value '" + this.voPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // vo-suffix

    if (this.voSuffix == null) {
      this.voSuffix = DEFAULT_VO_SUFFIX;
    } else {
      if (!this.voSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid vo-suffix value '" + this.daoSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // abstract-vo-prefix

    if (this.abstractVoPrefix == null) {
      this.abstractVoPrefix = DEFAULT_ABSTRACT_VO_PREFIX;
    } else {
      if (!this.abstractVoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid abstract-vo-prefix value '" + this.voPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // abstract-vo-suffix

    if (this.abstractVoSuffix == null) {
      this.abstractVoSuffix = DEFAULT_ABSTRACT_VO_SUFFIX;
    } else {
      if (!this.abstractVoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid abstract-vo-suffix value '" + this.daoSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // =========================================================================

    // dao-prefix

    if (this.daoPrefix == null) {
      this.daoPrefix = DEFAULT_DAO_PREFIX;
    } else {
      if (!this.daoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid dao-prefix value '" + this.daoPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // dao-suffix

    if (this.daoSuffix == null) {
      this.daoSuffix = DEFAULT_DAO_SUFFIX;
    } else {
      if (!this.daoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid dao-suffix value '" + this.daoSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // primitives-prefix

    if (this.primitivesPrefix == null) {
      this.primitivesPrefix = DEFAULT_PRIMITIVES_PREFIX;
    } else {
      if (!this.primitivesPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid primitives-prefix value '" + this.primitivesPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // primitives-suffix

    if (this.primitivesSuffix == null) {
      this.primitivesSuffix = DEFAULT_PRIMITIVES_SUFFIX;
    } else {
      if (!this.primitivesSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid primitives-suffix value '" + this.primitivesSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

  }

  // Behavior

  public String generateVOName(final ObjectId id) {
    // if (id.wasJavaNameSpecified()) {
    // return id.getJavaClassName();
    // } else {
    return this.voPrefix + id.getJavaClassName() + this.voSuffix;
    // }
  }

  public String generateVOName(final String name) {
    String voc = this.voPrefix + name + this.voSuffix;
    log.debug("name=" + name + " voc=" + voc);
    return voc;
  }

  public String generateDAOName(final ObjectId id) {
    if (id.wasJavaNameSpecified()) {
      if (id.isRelatedToDatabase()) { // database object
        return this.daoPrefix + id.getJavaClassName() + this.daoSuffix;
      } else { // executor
        return id.getJavaClassName();
      }
    } else {
      return this.daoPrefix + id.getJavaClassName() + this.daoSuffix;
    }
  }

  public String generateDAOName(final String name) {
    return this.daoPrefix + name + this.daoSuffix;
  }

  public String generatePrimitivesName(final ObjectId id) {
    return this.primitivesPrefix + id.getJavaClassName() + this.primitivesSuffix;
  }

  public String generateAbstractVOName(final ObjectId id) {
    // return this.abstractVoPrefix + this.generateVOName(id) +
    // this.abstractVoSuffix;
    return this.abstractVoPrefix + id.getJavaClassName() + this.abstractVoSuffix;
  }

  public String generateAbstractVOName(final String voName) {
    return this.abstractVoPrefix + voName + this.abstractVoSuffix;
  }

  // Getters

  /**
   * <pre>
   * 
   * Use instead:
   * 
   *   public ClassPackage getDaoPackage(final ClassPackage fragmentPackage)
   * 
   * </pre>
   * 
   * @return The DAO package name
   */
  @Deprecated
  public ClassPackage getDaoPackage() {
    return this.getDaoPackage(null);
  }

  public ClassPackage getDaoPackage(final ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      return this.daoPackage.append(fragmentPackage);
    } else {
      return this.daoPackage;
    }
  }

  /**
   * <pre>
   * 
   * Use instead:
   * 
   *   public File getDaosPackageDir(final ClassPackage fragmentPackage)
   * 
   * </pre>
   * 
   * @return The DAO package dir
   */
  @Deprecated
  public File getDaosPackageDir() {
    return this.getDaosPackageDir(null);
  }

  public File getDaosPackageDir(final ClassPackage fragmentPackage) {
    ClassPackage p = getDaoPackage(fragmentPackage);
    File dir = p.getPackageDir(this.baseDir);
    dir.mkdirs();
    return dir;
  }

  public File getVOPackageDir(final ClassPackage p) {
    File dir = p.getPackageDir(this.baseDir);
    dir.mkdirs();
    return dir;
  }

  public File getPrimitivesVOPackageDir(final ClassPackage p) {
    ClassPackage full = getPrimitivesVOPackage(p);
    File dir = full.getPackageDir(this.baseDir);
    dir.mkdirs();
    return dir;
  }

  public ClassPackage getPrimitivesVOPackage(final ClassPackage p) {
    return p.append(this.primitivesTailPackage);
  }

  public String getSqlSessionBeanQualifier() {
    return sqlSessionBeanQualifier;
  }

  public String getLiveSQLDialectBeanQualifier() {
    return liveSQLDialectBeanQualifier;
  }

  /**
   * <pre>
   * 
   * Use instead:
   * 
   *   public ClassPackage getPrimitivesPackage(final ClassPackage fragmentPackage)
   * 
   * </pre>
   * 
   * @return The primitives package
   */
  @Deprecated
  public ClassPackage getPrimitivesPackage() {
    return this.getPrimitivesPackage(null);
  }

  public ClassPackage getPrimitivesPackage(final ClassPackage fragmentPackage) {
    if (fragmentPackage != null) {
      return this.daoPackage.append(fragmentPackage).append(this.primitivesTailPackage);
    } else {
      return this.daoPackage.append(this.primitivesTailPackage);
    }
  }

  /**
   * <pre>
   * 
   * Use instead:
   * 
   * public File getPrimitivesPackageDir(final ClassPackage fragmentPackage)
   * 
   * </pre>
   * 
   * @return The primitives dir
   */
  @Deprecated
  public File getPrimitivesPackageDir() {
    ClassPackage p = getPrimitivesPackage();
    File dir = p.getPackageDir(this.baseDir);
    dir.mkdirs();
    return dir;
  }

  public File getPrimitivesPackageDir(final ClassPackage fragmentPackage) {
    ClassPackage p = getPrimitivesPackage(fragmentPackage);
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

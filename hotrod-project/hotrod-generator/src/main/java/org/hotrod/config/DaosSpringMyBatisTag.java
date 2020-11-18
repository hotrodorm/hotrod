package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;
import org.hotrod.utils.identifiers.ObjectId;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "daos")
public class DaosSpringMyBatisTag extends DaosTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(DaosSpringMyBatisTag.class);

  private static final String DEFAULT_PRIMITIVES_RELATIVE_PACKAGE = "primitives";

  private static final String DEFAULT_BASE_DIR = "src/main/java";

  private static final String DEFAULT_DAO_PREFIX = "";
  private static final String DEFAULT_DAO_SUFFIX = "DAO";
  private static final String DEFAULT_VO_PREFIX = "";
  private static final String DEFAULT_VO_SUFFIX = "VO";
  private static final String DEFAULT_ABSTRACT_VO_PREFIX = "";
  private static final String DEFAULT_ABSTRACT_VO_SUFFIX = "";

  private static final String DEFAULT_NDAO_PREFIX = "";
  private static final String DEFAULT_NDAO_SUFFIX = "DAO";
  private static final String DEFAULT_NVO_PREFIX = "";
  private static final String DEFAULT_NVO_SUFFIX = "";
  private static final String DEFAULT_NABSTRACT_VO_PREFIX = "Abstract";
  private static final String DEFAULT_NABSTRACT_VO_SUFFIX = "";

  private static final String DEFAULT_PRIMITIVES_PREFIX = "";
  private static final String DEFAULT_PRIMITIVES_SUFFIX = "Primitives";

  private static final String PREFIX_SUFFIX_PATTERN = "[A-Za-z0-9_]*";

  // Properties

  private String sBaseDir = null;
  private String sPackage = null;
  private String sPrimitivesPackage = null;

  private String daoPrefix = null;
  private String daoSuffix = null;
  private String voPrefix = null;
  private String voSuffix = null;
  private String abstractVoPrefix = null;
  private String abstractVoSuffix = null;

  private String primitivesPrefix = null;
  private String primitivesSuffix = null;

  private String ndaoPrefix = null;
  private String ndaoSuffix = null;
  private String nvoPrefix = null;
  private String nvoSuffix = null;
  private String nabstractVoPrefix = null;
  private String nabstractVoSuffix = null;

  private File baseDir;
  private ClassPackage daoPackage;
  private ClassPackage primitivesTailPackage;

  // Constructor

  public DaosSpringMyBatisTag() {
    super();
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

  @XmlAttribute(name = "dao-prefix")
  public void setDaoPrefix(final String daoPrefix) {
    this.daoPrefix = daoPrefix;
  }

  @XmlAttribute(name = "dao-suffix")
  public void setDaoSuffix(final String daoSuffix) {
    this.daoSuffix = daoSuffix;
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

  @XmlAttribute(name = "java-class-name")
  public void setPrimitivesPrefix(final String primitivesPrefix) {
    this.primitivesPrefix = primitivesPrefix;
  }

  @XmlAttribute(name = "primitives-suffix")
  public void setPrimitivesSuffix(final String primitivesSuffix) {
    this.primitivesSuffix = primitivesSuffix;
  }

  @XmlAttribute(name = "ndao-prefix")
  public void setNdaoPrefix(String ndaoPrefix) {
    this.ndaoPrefix = ndaoPrefix;
  }

  @XmlAttribute(name = "ndao-suffix")
  public void setNdaoSuffix(String ndaoSuffix) {
    this.ndaoSuffix = ndaoSuffix;
  }

  @XmlAttribute(name = "nvo-prefix")
  public void setNvoPrefix(String nvoPrefix) {
    this.nvoPrefix = nvoPrefix;
  }

  @XmlAttribute(name = "nvo-suffix")
  public void setNvoSuffix(String nvoSuffix) {
    this.nvoSuffix = nvoSuffix;
  }

  @XmlAttribute(name = "nabstract-vo-prefix")
  public void setNabstractVoPrefix(String nabstractVoPrefix) {
    this.nabstractVoPrefix = nabstractVoPrefix;
  }

  @XmlAttribute(name = "nabstract-vo-suffix")
  public void setNabstractVoSuffix(String nabstractVoSuffix) {
    this.nabstractVoSuffix = nabstractVoSuffix;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // gen-base-dir

    if (this.sBaseDir == null) {
      this.sBaseDir = DEFAULT_BASE_DIR;
    }
    if (SUtil.isEmpty(this.sBaseDir)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'gen-base-dir' cannot be empty", //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the base dir to generate the DAO classes.");
    }
    this.baseDir = new File(basedir, this.sBaseDir);
    log.debug("this.baseDir=" + this.baseDir + " , " + this.baseDir.getAbsolutePath());
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(this, //
          "'gen-base-dir' points to a non-existing directory:\n  " + this.baseDir.getPath(), //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> with value '" + this.sBaseDir
              + "' must point to an existing dir.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, //
          "'gen-base-dir' must be a directory but point to other type of file:\n  " + this.baseDir.getPath(), //
          "Attribute 'gen-base-dir' of tag <" + super.getTagName() + "> with value '" + this.sBaseDir
              + "' is not a directory.");
    }

    // dao-package

    try {
      this.daoPackage = new ClassPackage(this.sPackage);
    } catch (InvalidPackageException e) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid package '" + this.sPackage + "'", //
          "Invalid package '" + this.sPackage + "' on attribute 'dao-package' of tag <" + super.getTagName() + ">: "
              + e.getMessage());
    }

    // primitives-package

    if (this.sPrimitivesPackage == null) {
      try {
        this.primitivesTailPackage = new ClassPackage(DEFAULT_PRIMITIVES_RELATIVE_PACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'primitives-package': " + e.getMessage(), //
            "Invalid 'primitives-package' attribute value on tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    } else {
      try {
        this.primitivesTailPackage = new ClassPackage(this.sPrimitivesPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid package '" + this.sPrimitivesPackage + "': " + e.getMessage(), //
            "Invalid package '" + this.sPrimitivesPackage + "' on attribute 'primitives-package' of tag <"
                + super.getTagName() + ">: " + e.getMessage());
      }
    }

    // dao-prefix

    if (this.daoPrefix == null) {
      this.daoPrefix = DEFAULT_DAO_PREFIX;
    } else {
      if (!this.daoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid dao-prefix value '" + this.daoPrefix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid dao-prefix value '" + this.daoPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // dao-suffix

    if (this.daoSuffix == null) {
      this.daoSuffix = DEFAULT_DAO_SUFFIX;
    } else {
      if (!this.daoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid dao-suffix value '" + this.daoSuffix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid dao-suffix value '" + this.daoSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // vo-prefix

    if (this.voPrefix == null) {
      this.voPrefix = DEFAULT_VO_PREFIX;
    } else {
      if (!this.voPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid vo-prefix value '" + this.voPrefix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid vo-prefix value '" + this.voPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // vo-suffix

    if (this.voSuffix == null) {
      this.voSuffix = DEFAULT_VO_SUFFIX;
    } else {
      if (!this.voSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid vo-suffix value '" + this.daoSuffix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid vo-suffix value '" + this.daoSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // abstract-vo-prefix

    if (this.abstractVoPrefix == null) {
      this.abstractVoPrefix = DEFAULT_ABSTRACT_VO_PREFIX;
    } else {
      if (!this.abstractVoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid abstract-vo-prefix value '" + this.voPrefix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid abstract-vo-prefix value '" + this.voPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // abstract-vo-suffix

    if (this.abstractVoSuffix == null) {
      this.abstractVoSuffix = DEFAULT_ABSTRACT_VO_SUFFIX;
    } else {
      if (!this.abstractVoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid abstract-vo-suffix value '" + this.daoSuffix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid abstract-vo-suffix value '" + this.daoSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // ndao-prefix

    if (this.ndaoPrefix == null) {
      this.ndaoPrefix = DEFAULT_NDAO_PREFIX;
    } else {
      if (!this.ndaoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid ndao-prefix value '" + this.ndaoPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // ndao-suffix

    if (this.ndaoSuffix == null) {
      this.ndaoSuffix = DEFAULT_NDAO_SUFFIX;
    } else {
      if (!this.ndaoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid ndao-suffix value '" + this.ndaoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // nvo-prefix

    if (this.nvoPrefix == null) {
      this.nvoPrefix = DEFAULT_NVO_PREFIX;
    } else {
      if (!this.nvoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid nvo-prefix value '" + this.voPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // nvo-suffix

    if (this.nvoSuffix == null) {
      this.nvoSuffix = DEFAULT_NVO_SUFFIX;
    } else {
      if (!this.nvoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid nvo-suffix value '" + this.daoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // nabstract-vo-prefix

    if (this.nabstractVoPrefix == null) {
      this.nabstractVoPrefix = DEFAULT_NABSTRACT_VO_PREFIX;
    } else {
      if (!this.nabstractVoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid nabstract-vo-prefix value '" + this.voPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // nabstract-vo-suffix

    if (this.nabstractVoSuffix == null) {
      this.nabstractVoSuffix = DEFAULT_NABSTRACT_VO_SUFFIX;
    } else {
      if (!this.nabstractVoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid nabstract-vo-suffix value '" + this.daoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // primitives-prefix

    if (this.primitivesPrefix == null) {
      this.primitivesPrefix = DEFAULT_PRIMITIVES_PREFIX;
    } else {
      if (!this.primitivesPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid primitives-prefix value '" + this.primitivesPrefix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid primitives-prefix value '" + this.primitivesPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

    // primitives-suffix

    if (this.primitivesSuffix == null) {
      this.primitivesSuffix = DEFAULT_PRIMITIVES_SUFFIX;
    } else {
      if (!this.primitivesSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid primitives-suffix value '" + this.primitivesSuffix
                + "': when specified, it can only include one or more letters, digits, or underscores", //
            "Invalid primitives-suffix value '" + this.primitivesSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscores.");
      }
    }

  }

  // Entity DAOs, VOs, and AbstractVOs

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

  // Nitro DAOs, VOs, and AbstractVOs

  public String generateNitroVOName(final ObjectId id) {
    return this.nvoPrefix + id.getJavaClassName() + this.nvoSuffix;
  }

  public String generateNitroVOName(final String name) {
    String voc = this.nvoPrefix + name + this.nvoSuffix;
    return voc;
  }

  public String generateNitroDAOName(final ObjectId id) {
    if (id.wasJavaNameSpecified()) {
      if (id.isRelatedToDatabase()) { // database object
        return this.ndaoPrefix + id.getJavaClassName() + this.ndaoSuffix;
      } else { // executor
        return id.getJavaClassName();
      }
    } else {
      return this.ndaoPrefix + id.getJavaClassName() + this.ndaoSuffix;
    }
  }

  public String generateNitroDAOName(final String name) {
    return this.ndaoPrefix + name + this.ndaoSuffix;
  }

  public String generateNitroAbstractVOName(final ObjectId id) {
    return this.nabstractVoPrefix + id.getJavaClassName() + this.nabstractVoSuffix;
  }

  public String generateNitroAbstractVOName(final String voName) {
    return this.nabstractVoPrefix + voName + this.nabstractVoSuffix;
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      DaosSpringMyBatisTag f = (DaosSpringMyBatisTag) fresh;
      boolean different = !same(fresh);

      this.sBaseDir = f.sBaseDir;
      this.sPackage = f.sPackage;
      this.sPrimitivesPackage = f.sPrimitivesPackage;
      this.daoPrefix = f.daoPrefix;
      this.daoSuffix = f.daoSuffix;
      this.primitivesPrefix = f.primitivesPrefix;
      this.primitivesSuffix = f.primitivesSuffix;
      this.baseDir = f.baseDir;
      this.daoPackage = f.daoPackage;
      this.primitivesTailPackage = f.primitivesTailPackage;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      DaosSpringMyBatisTag f = (DaosSpringMyBatisTag) fresh;
      return //
      Compare.same(this.sBaseDir, f.sBaseDir) && //
          Compare.same(this.sPackage, f.sPackage) && //
          Compare.same(this.sPrimitivesPackage, f.sPrimitivesPackage) && //
          Compare.same(this.daoPrefix, f.daoPrefix) && //
          Compare.same(this.daoSuffix, f.daoSuffix) && //
          Compare.same(this.primitivesPrefix, f.primitivesPrefix) && //
          Compare.same(this.primitivesSuffix, f.primitivesSuffix) && //
          Compare.same(this.sBaseDir, f.sBaseDir) && //
          Compare.same(this.sBaseDir, f.sBaseDir) && //
          Compare.same(this.sBaseDir, f.sBaseDir) && //
          Compare.same(this.sBaseDir, f.sBaseDir);
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

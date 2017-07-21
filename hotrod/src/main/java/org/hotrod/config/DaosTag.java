package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "daos")
public class DaosTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(DaosTag.class);

  private static final String DEFAULT_PRIMITIVES_RELATIVE_PACKAGE = "primitives";
  private static final String DEFAULT_DAO_PREFIX = "";
  private static final String DEFAULT_DAO_SUFFIX = "DAO";
  private static final String DEFAULT_PRIMITIVES_PREFIX = "";
  private static final String DEFAULT_PRIMITIVES_SUFFIX = "Primitives";

  private static final String PREFIX_SUFFIX_PATTERN = "[A-Za-z0-9_]*";

  // Properties

  private String sGenBaseDir = null;
  private String sDaoPackage = null;
  private String sPrimitivesPackage = null;

  private String daoPrefix = null;
  private String daoSuffix = null;
  private String primitivesPrefix = null;
  private String primitivesSuffix = null;

  private File baseDir;
  private ClassPackage daoPackage;
  private ClassPackage primitivesTailPackage;

  // Constructor

  public DaosTag() {
    super("daos");
  }

  // JAXB Setters

  @XmlAttribute(name = "gen-base-dir")
  public void setGenBaseDir(final String genBaseDir) {
    this.sGenBaseDir = genBaseDir;
  }

  @XmlAttribute(name = "dao-package")
  public void setSdaoPackage(final String sDaoPackage) {
    this.sDaoPackage = sDaoPackage;
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

  @XmlAttribute(name = "java-class-name")
  public void setPrimitivesPrefix(final String primitivesPrefix) {
    this.primitivesPrefix = primitivesPrefix;
  }

  @XmlAttribute(name = "primitives-suffix")
  public void setPrimitivesSuffix(final String primitivesSuffix) {
    this.primitivesSuffix = primitivesSuffix;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'gen-base-dir' of tag <"
          + super.getTagName() + "> cannot be empty. " + "Must specify the base dir to generate the DAO classes.");
    }
    this.baseDir = new File(this.sGenBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'gen-base-dir' of tag <"
          + super.getTagName() + "> with value '" + this.sGenBaseDir + "' must point to an existing dir.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'gen-base-dir' of tag <"
          + super.getTagName() + "> with value '" + this.sGenBaseDir + "' is not a directory.");
    }

    // dao-package

    try {
      this.daoPackage = new ClassPackage(this.sDaoPackage);
    } catch (InvalidPackageException e) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid package '" + this.sDaoPackage
          + "' on attribute 'dao-package' of tag <" + super.getTagName() + ">: " + e.getMessage());
    }

    // primitives-package

    if (this.sPrimitivesPackage == null) {
      try {
        this.primitivesTailPackage = new ClassPackage(DEFAULT_PRIMITIVES_RELATIVE_PACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'primitives-package' attribute value on tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    } else {
      try {
        this.primitivesTailPackage = new ClassPackage(this.sPrimitivesPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid package '" + this.sPrimitivesPackage + "' on attribute 'primitives-package' of tag <"
                + super.getTagName() + ">: " + e.getMessage());
      }
    }

    // dao-prefix

    if (this.daoPrefix == null) {
      this.daoPrefix = DEFAULT_DAO_PREFIX;
    } else {
      if (!this.daoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid dao-prefix value '" + this.daoPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // dao-suffix

    if (this.daoSuffix == null) {
      this.daoSuffix = DEFAULT_DAO_SUFFIX;
    } else {
      if (!this.daoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid dao-suffix value '" + this.daoSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // primitives-prefix

    if (this.primitivesPrefix == null) {
      this.primitivesPrefix = DEFAULT_PRIMITIVES_PREFIX;
    } else {
      if (!this.primitivesPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid primitives-prefix value '" + this.primitivesPrefix
                + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // primitives-suffix

    if (this.primitivesSuffix == null) {
      this.primitivesSuffix = DEFAULT_PRIMITIVES_SUFFIX;
    } else {
      if (!this.primitivesSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid primitives-suffix value '" + this.primitivesSuffix
                + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

  }

  // Behavior

  public String generateDAOName(final Identifier identifier) {
    if (identifier.wasJavaNameSpecified()) {
      return identifier.getJavaClassIdentifier();
    } else {
      return this.daoPrefix + identifier.getJavaClassIdentifier() + this.daoSuffix;
    }
  }

  public String generatePrimitivesName(final Identifier identifier) {
    log.debug("this.primitivesSuffix=" + this.primitivesSuffix);
    return this.primitivesPrefix + identifier.getJavaClassIdentifier() + this.primitivesSuffix;
  }

  public String generateAbstractVOName(final Identifier identifier) {
    if (identifier.wasJavaNameSpecified()) {
      // For this case new <dao> tag attributes are needed.
      // Something like: abstract-vo-prefix & abstract-vo-suffix
      return "Abstract" + identifier.getJavaClassIdentifier() + "VO";
    } else {
      return "Abstract" + identifier.getJavaClassIdentifier() + "VO";
    }
  }

  public String generateVOName(final Identifier identifier) {
    if (identifier.wasJavaNameSpecified()) {
      // For this case new <dao> tag attributes are needed.
      // Something like: vo-prefix & vo-suffix
      return identifier.getJavaClassIdentifier() + "VO";
    } else {
      return identifier.getJavaClassIdentifier() + "VO";
    }
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
   * @return
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
   * @return
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

  /**
   * <pre>
   * 
   * Use instead:
   * 
   *   public ClassPackage getPrimitivesPackage(final ClassPackage fragmentPackage)
   * 
   * </pre>
   * 
   * @return
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
   * @return
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

}

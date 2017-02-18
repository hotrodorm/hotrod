package org.hotrod.config;

import java.io.File;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;
import org.hotrod.utils.identifiers.Identifier;

public class DaosTag {

  private static final Logger log = Logger.getLogger(DaosTag.class);

  public static final String TAG_NAME = "daos";

  private static final String DEFAULT_PRIMITIVES_RELATIVE_PACKAGE = "primitives";

  private static final String DEFAULT_DAO_PREFIX = "";
  private static final String DEFAULT_DAO_SUFFIX = "DAO";
  private static final String DEFAULT_PRIMITIVES_PREFIX = "";
  private static final String DEFAULT_PRIMITIVES_SUFFIX = "Primitives";

  private static final String PREFIX_SUFFIX_PATTERN = "[A-Za-z0-9_]*";

  private String sGenBaseDir = null;
  private String sDaoPackage = null;
  private String sPrimitivesPackage = null;

  private String daoPrefix = null;
  private String daoSuffix = null;
  private String primitivesPrefix = null;
  private String primitivesSuffix = null;

  private File baseDir;
  // private ClassPackage basePackage;
  private ClassPackage daoPackage;
  private ClassPackage primitivesPackage;

  private File daosDir;
  private File primitivesDir;
  private boolean daosDirVerified = false;
  private boolean primitivesDirVerified = false;

  public void validate() throws InvalidConfigurationFileException {

    // gen-base-dir

    if (SUtils.isEmpty(this.sGenBaseDir)) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify the base dir to generate the DAO classes.");
    }
    this.baseDir = new File(this.sGenBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' must point to an existing dir.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException("Attribute 'gen-base-dir' of tag <" + TAG_NAME + "> with value '"
          + this.sGenBaseDir + "' is not a directory.");
    }

    // dao-package

    try {
      this.daoPackage = new ClassPackage(this.sDaoPackage);
    } catch (InvalidPackageException e) {
      throw new InvalidConfigurationFileException("Invalid package '" + this.sDaoPackage
          + "' on attribute 'dao-package' of tag <" + TAG_NAME + ">: " + e.getMessage());
    }
    this.daosDir = this.daoPackage.getPackageDir(this.baseDir);

    // primitives-package

    if (this.sPrimitivesPackage == null) {
      try {
        this.primitivesPackage = this.daoPackage.append(DEFAULT_PRIMITIVES_RELATIVE_PACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(
            "Invalid 'primitives-package' attribute value on tag <" + TAG_NAME + ">: " + e.getMessage());
      }
    } else {
      try {
        this.primitivesPackage = new ClassPackage(this.sPrimitivesPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException("Invalid package '" + this.sPrimitivesPackage
            + "' on attribute 'primitives-package' of tag <" + TAG_NAME + ">: " + e.getMessage());
      }
    }
    this.primitivesDir = this.primitivesPackage.getPackageDir(this.baseDir);

    // dao-prefix

    if (this.daoPrefix == null) {
      this.daoPrefix = DEFAULT_DAO_PREFIX;
    } else {
      if (!this.daoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException("Invalid dao-prefix value '" + this.daoPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // dao-suffix

    if (this.daoSuffix == null) {
      this.daoSuffix = DEFAULT_DAO_SUFFIX;
    } else {
      if (!this.daoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException("Invalid dao-suffix value '" + this.daoSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // primitives-prefix

    if (this.primitivesPrefix == null) {
      this.primitivesPrefix = DEFAULT_PRIMITIVES_PREFIX;
    } else {
      if (!this.primitivesPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException("Invalid primitives-prefix value '" + this.primitivesPrefix
            + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

    // primitives-suffix

    if (this.primitivesSuffix == null) {
      this.primitivesSuffix = DEFAULT_PRIMITIVES_SUFFIX;
    } else {
      if (!this.primitivesSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException("Invalid primitives-suffix value '" + this.primitivesSuffix
            + "'. When specified, it can only include one or more letters, digits, or underscored (_).");
      }
    }

  }

  // Setters (digester)

  public void setGenBaseDir(final String genBaseDir) {
    this.sGenBaseDir = genBaseDir;
  }

  public void setSdaoPackage(final String sDaoPackage) {
    this.sDaoPackage = sDaoPackage;
  }

  public void setSprimitivesPackage(String sPrimitivesPackage) {
    this.sPrimitivesPackage = sPrimitivesPackage;
  }

  public void setDaoPrefix(String daoPrefix) {
    this.daoPrefix = daoPrefix;
  }

  public void setDaoSuffix(String daoSuffix) {
    this.daoSuffix = daoSuffix;
  }

  public void setPrimitivesPrefix(String primitivesPrefix) {
    this.primitivesPrefix = primitivesPrefix;
  }

  public void setPrimitivesSuffix(String primitivesSuffix) {
    this.primitivesSuffix = primitivesSuffix;
  }

  // Behavior

  public String generateDAOName(Identifier identifier) {
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

  // Getters

  public ClassPackage getDaoPackage() {
    return daoPackage;
  }

  public ClassPackage getPrimitivesPackage() {
    return primitivesPackage;
  }

  public File getDaosPackageDir() {
    if (!this.daosDirVerified) {
      if (!this.daosDir.exists()) {
        this.daosDir.mkdirs();
      }
      this.daosDirVerified = true;
    }
    return daosDir;
  }

  public File getPrimitivesPackageDir() {
    if (!this.primitivesDirVerified) {
      if (!this.primitivesDir.exists()) {
        this.primitivesDir.mkdirs();
      }
      this.primitivesDirVerified = true;
    }
    return primitivesDir;
  }

}

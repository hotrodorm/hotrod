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

@XmlRootElement(name = "layer-resources")
public class JDBCLayerResourcesTag extends AbstractConfigurationTag {

  private static final Logger log = Logger.getLogger(JDBCLayerResourcesTag.class.getName());

  private static final String DEFAULT_SUBPACKAGE = "resources";
  private static final String DEFAULT_CLASS_NAME = "LayerResourcesBean";

  private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("^[A-Z][A-Za-z0-9_]*+$");

  // Properties

  private String sBaseDir = null;
  private String sPackage = null;
  private String sSubPackage = null;
  private String className = null;

  private File baseDir;
  private ClassPackage basePackage;
  private ClassPackage subpackage;
  private ClassPackage assembledPackage;

  // Constructor

  public JDBCLayerResourcesTag() {
    super("LayerResources");
    log.fine("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "base-dir")
  public void setSBaseDir(final String sBaseDir) {
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

  @XmlAttribute(name = "name")
  public void setName(final String name) {
    this.className = name;
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

    this.assembledPackage = this.basePackage.append(this.subpackage);

    // name

    if (this.className == null) {
      this.className = DEFAULT_CLASS_NAME;
    } else {
      Matcher m = CLASS_NAME_PATTERN.matcher(this.className);
      if (!m.matches()) {
        throw new InvalidConfigurationFileException(this, "Invalid name '" + this.className
            + "'. When specified, it can only include letters, digits, and/or underscores.");
      }
    }

  }

  // Getters

  public ClassPackage getAssembledPackage() {
    return assembledPackage;
  }

  public File getDir() {
    return this.assembledPackage.getPackageDir(this.baseDir);
  }

  public String getClassName() {
    return className;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

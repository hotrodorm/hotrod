package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;

@XmlRootElement(name = "hotrod-fragment")
public class HotRodFragmentConfigTag extends AbstractHotRodConfigTag {

  // Constants

  private static final Logger log = Logger.getLogger(HotRodFragmentConfigTag.class);

  // Properties

  private String sPackage = null;

  private String filename;

  private ClassPackage fragmentPackage;

  // Constructor

  public HotRodFragmentConfigTag() {
    super("hotrod-fragment");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setPackage(final String p) {
    this.sPackage = p;
  }

  // Getters

  public ClassPackage getFragmentPackage() {
    return fragmentPackage;
  }

  public String toString() {
    return "{fragment-config:" + this.filename + "}";
  }

  // Validation

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // package

    if (this.sPackage == null) {
      this.fragmentPackage = null;
    } else {
      try {
        this.fragmentPackage = new ClassPackage(this.sPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException("Invalid package '" + this.sPackage
            + "' on attribute 'package' of tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

  }

}

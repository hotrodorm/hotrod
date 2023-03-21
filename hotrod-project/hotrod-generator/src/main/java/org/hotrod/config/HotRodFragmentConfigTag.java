package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "hotrod-fragment")
public class HotRodFragmentConfigTag extends AbstractHotRodConfigTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(HotRodFragmentConfigTag.class);

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
        throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sPackage
            + "' on attribute 'package' of tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return super.commonSameKey(fresh) && true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      HotRodFragmentConfigTag f = (HotRodFragmentConfigTag) fresh;
      boolean different = !super.commonSame(fresh) || !same(fresh);

      super.commonCopyNonKeyProperties(fresh);
      this.sPackage = f.sPackage;
      this.filename = f.filename;
      this.fragmentPackage = f.fragmentPackage;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      HotRodFragmentConfigTag f = (HotRodFragmentConfigTag) fresh;
      return //
      super.commonSame(fresh) && //
          Compare.same(this.sPackage, f.sPackage);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Update generated cache

  @Override
  public boolean concludeGeneration(final AbstractHotRodConfigTag cache, final DatabaseAdapter adapter) {
    HotRodFragmentConfigTag uc = (HotRodFragmentConfigTag) cache;
    boolean successfulCommonGeneration = super.commonConcludeGeneration(uc, adapter);

    // HotRodFragmentConfigTag

    if (successfulCommonGeneration) {
      return this.concludeGenerationMarkTag();
    }

    return false;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

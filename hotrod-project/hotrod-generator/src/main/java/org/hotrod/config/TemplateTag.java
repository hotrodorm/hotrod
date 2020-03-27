package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "mybatis-configuration-template")
public class TemplateTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(TemplateTag.class);

  // Properties

  private String sFile = null;

  private File file;

  // Constructor

  public TemplateTag() {
    super("mybatis-configuration-template");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "file")
  public void setTemplateFile(final String file) {
    this.sFile = file;
  }

  // Behavior

  public void validate(final File basedir, final File parentDir) throws InvalidConfigurationFileException {

    // file

    if (SUtil.isEmpty(this.sFile)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'file' cannot be empty", //
          "MyBatis configuration template not found. " + "Attribute 'file' of tag <" + super.getTagName()
              + "> cannot be empty. " + "Must specify the MyBatis configuration template file.");
    }
    this.file = new File(parentDir, this.sFile);
    if (!this.file.exists()) {
      throw new InvalidConfigurationFileException(this, //
          "MyBatis configuration template file not found", //
          "MyBatis configuration template file not found. " + "Attribute 'file' of tag <" + super.getTagName()
              + "> with value '" + this.sFile + "' points to a non-existing file.");
    }
    if (!this.file.isFile()) {
      throw new InvalidConfigurationFileException(this, //
          "MyBatis configuration template file found, but it's not a regular file", //
          "Attribute 'file' of tag <" + super.getTagName() + "> with value '" + this.sFile
              + "' is not a regular file.");
    }

  }

  // Getters

  public File getFile() {
    return file;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      TemplateTag f = (TemplateTag) fresh;
      boolean different = !same(fresh);

      this.sFile = f.sFile;
      this.file = f.file;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      TemplateTag f = (TemplateTag) fresh;
      return Compare.same(this.sFile, f.sFile);
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

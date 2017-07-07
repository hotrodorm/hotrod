package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

@XmlRootElement(name = "mybatis-configuration-template")
public class TemplateTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(TemplateTag.class);

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

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // file

    if (SUtils.isEmpty(this.sFile)) {
      throw new InvalidConfigurationFileException(
          "MyBatis configuration template not found. " + "Attribute 'file' of tag <" + super.getTagName()
              + "> cannot be empty. " + "Must specify the MyBatis configuration template file.");
    }
    this.file = new File(basedir, this.sFile);
    if (!this.file.exists()) {
      throw new InvalidConfigurationFileException(
          "MyBatis configuration template not found. " + "Attribute 'file' of tag <" + super.getTagName()
              + "> with value '" + this.sFile + "' points to a non-existing file.");
    }
    if (!this.file.isFile()) {
      throw new InvalidConfigurationFileException(
          "Attribute 'file' of tag <" + super.getTagName() + "> with value '" + this.sFile + "' is not a file.");
    }

  }

  // Getters

  public File getFile() {
    return file;
  }

}

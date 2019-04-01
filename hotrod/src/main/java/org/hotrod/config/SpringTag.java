package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "spring")
public class SpringTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(SpringTag.class);

  // Properties

  private String sBeansFile = null;

  private File beansFile;

  // Constructor

  public SpringTag() {
    super("spring");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "beans-file")
  public void setBeansFile(final String beansFile) {
    this.sBeansFile = beansFile;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // beans-file

    if (SUtils.isEmpty(this.sBeansFile)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'beans-file' of tag <" + super.getTagName() + "> cannot be empty", //
          "Attribute 'beans-file' of tag <" + super.getTagName() + "> cannot be empty");
    }
    this.beansFile = new File(basedir, this.sBeansFile);

  }

  // Getters

  public File getBeansResolvedFile() {
    return this.beansFile;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SpringTag f = (SpringTag) fresh;
      boolean different = !same(fresh);

      this.sBeansFile = f.sBeansFile;
      this.beansFile = f.beansFile;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SpringTag f = (SpringTag) fresh;
      return Compare.same(this.sBeansFile, f.sBeansFile);
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

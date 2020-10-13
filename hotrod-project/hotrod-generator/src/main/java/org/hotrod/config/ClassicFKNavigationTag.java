package org.hotrod.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "classic-fk-navigation")
public class ClassicFKNavigationTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ClassicFKNavigationTag.class);

  static final String TAG_NAME = "classic-fk-navigation";

  // Properties

  // Constructor

  protected ClassicFKNavigationTag() {
    super(TAG_NAME);
  }

  // JAXB Setters

  // Behavior

  public void validate() throws InvalidConfigurationFileException {
    log.debug("validate");
  }

  // Getters

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    return false;
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    return true;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

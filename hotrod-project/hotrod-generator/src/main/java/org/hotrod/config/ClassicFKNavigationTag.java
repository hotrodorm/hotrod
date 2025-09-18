package org.hotrod.config;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "classic-fk-navigation")
public class ClassicFKNavigationTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ClassicFKNavigationTag.class.getName());

  static final String TAG_NAME = "classic-fk-navigation";

  // Properties

  // Constructor

  protected ClassicFKNavigationTag() {
    super(TAG_NAME);
  }

  // JAXB Setters

  // Behavior

  public void validate() throws InvalidConfigurationFileException {
    log.fine("validate");
  }

  // Getters

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

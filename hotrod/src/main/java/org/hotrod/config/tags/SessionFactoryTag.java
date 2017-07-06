package org.hotrod.config.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

@XmlRootElement(name = "session-factory")
public class SessionFactoryTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(SequenceTag.class);

  private static final String ATT_NAME = "singleton-full-class-name";

  // Properties

  private String singletonFullClassName = null;

  // Constructor

  protected SessionFactoryTag() {
    super("session-factory");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "singleton-full-class-name")
  public void setSingletonFullClassName(final String singletonFullClassName) {
    this.singletonFullClassName = singletonFullClassName;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // singleton-full-class-name

    if (SUtils.isEmpty(this.singletonFullClassName)) {
      throw new InvalidConfigurationFileException("Attribute '" + ATT_NAME + "' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the singleton " + "that provides the SessionFactory object.");
    }

  }

  // Getters

  public String getSingletonFullClassName() {
    return singletonFullClassName;
  }

  public String getSessionFactoryGetter() {
    return this.singletonFullClassName + ".getInstance().getSqlSessionFactory()";
  }

}

package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "session-factory")
public class SessionFactoryTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SequenceMethodTag.class);

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

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // singleton-full-class-name

    if (SUtil.isEmpty(this.singletonFullClassName)) {
      throw new InvalidConfigurationFileException(this, "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName()
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

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

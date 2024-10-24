package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;

public class PropertyTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(PropertyTag.class);

  private static final String VIEW_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_\\.]*";

  private static final String ATT_NAME = "name";
  @SuppressWarnings("unused")
  private static final String ATT_VALUE = "value";

  // Properties

  private String name = null;
  private String value = null;

  // Constructors

  public PropertyTag() {
    super("property");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setValue(final String value) {
    this.value = value;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a property name.");
    }
    if (!this.name.matches(VIEW_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName() + "> must be a valid name. Specified value is '"
              + this.name + "' but must start with a letter, " + "and continue with one or more "
              + "letters, digits, period, or underscore characters.");
    }

    // value (no specific validation)

  }

  // Getters

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}

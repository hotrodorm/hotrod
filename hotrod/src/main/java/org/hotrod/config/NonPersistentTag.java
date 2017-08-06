package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;

@XmlRootElement(name = "non-persistent")
public class NonPersistentTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(NonPersistentTag.class);

  public static final String VALID_JAVA_CONSTANT_IDENTIFIER = "[A-Z][A-Z0-9_]*";

  // Properties

  private String value = null;
  private String name = null;

  // Constructor

  public NonPersistentTag() {
    super("non-persistent");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setValue(final String value) {
    this.value = value;
  }

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // value

    if (SUtils.isEmpty(this.value)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'value' of tag <"
          + super.getTagName() + "> cannot be empty. " + "Must specify a non-empty value.");
    }

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. " + "Must specify a non-empty name.");
    }
    if (!this.name.matches(VALID_JAVA_CONSTANT_IDENTIFIER)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'name' of tag <"
          + super.getTagName() + "> must be a valid Java constant identifier but found '" + this.name + "'. "
          + "The name must start with an uppercase letter, and continue with uppercase letters, digits, or underscores.");
    }

  }

  // Getters

  public String getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

}

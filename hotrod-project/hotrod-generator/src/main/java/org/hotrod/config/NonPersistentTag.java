package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "non-persistent")
public class NonPersistentTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(NonPersistentTag.class);

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

    if (SUtil.isEmpty(this.value)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'value' of tag <" + super.getTagName() + "> cannot be empty.");
    }

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty.");
    }
    if (!this.name.matches(VALID_JAVA_CONSTANT_IDENTIFIER)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> must be a valid Java constant identifier but found '" + this.name + "'. "
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      NonPersistentTag f = (NonPersistentTag) fresh;
      return this.value.equals(f.value);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      NonPersistentTag f = (NonPersistentTag) fresh;
      boolean different = !same(fresh);

      this.name = f.name;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      NonPersistentTag f = (NonPersistentTag) fresh;
      return //
      Compare.same(this.value, f.value) && //
          Compare.same(this.name, f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}

package org.hotrod.config;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class PropertyTag {

  private static final String VIEW_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_\\.]*";

  private static final String TAG_NAME = "property";

  private static final String ATT_NAME = "name";
  @SuppressWarnings("unused")
  private static final String ATT_VALUE = "value";

  private String name = null;
  private String value = null;

  // Validation

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute '" + ATT_NAME + "' of tag <" + TAG_NAME + "> cannot be empty. " + "Must specify a property name.");
    }
    if (!this.name.matches(VIEW_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute '" + ATT_NAME + "' of tag <" + TAG_NAME
          + "> must be a valid name. Specified value is '" + this.name + "' but must start with a letter, "
          + "and continue with one or more " + "letters, digits, period, or underscore characters.");
    }

    // value (no specific validation)

  }

  // Setters (digester)

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

}

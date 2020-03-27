package org.hotrod.config;

import java.io.Serializable;

import org.hotrod.exceptions.InvalidConfigurationFileException;

public class MyBatisProperties implements Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final String PROPERTY_CHECKED_PERSISTENCE_EXCEPTION = "checked.persistence.exception";
  private static final String PROPERTY_MULTILINE_TOSTRING = "multiline.tostring";

  private static final String TRUE = "true";
  private static final String FALSE = "false";

  // Properties

  private boolean checkedPersistenceException = true;
  private boolean multilineTostring = true;

  // Setters

  public void set(final AbstractConfigurationTag tag, final PropertyTag p) throws InvalidConfigurationFileException {

    if (PROPERTY_CHECKED_PERSISTENCE_EXCEPTION.equals(p.getName())) {
      if (TRUE.equals(p.getValue())) {
        this.checkedPersistenceException = true;
      } else if (FALSE.equals(p.getValue())) {
        this.checkedPersistenceException = false;
      } else {
        throw new InvalidConfigurationFileException(tag, //
            "Invalid value '" + p.getValue() + "' for property '" + p.getName() + "'. Valid values are: true, false", //
            "Invalid value '" + p.getValue() + "' for property '" + p.getName() + "'. Valid values are: true, false");
      }
    } else if (PROPERTY_MULTILINE_TOSTRING.equals(p.getName())) {
      if (TRUE.equals(p.getValue())) {
        this.multilineTostring = true;
      } else if (FALSE.equals(p.getValue())) {
        this.multilineTostring = false;
      } else {
        throw new InvalidConfigurationFileException(tag, //
            "Invalid value '" + p.getValue() + "' for property '" + p.getName() + "'. Valid values are: true, false", //
            "Invalid value '" + p.getValue() + "' for property '" + p.getName() + "'. Valid values are: true, false");
      }
    } else {
      throw new InvalidConfigurationFileException(tag, //
          "Invalid property name '" + p.getName() + "'. Valid property names are: " + getAllPropertyNames(), //
          "Invalid property name '" + p.getName() + "'. Valid property names are: " + getAllPropertyNames());
    }

  }

  // Getters

  public boolean isCheckedPersistenceException() {
    return checkedPersistenceException;
  }

  public boolean isMultilineTostring() {
    return multilineTostring;
  }

  // Utilities

  private String getAllPropertyNames() {
    return PROPERTY_CHECKED_PERSISTENCE_EXCEPTION + ", " + PROPERTY_MULTILINE_TOSTRING;
  }

}

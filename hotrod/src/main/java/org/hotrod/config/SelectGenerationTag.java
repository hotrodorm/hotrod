package org.hotrod.config;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class SelectGenerationTag {

  private static final String VIEW_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  private static final String TAG_NAME = "select-generation";

  private static final String ATT_NAME = "temp-view-base-name";

  private String tempViewBaseName = null;

  // Validation

  public void validate() throws InvalidConfigurationFileException {

    // temp-view-base-name

    if (SUtils.isEmpty(this.tempViewBaseName)) {
      throw new InvalidConfigurationFileException("Attribute '" + ATT_NAME
          + "' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify a temporary view name, "
          + "a name that is NOT used by any existing table, "
          + "view or any other database object on this database. "
          + "A view with this name may be created and dropped "
          + "several times during the DAO generation.");
    }
    if (!this.tempViewBaseName.matches(VIEW_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute '" + ATT_NAME
          + "' of tag <" + TAG_NAME
          + "> must be a valid view name. Specified value is '"
          + this.tempViewBaseName + "' but must start with a letter, "
          + "and continue with one or more "
          + "letters, digits, or undersore characters.");
    }

  }

  // Setters (digester)

  public void setTempViewBaseName(String tempViewBaseName) {
    this.tempViewBaseName = tempViewBaseName;
  }

  // Getters

  public String getTempViewBaseName() {
    return tempViewBaseName;
  }

}

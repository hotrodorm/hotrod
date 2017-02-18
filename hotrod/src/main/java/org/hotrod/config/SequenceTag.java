package org.hotrod.config;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class SequenceTag {

  static final String TAG_NAME = "sequence";

  private static final String METHOD_PREFIX = "selectSequence";
  static final String VALID_JAVA_METHOD_PATTERN = "[a-z][a-zA-Z0-9_$]*";

  private String name = null;
  private String javaMethodName = null;

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a sequence name.");
    }

    // method-name

    if (this.javaMethodName == null) {
      this.javaMethodName = METHOD_PREFIX + this.getIdentifier().getJavaClassIdentifier();
    } else {
      if (!this.javaMethodName.matches(VALID_JAVA_METHOD_PATTERN)) {
        throw new InvalidConfigurationFileException("Attribute 'java-method-name' of tag <" + TAG_NAME + "> specifies '"
            + this.javaMethodName + "' but must specify a valid java method name. "
            + "Valid method names must start with a lowercase letter, "
            + "and continue with letters, digits, dollarsign, and/or underscores.");
      }
    }

  }

  // Setters (digester)

  public void setName(final String name) {
    this.name = name;
  }

  public void setJavaMethodName(String javaMethodName) {
    this.javaMethodName = javaMethodName;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaMethodName() {
    return javaMethodName;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier(this.name);
  }

}

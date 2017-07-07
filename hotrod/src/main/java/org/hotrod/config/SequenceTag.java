package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "sequence")
public class SequenceTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(SequenceTag.class);

  static final String TAG_NAME = "sequence";

  private static final String METHOD_PREFIX = "selectSequence";
  static final String VALID_JAVA_METHOD_PATTERN = "[a-z][a-zA-Z0-9_$]*";

  // Properties

  private String name = null;
  private String javaMethodName = null;

  // Constructor

  public SequenceTag() {
    super("sequence");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-method-name")
  public void setJavaMethodName(final String javaMethodName) {
    this.javaMethodName = javaMethodName;
  }

  // Behavior

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

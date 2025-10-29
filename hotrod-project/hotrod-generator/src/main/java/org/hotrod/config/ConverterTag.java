package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.converter.TypeConverter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.TypesUtil;

@XmlRootElement(name = "converter")
public class ConverterTag extends AbstractConfigurationTag {

  // Constants

  private static final String NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String name = null;

  @Deprecated
  private String javaRawClass = null;
  private String rawClass = null;

  @Deprecated
  private String javaDomainClass = null;
  private String domainClass = null;

  private String converterClass = null;

  // Constructor

  public ConverterTag() {
    super("converter");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-raw-type")
  public void setJavaRawType(final String javaRawType) {
    this.javaRawClass = javaRawType;
  }

  @XmlAttribute(name = "raw-type")
  public void setRawType(final String rawType) {
    this.rawClass = rawType;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaDomainClass = javaType;
  }

  @XmlAttribute(name = "type")
  public void setType(final String type) {
    this.domainClass = type;
  }

  @XmlAttribute(name = "class")
  public void setJavaClass(final String javaClass) {
    this.converterClass = javaClass;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "You must specify a converter name.");
    }
    if (!this.name.matches(NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> must be a valid name. An alphanumeric name was expected but '" + this.name + "' was specified.");
    }

    // raw-type & java-raw-type

    if (this.javaRawClass == null) {
      if (this.rawClass == null) {
        throw new InvalidConfigurationFileException(this,
            "The attribute 'raw-type' of the tag <" + super.getTagName() + "> must be specified.");
      } else {

        // raw-type
        if (SUtil.isEmpty(this.rawClass)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'raw-type' of the tag <" + super.getTagName()
              + "> cannot be empty. It must specify the class for the raw type read from the database.");
        }
        if (!TypesUtil.isValidClassName(this.rawClass)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'raw-type' of tag <" + super.getTagName()
              + "> must be a valid full class name, but '" + this.rawClass + "' was specified.");
        }

      }
    } else if (this.rawClass != null) {
      throw new InvalidConfigurationFileException(this,
          "Either the attribute 'raw-type' or 'java-raw-type' of the tag <" + super.getTagName()
              + "> can be specified at the same time. " + "Use the former when possible.");

    } else {

      // java-raw-type
      if (SUtil.isEmpty(this.javaRawClass)) {
        throw new InvalidConfigurationFileException(this,
            "The attribute 'java-raw-type' of the tag <" + super.getTagName()
                + "> cannot be empty. It must specify the class for the raw type read from the database.");
      }

      if (!TypesUtil.isValidClassName(this.javaRawClass)) {
        throw new InvalidConfigurationFileException(this, "The attribute 'java-raw-type' of tag <" + super.getTagName()
            + "> must be a valid full class name, but '" + this.rawClass + "' was specified.");
      }
      this.rawClass = this.javaRawClass;
      this.javaRawClass = null;

    }

    // type & java-type

    if (this.javaDomainClass == null) {
      if (this.domainClass == null) {
        throw new InvalidConfigurationFileException(this,
            "The attribute 'type' of the tag <" + super.getTagName() + "> must be specified.");
      } else {

        // type
        if (SUtil.isEmpty(this.domainClass)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'type' of the tag <" + super.getTagName()
              + "> with name '" + this.name + "' cannot be empty.");
        }
        if (!TypesUtil.isValidClassName(this.domainClass)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'type' of the tag <" + super.getTagName()
              + "> must be a valid full class name, but '" + this.domainClass + "' was specified.");
        }

      }
    } else {
      if (this.domainClass != null) {
        throw new InvalidConfigurationFileException(this, "Either the attribute 'type' or 'java-type' of the tag <"
            + super.getTagName() + "> can be specified at the same time. " + "Use the former when possible.");

      } else {

        // java-type
        if (SUtil.isEmpty(this.javaDomainClass)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'java-type' of the tag <"
              + super.getTagName() + "> with name '" + this.name + "' cannot be empty.");
        }
        if (!TypesUtil.isValidClassName(this.javaDomainClass)) {
          throw new InvalidConfigurationFileException(this,
              "The attribute 'java-type' of the tag <" + super.getTagName() + "> must be a valid full class name, but '"
                  + this.domainClass + "' was specified.");
        }
        this.domainClass = this.javaDomainClass;
        this.javaDomainClass = null;

      }

    }

    // converter

    if (SUtil.isEmpty(this.converterClass)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'class' of tag <" + super.getTagName()
              + "> cannot be empty. Please specify a class that implements the " + TypeConverter.class.getName()
              + " interface.");
    }
    if (!TypesUtil.isValidClassName(this.converterClass)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'class' of tag <" + super.getTagName()
          + "> must be a valid full class name, but '" + this.converterClass + "' was specified.");
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  public String getRawClass() {
    return rawClass;
  }

  public String getDomainClass() {
    return domainClass;
  }

  public String getConverterClass() {
    return converterClass;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}

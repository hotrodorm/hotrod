package org.hotrod.config.structuredcolumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "expression")
public class ExpressionTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ExpressionTag.class);

  // Properties

  private String property = null;
  private String className = null;
  private String converter = null;

  private boolean isId = false;

  private ConverterTag converterTag;
  private transient StructuredColumnMetadata metadata;
  private String tempAlias = null;
  private String namespacedAlias;

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

  @XmlMixed
  private List<Object> content = new ArrayList<Object>();

  private String body;

  // Constructor

  public ExpressionTag() {
    super("expression");
  }

  // JAXB Setters

  @XmlAttribute(name = "property")
  public void setProperty(final String property) {
    this.property = property;
  }

  @XmlAttribute(name = "class")
  public void setClassName(final String className) {
    this.className = className;
  }

  @XmlAttribute(name = "converter")
  public void setConverter(final String converter) {
    this.converter = converter;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult, final Set<String> ids)
      throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: body

    this.body = null;
    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtil.isEmpty(s)) {
          if (this.body == null) {
            this.body = s;
          } else {
            this.body = this.body + " " + s;
          }
        }
      } catch (ClassCastException e1) {
        throw new InvalidConfigurationFileException(this, "The body of the tag <" + super.getTagName()
            + "> has an invalid tag of class " + obj.getClass().getName() + ".");
      }
    }

    // body

    if (this.body == null) {
      throw new InvalidConfigurationFileException(this, "Missing SQL expression in <" + super.getTagName() + "> tag. "
          + "An <" + super.getTagName() + "> tag must include a body with the SQL expression in it.");
    }
    cleanBody();
    if (SUtil.isEmpty(this.body)) {
      throw new InvalidConfigurationFileException(this, "Invalid empty <" + super.getTagName() + "> tag. " + "An <"
          + super.getTagName() + "> tag must include a non-empty body with the SQL expression in it.");
    }

    // property

    if (this.property == null) {
      throw new InvalidConfigurationFileException(this, "Missing 'property' attribute in <" + super.getTagName()
          + "> tag. " + "An <" + super.getTagName() + "> must specify the 'property' attribute.");
    }
    if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
      throw new InvalidConfigurationFileException(this,
          "Invalid property name '" + this.property + "' in the <" + super.getTagName()
              + "> tag. A Java property name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores.");
    }
    if (ids != null) {
      this.isId = ids.remove(this.property);
    }

    // class

    if (this.className != null) {
      if (!this.className.matches(Patterns.VALID_JAVA_TYPE)) {
        throw new InvalidConfigurationFileException(this,
            "Invalid class name '" + this.className + "' in the <" + super.getTagName()
                + "> tag. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // converter

    if (this.converter != null) {
      if (this.className != null) {
        throw new InvalidConfigurationFileException(this, "Invalid attribute 'class' of tag <" + super.getTagName()
            + ">: "
            + "the 'class' and 'converter' attributes are mutually exclusive, so only one of them can be specified in an <"
            + super.getTagName() + "> definition.");
      }
      if (SUtil.isEmpty(this.converter)) {
        throw new InvalidConfigurationFileException(this, "When specified, the attribute 'converter' of tag <"
            + super.getTagName() + "> cannot be empty. " + "Must specify a valid converter name.");
      }
      this.converterTag = config.getConverterTagByName(this.converter);
      if (this.converterTag == null) {
        throw new InvalidConfigurationFileException(this, "Converter '" + this.converter + "' not found.");
      }
    } else {
      this.converterTag = null;
    }

  }

  private void cleanBody() {
    String b = this.body.trim();
    while (b.startsWith(",") || b.endsWith(",")) {
      if (b.startsWith(",")) {
        b = b.substring(1).trim();
      }
      if (b.endsWith(",")) {
        b = b.substring(0, b.length() - 1).trim();
      }
    }
    this.body = b;
  }

  // Setters

  public void setTempAlias(final String tempAlias) {
    this.tempAlias = tempAlias;
  }

  public void setMetadata(final StructuredColumnMetadata metadata) {
    this.metadata = metadata;
  }

  // Getters

  public StructuredColumnMetadata getMetadata() {
    return this.metadata;
  }

  public String getProperty() {
    return property;
  }

  public String getClassName() {
    return className;
  }

  public String getConverter() {
    return converter;
  }

  ConverterTag getConverterTag() {
    return converterTag;
  }

  public String getBody() {
    return body;
  }

  public String getNamespacedAlias() {
    return namespacedAlias;
  }

  public String getTempAlias() {
    return tempAlias;
  }

  public boolean isId() {
    return isId;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}

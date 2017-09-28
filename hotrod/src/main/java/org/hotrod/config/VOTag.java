package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;

@XmlRootElement(name = "vo")
public class VOTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(VOTag.class);

  // Properties

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = CollectionTag.class), //
      @XmlElementRef(type = AssociationTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  private String table = null;
  private String view = null;
  private String property = null;
  private String prefix = null;
  private String extendedVOClass = null;
  private String body = null;
  private List<CollectionTag> collections = new ArrayList<CollectionTag>();
  private List<AssociationTag> associations = new ArrayList<AssociationTag>();

  // Constructors

  public VOTag() {
    super("vo");
  }

  protected VOTag(final String name) {
    super(name);
  }

  // JAXB Setters

  @XmlAttribute
  public void setTable(final String table) {
    this.table = table;
  }

  @XmlAttribute
  public void setView(final String view) {
    this.view = view;
  }

  @XmlAttribute
  public void setProperty(final String property) {
    this.property = property;
  }

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  @XmlAttribute(name = "extended-vo-class")
  public void setExtendedClass(final String extendedVOClass) {
    this.extendedVOClass = extendedVOClass;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVO) throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: content, collections, and associations

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (this.body == null) {
          this.body = "";
        }
        if (!SUtils.isEmpty(s)) {
          if (!this.body.isEmpty()) {
            this.body = this.body + " ";
          }
          this.body = this.body + s;
        }
      } catch (ClassCastException e1) {
        try {
          CollectionTag c = (CollectionTag) obj; // collection
          this.collections.add(c);
        } catch (ClassCastException e2) {
          try {
            AssociationTag a = (AssociationTag) obj; // association
            this.associations.add(a);
          } catch (ClassCastException e3) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
                + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
          }
        }
      }
    }

    // table & view

    if (this.table == null && this.view == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid <" + super.getTagName()
          + "> tag. Must specify the 'table' or the 'view' attribute, but none was specified.");
    }
    if (this.table != null && this.view != null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <" + super.getTagName() + "> tag. Cannot specify both the 'table' and 'view' attributes.");
    }
    if (this.table != null && SUtils.isEmpty(this.table)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'table' attribute on the <"
          + super.getTagName() + "> tag. When specified this attribute must not be empty.");
    }
    if (this.view != null && SUtils.isEmpty(this.view)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'view' attribute on the <"
          + super.getTagName() + "> tag. When specified this attribute must not be empty.");
    }

    // property

    if (this.property == null) {
      if (!singleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Missing 'property' attribute on <" + super.getTagName() + "> tag. This attribute can only be ommitted "
                + "when the <columns> tag includes a single '<vo> tag and no <expressions> tag.");
      }
    } else {
      if (singleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The 'property' attribute on the <" + super.getTagName()
                + "> tag should not be specified. This attribute should be ommitted "
                + "when the <columns> tag includes a single <vo> tag and no <expressions> tag.");
      }
      if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid property value '" + this.property + "' on <" + super.getTagName()
                + "> tag. A Java property name must start with a lower case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // prefix

    if (this.prefix != null) {
      if (SUtils.isEmpty(this.prefix)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "When specified the 'prefix' attribute should not be empty.");
      }
    }

    // extended-class

    if (this.extendedVOClass == null) {
      if (!this.associations.isEmpty() || !this.collections.isEmpty()) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The extended-vo-class attribute must be specified when the <" + super.getTagName()
                + "> tag includes one or more <association> and/or <colection> tags.");
      }
    } else {
      if (this.associations.isEmpty() && this.collections.isEmpty()) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The extended-vo-class attribute must not be specified when the <" + super.getTagName()
                + "> tag does not include any <association> or <colection> tags.");
      }
      if (!this.extendedVOClass.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid extended-vo-class value '" + this.extendedVOClass + "' on <" + super.getTagName()
                + "> tag. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // body

    if (this.body != null) {
      if (SUtils.isEmpty(this.body)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "When the tag <" + this.getTagName()
                + "> has a body, this body should include a comma-separated list of columns or a wild card (*). "
                + "To include all columns leave the body empty, or simply remove the body altogether.");
      }
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validate(daosTag, config, fragmentConfig, false);
    }

    // associations

    for (AssociationTag a : this.associations) {
      a.validate(daosTag, config, fragmentConfig, false);
    }

  }

  // Getters

  public String getTable() {
    return table;
  }

  public String getView() {
    return view;
  }

  public String getProperty() {
    return property;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getExtendedVOClass() {
    return extendedVOClass;
  }

  public String getBody() {
    return body;
  }

  public List<CollectionTag> getCollections() {
    return collections;
  }

  public List<AssociationTag> getAssociations() {
    return associations;
  }

}

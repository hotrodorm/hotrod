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
  private String extendedClass = null;
  private String body = "";
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

  @XmlAttribute(name = "extended-class")
  public void setExtendedClass(final String extendedClass) {
    this.extendedClass = extendedClass;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: content, collections, and associations

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
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
          + "> tag: must specify the 'table' or the 'view' attribute, but none was specified.");
    }
    if (this.table != null && this.view != null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <" + super.getTagName() + "> tag: cannot specify both the 'table' and 'view' attributes.");
    }

    // property

    if (this.property != null) {
      if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid property value '"
            + this.property + "' on <" + super.getTagName() + "> tag. Must be a valid Java property name.");
      }
    }

    // prefix -- nothing to validate

    // extended-class

    if (this.extendedClass != null) {
      if (!this.extendedClass.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid extended-class value '"
            + this.extendedClass + "' on <" + super.getTagName() + "> tag. Must be a valid Java class name.");
      }
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validate(daosTag, config, fragmentConfig);
    }

    // associations

    for (AssociationTag a : this.associations) {
      a.validate(daosTag, config, fragmentConfig);
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

  public String getExtendedClass() {
    return extendedClass;
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

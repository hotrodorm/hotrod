package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "foreign-key")
public class ForeignKeyTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ForeignKeyTag.class);

  static final String TAG_NAME = "foreign-key";

  // Properties

  private String parent = null;
  private String children = null;
  private String getParentMethod = null;
  private String getChildrenMethod = null;

  // Constructor

  public ForeignKeyTag() {
    super("foreign-key");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "parent")
  public void setParent(final String parent) {
    this.parent = parent;
  }

  @XmlAttribute(name = "children")
  public void setChildren(final String children) {
    this.children = children;
  }

  @XmlAttribute(name = "get-parent-method")
  public void setGetParentMethod(final String getParentMethod) {
    this.getParentMethod = getParentMethod;
  }

  @XmlAttribute(name = "get-children-method")
  public void setGetChildrenMethod(final String getChildrenMethod) {
    this.getChildrenMethod = getChildrenMethod;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // parent

    if (this.parent == null) {
      throw new InvalidConfigurationFileException(this, "Attribute 'parent' must be specified");
    }
    if (SUtil.isEmpty(this.parent)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'parent' cannot be empty or empty");
    }

    // children

    if (this.children == null) {
      throw new InvalidConfigurationFileException(this, "The 'children' must be specified");
    }
    if (SUtil.isEmpty(this.children)) {
      throw new InvalidConfigurationFileException(this, "The 'children' attribute cannot be empty");
    }

    // get-parent-method

    if (this.getParentMethod == null) {
      throw new InvalidConfigurationFileException(this, "The 'get-parent-method' must be specified");
    }
    if (SUtil.isEmpty(this.getParentMethod)) {
      throw new InvalidConfigurationFileException(this, "The 'get-parent-method' attribute cannot be empty");
    }

    // get-children-method

    if (this.getChildrenMethod == null) {
      throw new InvalidConfigurationFileException(this, "The 'get-children-method' must be specified");
    }
    if (SUtil.isEmpty(this.getChildrenMethod)) {
      throw new InvalidConfigurationFileException(this, "The 'get-children-method' attribute cannot be empty");
    }

  }

  // Getters

  public String getFKParent() {
    return parent;
  }

  public String getFKChildren() {
    return children;
  }

  public String getGetParentMethod() {
    return getParentMethod;
  }

  public String getGetChildrenMethod() {
    return getChildrenMethod;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + "-" + this.parent + "-" + this.children;
  }

}

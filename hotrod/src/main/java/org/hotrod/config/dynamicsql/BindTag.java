package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLPart {

  // Constructor

  public BindTag() {
    super("bind");
  }

  // Properties

  private String name = null;
  private String value = null;

  // Getters & Setters

  public String getName() {
    return name;
  }

  @XmlAttribute
  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  @XmlAttribute
  public void setValue(String value) {
    this.value = value;
  }

  // Rendering

  @Override
  public String render() {
    return super.renderEmptyTag( //
        new TagAttribute("name", this.name), //
        new TagAttribute("value", this.value) //
    );
  }

}
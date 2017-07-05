package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLTag {

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
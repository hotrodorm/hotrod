package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "if")
public class IfTag extends DynamicSQLPart {

  // Constructor

  public IfTag() {
    super("if");
  }

  // Properties

  private String test = null;

  // Getters & Setters

  public String getTest() {
    return test;
  }

  @XmlAttribute
  public void setTest(final String test) {
    this.test = test;
  }

  // Rendering

  @Override
  public String render() {
    return super.renderTag(new TagAttribute("test", this.test));
  }

}
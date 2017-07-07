package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "when")
public class WhenTag extends DynamicSQLPart {

  // Constructor

  public WhenTag() {
    super("when");
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

  @Override
  public String render() {
    return super.renderTag(new TagAttribute("test", this.test));
  }

}
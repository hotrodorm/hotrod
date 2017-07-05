package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement(name = "if")
public class IfTag extends DynamicSQLTag {

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
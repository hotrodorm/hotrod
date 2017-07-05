package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement(name = "when")
public class WhenTag extends DynamicSQLTag {

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
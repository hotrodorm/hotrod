package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "set")
public class SetTag extends DynamicSQLTag {

  // Constructor

  public SetTag() {
    super("set");
  }

  // Properties

  // Getters & Setters

  // Rendering

  @Override
  public String render() {
    return super.renderTag();
  }

}
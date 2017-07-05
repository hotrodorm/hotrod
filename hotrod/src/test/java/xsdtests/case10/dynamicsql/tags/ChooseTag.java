package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "choose")
public class ChooseTag extends DynamicSQLTag {

  // Constructor

  public ChooseTag() {
    super("choose");
  }

  // Properties

  // Getters & Setters

  // Rendering

  @Override
  public String render() {
    return super.renderTag();
  }

}
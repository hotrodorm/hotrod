package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLTag {

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  // Getters & Setters

  // Rendering

  @Override
  public String render() {
    return super.renderTag();
  }

}
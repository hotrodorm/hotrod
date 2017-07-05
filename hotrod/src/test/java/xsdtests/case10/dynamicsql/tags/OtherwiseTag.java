package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "otherwise")
public class OtherwiseTag extends DynamicSQLTag {

  // Constructor

  public OtherwiseTag() {
    super("otherwise");
  }

  @Override
  public String render() {
    return super.renderTag();
  }

}
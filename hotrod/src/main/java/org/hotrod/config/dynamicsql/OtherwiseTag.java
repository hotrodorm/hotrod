package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "otherwise")
public class OtherwiseTag extends DynamicSQLPart {

  // Constructor

  public OtherwiseTag() {
    super("otherwise");
  }

  @Override
  public String render() {
    return super.renderTag();
  }

}
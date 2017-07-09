package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "complement")
public class ComplementTag extends DynamicSQLPart {

  // Constructor

  public ComplementTag() {
    super("complement");
  }

  @Override
  public String render() {
    return super.renderTag();
  }

}
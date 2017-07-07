package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

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
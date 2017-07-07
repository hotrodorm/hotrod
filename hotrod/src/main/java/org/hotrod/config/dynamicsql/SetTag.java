package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "set")
public class SetTag extends DynamicSQLPart {

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
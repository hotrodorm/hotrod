package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "choose")
public class ChooseTag extends DynamicSQLPart {

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
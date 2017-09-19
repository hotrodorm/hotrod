package org.hotrod.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "columns")
public class ColumnsTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsTag.class);

  // Properties

  // Constructor

  public ColumnsTag() {
    super("columns");
    log.debug("init");
  }

  // JAXB Setters

  // Behavior

  // Getters

}

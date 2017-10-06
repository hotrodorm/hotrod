package org.hotrod.config.sqlcolumns;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "association")
public class AssociationTag extends VOTag {

  // Constants

  private static final Logger log = Logger.getLogger(AssociationTag.class);

  // Properties

  // Constructor

  public AssociationTag() {
    super("association");
    log.debug("init");
  }

  // JAXB Setters

  // Behavior

  // Getters

}

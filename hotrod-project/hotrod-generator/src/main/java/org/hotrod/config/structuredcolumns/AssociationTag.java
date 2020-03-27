package org.hotrod.config.structuredcolumns;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement(name = "association")
public class AssociationTag extends VOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(AssociationTag.class);

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

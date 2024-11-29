package org.hotrod.config.structuredcolumns;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "association")
public class AssociationTag extends VOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(AssociationTag.class.getName());

  // Properties

  // Constructor

  public AssociationTag() {
    super("association");
    log.fine("init");
  }

  // JAXB Setters

  // Behavior

  // Getters

}

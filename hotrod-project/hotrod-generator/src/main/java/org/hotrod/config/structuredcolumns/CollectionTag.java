package org.hotrod.config.structuredcolumns;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "collection")
public class CollectionTag extends VOTag {

  // Constants

  private static final Logger log = Logger.getLogger(CollectionTag.class.getName());

  // Properties

  // Constructor

  public CollectionTag() {
    super("collection");
    log.fine("init");
  }

  // JAXB Setters

  // Behavior

  // Getters

}

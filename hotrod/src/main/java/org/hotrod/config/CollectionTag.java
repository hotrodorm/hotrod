package org.hotrod.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "collection")
public class CollectionTag extends VOTag {

  // Constants

  private static final Logger log = Logger.getLogger(CollectionTag.class);

  // Properties

  // Constructor

  public CollectionTag() {
    super("collection");
    log.debug("init");
  }

  // JAXB Setters

  // Behavior

  // Getters

}

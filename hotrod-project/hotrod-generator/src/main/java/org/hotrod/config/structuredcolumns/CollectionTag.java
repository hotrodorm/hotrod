package org.hotrod.config.structuredcolumns;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement(name = "collection")
public class CollectionTag extends VOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(CollectionTag.class);

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

package org.hotrod.exceptions;

import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(InvalidConfigurationFileException.class.getName());

  private AbstractConfigurationTag tag;

  public InvalidConfigurationFileException(final AbstractConfigurationTag tag, final String message) {
    super(message);
    log.fine("init");
    intialize(tag);
  }

  private void intialize(final AbstractConfigurationTag tag) {
    this.tag = tag;
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

}

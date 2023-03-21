package org.hotrod.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(InvalidConfigurationFileException.class);

  private AbstractConfigurationTag tag;

  public InvalidConfigurationFileException(final AbstractConfigurationTag tag, final String message) {
    super(message);
    log.debug("init");
    intialize(tag);
  }

  private void intialize(final AbstractConfigurationTag tag) {
    this.tag = tag;
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

}

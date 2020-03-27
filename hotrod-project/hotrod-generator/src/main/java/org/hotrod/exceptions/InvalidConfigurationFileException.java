package org.hotrod.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(InvalidConfigurationFileException.class);

  private AbstractConfigurationTag tag;
  private String interactiveMessage;

  public InvalidConfigurationFileException(final AbstractConfigurationTag tag, final String batchMessage) {
    super(batchMessage);
    intialize(tag, batchMessage);
  }

  public InvalidConfigurationFileException(final AbstractConfigurationTag tag, final String interactiveMessage,
      final String batchMessage) {
    super(batchMessage);
    intialize(tag, interactiveMessage);
  }

  private void intialize(final AbstractConfigurationTag tag, final String interactiveMessage) {
    if (tag == null) {
      throw new IllegalArgumentException("tag cannot be null");
    }
    log.debug("  interactiveMessage=" + interactiveMessage + "\n  loc=" + tag.getSourceLocation().render());
    log.debug("  parent=" + tag.getParent());
    tag.setErrorMessage(interactiveMessage);
    this.interactiveMessage = interactiveMessage;
    this.tag = tag;
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

  public String getInteractiveMessage() {
    return this.interactiveMessage;
  }

}

package org.hotrod.exceptions;

import org.hotrod.config.AbstractConfigurationTag;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private AbstractConfigurationTag tag;
  private String notice;

  public InvalidConfigurationFileException(final AbstractConfigurationTag tag, final String interactiveMessage,
      final String batchMessage) {
    super(batchMessage);
    if (tag == null) {
      throw new IllegalArgumentException("tag cannot be null");
    }
    tag.setErrorMessage(interactiveMessage);
    this.notice = interactiveMessage;
    this.tag = tag;
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

  public String getNotice() {
    return this.notice;
  }

}

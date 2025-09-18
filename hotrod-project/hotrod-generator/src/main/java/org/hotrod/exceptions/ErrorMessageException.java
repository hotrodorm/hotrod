package org.hotrod.exceptions;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.utils.SourceLocation;

public class ErrorMessageException extends Exception {

  private static final long serialVersionUID = 2L;

  private SourceLocation location = null;

  public ErrorMessageException(final String message) {
    super(message);
  }

  public ErrorMessageException(final SourceLocation location, final String message) {
    super(message);
    this.location = location;
  }

  public ErrorMessageException(final AbstractConfigurationTag tag, final String message) {
    super(message);
    this.location = tag == null ? null : tag.getSourceLocation();
  }

  public String renderErrorMessage(String prompt) {
    if (this.location == null) {
      return prompt + "\n" + this.getMessage();
    } else {
      return prompt + ": invalid configuration in " + this.location.render() + ":\n" + this.getMessage();
    }
  }

}

package org.hotrod.exceptions;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.utils.SourceLocation;

public class FaultException extends Exception {

  private static final long serialVersionUID = 1L;

  private SourceLocation location;

  public FaultException(String message, Throwable cause) {
    super(message, cause);
    this.location = null;
  }

  public FaultException(final AbstractConfigurationTag tag, String message, Throwable cause) {
    super(message, cause);
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

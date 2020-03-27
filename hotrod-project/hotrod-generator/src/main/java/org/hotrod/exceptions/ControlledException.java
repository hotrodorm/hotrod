package org.hotrod.exceptions;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public class ControlledException extends Exception {

  private static final long serialVersionUID = 2L;

  private SourceLocation location = null;
  private String interactiveMessage = null;

  public ControlledException(final String message) {
    super(message);
  }

  public ControlledException(final String message, final String interactiveMessage) {
    super(message);
    this.interactiveMessage = interactiveMessage;
  }

  public ControlledException(final SourceLocation location, final String message) {
    super(message);
    this.location = location;
  }

  public ControlledException(final SourceLocation location, final String message, final String interactiveMessage) {
    super(message);
    this.location = location;
    this.interactiveMessage = interactiveMessage;
  }

  public SourceLocation getLocation() {
    return location;
  }

  public String getInteractiveMessage() {
    return this.interactiveMessage != null ? this.interactiveMessage : this.getMessage();
  }

}

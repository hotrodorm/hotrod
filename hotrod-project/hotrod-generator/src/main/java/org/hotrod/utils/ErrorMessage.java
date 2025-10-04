package org.hotrod.utils;

public class ErrorMessage {

  private SourceLocation location;
  private String message;

  public ErrorMessage(final SourceLocation location, final String message) {
    this.location = location;
    this.message = message;
  }

  // Getters

  public SourceLocation getLocation() {
    return location;
  }

  public String getMessage() {
    return message;
  }

  // toString

  public String toString() {
    return (this.location == null ? ""
        : this.location.getFile().getName() + "(" + this.location.getLineNumber() + ","
            + this.location.getColumnNumber() + "): ")
        + this.message;
  }

}

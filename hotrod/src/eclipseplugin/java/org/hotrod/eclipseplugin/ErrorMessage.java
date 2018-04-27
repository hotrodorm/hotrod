package org.hotrod.eclipseplugin;

import org.hotrod.runtime.dynamicsql.SourceLocation;

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

}

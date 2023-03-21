package org.hotrod.exceptions;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public class ControlledException extends Exception {

  private static final long serialVersionUID = 2L;

  private SourceLocation location = null;

  public ControlledException(final String message) {
    super(message);
  }

  public ControlledException(final SourceLocation location, final String message) {
    super(message);
    this.location = location;
  }

  public SourceLocation getLocation() {
    return location;
  }

}

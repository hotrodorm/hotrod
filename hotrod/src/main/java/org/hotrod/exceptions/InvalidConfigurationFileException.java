package org.hotrod.exceptions;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private SourceLocation sourceLocation;

  public InvalidConfigurationFileException(final SourceLocation sourceLocation, final String message) {
    super(message);
    this.sourceLocation = sourceLocation;
  }

  public SourceLocation getSourceLocation() {
    return sourceLocation;
  }

}

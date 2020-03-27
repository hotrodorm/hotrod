package org.hotrod.runtime.exceptions;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public class InvalidJavaExpressionException extends Exception {

  private static final long serialVersionUID = 1L;

  private SourceLocation sourceLocation;

  public InvalidJavaExpressionException(final SourceLocation sourceLocation, final String message) {
    super(message);
    this.sourceLocation = sourceLocation;
  }

  public SourceLocation getSourceLocation() {
    return sourceLocation;
  }

}

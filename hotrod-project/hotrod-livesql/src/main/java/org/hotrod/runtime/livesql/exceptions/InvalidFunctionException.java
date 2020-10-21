package org.hotrod.runtime.livesql.exceptions;

public class InvalidFunctionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidFunctionException(final String message) {
    super(message);
  }

}

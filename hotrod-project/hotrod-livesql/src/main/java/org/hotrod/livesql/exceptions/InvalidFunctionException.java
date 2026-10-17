package org.hotrod.livesql.exceptions;

public class InvalidFunctionException extends LiveSQLException {

  private static final long serialVersionUID = 1L;

  public InvalidFunctionException(final String message) {
    super(message);
  }

}

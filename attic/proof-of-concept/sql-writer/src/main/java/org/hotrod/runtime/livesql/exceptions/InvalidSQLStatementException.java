package org.hotrod.runtime.livesql.exceptions;

public class InvalidSQLStatementException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidSQLStatementException(String message) {
    super(message);
  }

}

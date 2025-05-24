package org.hotrod.livesql.exceptions;

public class InvalidLiveSQLStatementException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidLiveSQLStatementException(String message) {
    super(message);
  }

}

package org.hotrod.runtime.livesql.exceptions;

public class InvalidLiteralException extends InvalidLiveSQLStatementException {

  private static final long serialVersionUID = 1L;

  public InvalidLiteralException(String message) {
    super(message);
  }

}

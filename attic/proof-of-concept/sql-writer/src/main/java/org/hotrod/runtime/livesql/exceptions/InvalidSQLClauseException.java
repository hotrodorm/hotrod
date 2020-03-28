package org.hotrod.runtime.livesql.exceptions;

public class InvalidSQLClauseException extends InvalidSQLStatementException {

  private static final long serialVersionUID = 1L;

  public InvalidSQLClauseException(String message) {
    super(message);
  }

}

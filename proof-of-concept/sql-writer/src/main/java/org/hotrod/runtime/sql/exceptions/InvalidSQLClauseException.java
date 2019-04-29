package org.hotrod.runtime.sql.exceptions;

public class InvalidSQLClauseException extends InvalidSQLStatementException {

  private static final long serialVersionUID = 1L;

  public InvalidSQLClauseException(String message) {
    super(message);
  }

}

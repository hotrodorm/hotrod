package org.hotrod.livesql.exceptions;

public class InvalidLiveSQLClauseException extends InvalidLiveSQLStatementException {

  private static final long serialVersionUID = 1L;

  public InvalidLiveSQLClauseException(String message) {
    super(message);
  }

}

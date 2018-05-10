package org.hotrod.exceptions;

public class InvalidSQLException extends Exception {

  private static final long serialVersionUID = 1L;

  private String invalidSQL;

  public InvalidSQLException(final String invalidSQL, final Throwable cause) {
    super(cause);
    this.invalidSQL = invalidSQL;
  }

  public String getInvalidSQL() {
    return invalidSQL;
  }

}

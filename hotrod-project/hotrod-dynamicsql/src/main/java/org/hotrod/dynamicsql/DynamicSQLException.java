package org.hotrod.dynamicsql;

public class DynamicSQLException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DynamicSQLException(String message) {
    super(message);
  }

  public DynamicSQLException(String message, Throwable cause) {
    super(message, cause);
  }

}

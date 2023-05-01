package org.hotrod.runtime.livesql.exceptions;

public class LiveSQLException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public LiveSQLException(String message, Throwable cause) {
    super(message, cause);
  }

}

package org.hotrod.exceptions;

public class UnrecognizedDatabaseException extends Exception {

  private static final long serialVersionUID = 1L;

  public UnrecognizedDatabaseException(final String message) {
    super(message);
  }

}

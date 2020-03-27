package org.hotrod.exceptions;

public class UncontrolledException extends Exception {

  private static final long serialVersionUID = 1L;

  public UncontrolledException(final String message, final Throwable cause) {
    super(message, cause);
  }

}

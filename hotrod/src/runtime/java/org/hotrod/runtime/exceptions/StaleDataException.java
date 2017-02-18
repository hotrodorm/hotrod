package org.hotrod.runtime.exceptions;

public class StaleDataException extends Exception {

  private static final long serialVersionUID = 1L;

  public StaleDataException(String message) {
    super(message);
  }

}

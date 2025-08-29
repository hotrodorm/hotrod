package org.hotrod.exceptions;

public class StaleDataException extends PersistenceException {

  private static final long serialVersionUID = 1L;

  public StaleDataException(String message) {
    super(message);
  }

}

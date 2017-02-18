package org.hotrod.exceptions;

public class InvalidConfigurationFileException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidConfigurationFileException(final String message) {
    super(message);
  }

}

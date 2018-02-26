package org.hotrod.domain.loader;

public class UnreadableConfigFileException extends Exception {

  private static final long serialVersionUID = 1L;

  public UnreadableConfigFileException(final String message) {
    super(message);
  }

}

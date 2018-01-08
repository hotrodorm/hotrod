package org.hotrod.eclipseplugin.domain.loader;

public class FaultyConfigFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private int lineNumber;

  public FaultyConfigFileException(final int lineNumber, final String message) {
    super(message);
    this.lineNumber = lineNumber;
  }

  public int getLineNumber() {
    return lineNumber;
  }

}

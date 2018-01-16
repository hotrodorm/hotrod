package org.hotrod.eclipseplugin.domain.loader;

public class FaultyConfigFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private String fileName;
  private int lineNumber;

  public FaultyConfigFileException(final String fileName, final int lineNumber, final String message) {
    super(message);
    this.lineNumber = lineNumber;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLineNumber() {
    return lineNumber;
  }

}

package org.hotrod.eclipseplugin.domain.loader;

import org.hotrod.eclipseplugin.domain.loader.FaceProducer.RelativeProjectPath;

public class FaultyConfigFileException extends Exception {

  private static final long serialVersionUID = 1L;

  private RelativeProjectPath path;
  private int lineNumber;

  public FaultyConfigFileException(final RelativeProjectPath path, final int lineNumber, final String message) {
    super(message);
    this.path = path;
    this.lineNumber = lineNumber;
  }

  public RelativeProjectPath getPath() {
    return path;
  }

  public int getLineNumber() {
    return lineNumber;
  }

}

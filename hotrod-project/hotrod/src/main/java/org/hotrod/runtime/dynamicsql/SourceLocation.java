package org.hotrod.runtime.dynamicsql;

import java.io.File;
import java.io.Serializable;

public class SourceLocation implements Serializable {

  private static final long serialVersionUID = 1L;

  private File file;
  private int lineNumber;
  private int columnNumber;
  private int characterOffset;

  public SourceLocation(final File file, final int lineNumber, final int columnNumber, final int characterOffset) {
    this.file = file;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.characterOffset = characterOffset;

  }

  public File getFile() {
    return this.file;
  }

  public int getLineNumber() {
    return this.lineNumber;
  }

  public int getColumnNumber() {
    return this.columnNumber;
  }

  public int getCharacterOffset() {
    return characterOffset;
  }

  public String render() {
    return "file '" + this.file.getPath() + "' (line " + this.lineNumber + ", col " + this.columnNumber + ")";
  }

  public String toString() {
    return "[file '" + this.file.getPath() + "': line " + this.lineNumber + ", col " + this.columnNumber + " (char "
        + this.characterOffset + ")]";
  }

}

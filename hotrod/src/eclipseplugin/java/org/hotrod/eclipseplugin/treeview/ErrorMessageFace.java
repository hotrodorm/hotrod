package org.hotrod.eclipseplugin.treeview;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public class ErrorMessageFace extends AbstractFace {

  private SourceLocation location;
  private String message;

  public ErrorMessageFace(final SourceLocation location, final String message) {
    super(location != null ? (location.getFile().getName() + " (line " + location.getLineNumber() + ", col "
        + location.getColumnNumber() + "):\n") : "" + message, null);
    // System.out.println("[ErrorMessageFace] message=" + message);
    this.location = location;
    this.message = message;
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query6-16.png";
    return "eclipse-plugin/icons/transparent.png";
  }

  @Override
  public String getDecoration() {
    return "";
  }

  @Override
  public String getTooltip() {
    return "Click to error source";
  }

  // Extra getters

  // public IProject getProject() {
  // return this.path.getProject();
  // }
  //
  // public String getAbsolutePath() {
  // IPath location = this.path.getProject().getLocation();
  // File projectDir = location.toFile();
  // File f = new File(projectDir, this.path.getRelativeFileName());
  // return f.getAbsolutePath();
  // }

  // public int getLineNumber() {
  // return line;
  // }

  public SourceLocation getLocation() {
    return location;
  }

  public String getMessage() {
    return message;
  }

}

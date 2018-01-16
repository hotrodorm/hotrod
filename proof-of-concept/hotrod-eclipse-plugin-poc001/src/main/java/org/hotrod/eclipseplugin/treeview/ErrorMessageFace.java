package org.hotrod.eclipseplugin.treeview;

import org.eclipse.core.resources.IProject;

public class ErrorMessageFace extends AbstractMethodFace {

  private IProject project;
  private String file;
  private int line;
  private String message;

  public ErrorMessageFace(final IProject project, final String file, final int line, final String message) {
    super("Error at " + file + ":" + line + ":\n" + message);
    this.project = project;
    this.file = file;
    this.line = line;
    this.message = message;
  }

  @Override
  public String getIconPath() {
    return "icons/sql-query6-16.png";
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

  public IProject getProject() {
    return project;
  }

  public String getFile() {
    return file;
  }

  public int getLineNumber() {
    return line;
  }

  public String getMessage() {
    return message;
  }

}

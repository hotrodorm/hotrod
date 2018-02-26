package org.hotrod.eclipseplugin.treeview;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.hotrod.eclipseplugin.FaceProducer.RelativeProjectPath;

public class ErrorMessageFace extends AbstractFace {

  private RelativeProjectPath path;

  private int line;
  private String message;

  public ErrorMessageFace(final RelativeProjectPath path, final int line, final String message) {
    super(path.getRelativeFileName() + ":" + line + ":\n" + message, null);
    // System.out.println("[ErrorMessageFace] message=" + message);
    this.path = path;
    this.line = line;
    this.message = message;
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query6-16.png";
    return "icons/transparent.png";
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
    return this.path.getProject();
  }

  public String getAbsolutePath() {
    IPath location = this.path.getProject().getLocation();
    File projectDir = location.toFile();
    File f = new File(projectDir, this.path.getRelativeFileName());
    return f.getAbsolutePath();
  }

  public int getLineNumber() {
    return line;
  }

  public String getMessage() {
    return message;
  }

}

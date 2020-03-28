package org.hotrod.eclipseplugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

public class RelativeProjectPath {

  private static final Logger log = Logger.getLogger(RelativeProjectPath.class);

  private IProject project; // the project
  private String relativePath; // the relative path (only folders)
  private String fileName; // the simple file name

  public RelativeProjectPath(final IProject project, final String relativePath, final String fileName) {
    this.project = project;
    this.relativePath = relativePath;
    this.fileName = fileName;
    log.debug("***    relativePath=" + relativePath);
  }

  public IProject getProject() {
    return project;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public String getFileName() {
    return fileName;
  }

  public String getRelativeFileName() {
    File file;
    if (this.relativePath == null || this.relativePath.isEmpty()) {
      file = new File(this.fileName);
    } else {
      File folder = new File(this.relativePath);
      file = new File(folder, this.fileName);
    }
    log.debug(
        "/// this.relativePath=" + this.relativePath + " this.fileName=" + this.fileName + " file=" + file.getPath());
    return file.getPath();
  }

  // Utilities

  public static RelativeProjectPath findRelativePath(final IProject project, final File f) {
    String parentPath = f.getParent();
    String fileName = f.getName();

    IPath projectIPath = project.getLocation();
    String projectPath = projectIPath.toFile().getAbsolutePath();
    String head = projectPath + File.separator;
    if (parentPath.equals(projectPath)) {
      return new RelativeProjectPath(project, "", fileName);
    }
    if (parentPath.startsWith(head)) {
      String relativePath = parentPath.substring(head.length());
      return new RelativeProjectPath(project, relativePath, fileName);
    }
    return null;
  }

  public static RelativeProjectPath findProjectPath(final File f) {
    String parentPath = f.getParent();
    String fileName = f.getName();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    for (IProject project : workspace.getRoot().getProjects()) {
      if (project.exists() && project.isOpen()) {
        IPath projectIPath = project.getLocation();
        String projectPath = projectIPath.toFile().getAbsolutePath();
        String head = projectPath + File.separator;
        log.debug(">>> projectPath=" + projectPath);
        if (parentPath.equals(projectPath)) {
          log.debug(">>> -> equals");
          return new RelativeProjectPath(project, "", fileName);
        }
        if (parentPath.startsWith(head)) {
          log.debug(">>> -> starts");
          String relativePath = parentPath.substring(head.length());
          return new RelativeProjectPath(project, relativePath, fileName);
        }
      }
    }
    return null;
  }

  public String toString() {
    return "[ " + this.project.getLocation().toFile().getPath() + " ! " + this.relativePath + " / " + this.fileName
        + " ]";
  }

}

package org.hotrod.eclipseplugin;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.hotrod.domain.MainConfigFile;
import org.hotrod.domain.loader.FaultyConfigFileException;
import org.hotrod.eclipseplugin.treeview.ErrorMessageFace;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class FaceProducer {

  public static MainConfigFace load(final HotRodViewContentProvider provider, final File f) {

    RelativeProjectPath path = RelativeProjectPath.findProjectPath(f);
    if (path == null) {
      return null;
    }
    log("*** f=" + f.getPath() + " path.getRelativePathName()=" + path.getRelativeFileName());

    // Load the file

    MainConfigFile config = null;
    try {
      config = ConfigFileLoader.loadMainFile(f, path);
      log("*** main file loaded: path=");
      return new MainConfigFace(f, path, provider, config);
    } catch (FaultyConfigFileException e) {
      log("*** FaultyConfigFileException: path=" + e.getPath());
      return new MainConfigFace(f, path, provider,
          new ErrorMessageFace(e.getPath(), e.getLineNumber(), e.getMessage()));
    }

    // Assemble the view

    // // // TODO: remove
    // if (f.getName().equals("hotrod-1.xml")) {
    // log("MODIFYING...");
    // // mainConfigFace.getChildren().get(0).setDeleted();
    // mainConfigFace.getChildren()[0].setDeleted();
    // // mainConfigFace.getChildren()[1].setModified();
    // // mainConfigFace.getChildren()[2].setAdded();
    // // mainConfigFace.getChildren()[2].setGenerating(true);
    // }

  }

  // Utilities

  public static class RelativeProjectPath {

    private IProject project; // the project
    private String relativePath; // the relative path (only folders)
    private String fileName; // the simple file name

    public RelativeProjectPath(final IProject project, final String relativePath, final String fileName) {
      this.project = project;
      this.relativePath = relativePath;
      this.fileName = fileName;
      log("***    relativePath=" + relativePath);
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
      log("/// this.relativePath=" + this.relativePath + " this.fileName=" + this.fileName + " file=" + file.getPath());
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
          log(">>> projectPath=" + projectPath);
          if (parentPath.equals(projectPath)) {
            log(">>> -> equals");
            return new RelativeProjectPath(project, "", fileName);
          }
          if (parentPath.startsWith(head)) {
            log(">>> -> starts");
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

  public static void log(final String txt) {
    System.out.println("[" + FaceProducer.class.getName() + "] " + txt);
  }

}

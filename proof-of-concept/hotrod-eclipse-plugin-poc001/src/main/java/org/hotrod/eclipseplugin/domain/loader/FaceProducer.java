package org.hotrod.eclipseplugin.domain.loader;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.MainConfigFile;
import org.hotrod.eclipseplugin.treeview.AbstractFace;
import org.hotrod.eclipseplugin.treeview.FaceFactory;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class FaceProducer {

  public static MainConfigFace load(final HotRodViewContentProvider provider, final File f) {

    RelativeProjectPath path = RelativeProjectPath.findProjectPath(f);
    if (path == null) {
      return null;
    }

    MainConfigFace mainConfigFace = new MainConfigFace(f, path, provider);

    // Load the file

    MainConfigFile config;
    try {
      config = ConfigFileLoader.loadMainFile(f, path);
      mainConfigFace.setValid(true);
    } catch (UnreadableConfigFileException e) {
      mainConfigFace.setValid(false);
      return mainConfigFace;
    } catch (FaultyConfigFileException e) {
      mainConfigFace.setValid(false);
      return mainConfigFace;
    }

    // Assemble the view

    for (ConfigItem item : config.getConfigItems()) {
      try {
        AbstractFace face = FaceFactory.getFace(item);
        mainConfigFace.addChild(face);
      } catch (InvalidConfigurationItemException e) {
        mainConfigFace = new MainConfigFace(f, path, provider);
        mainConfigFace.setValid(false);
        return mainConfigFace;
      }
    }
    mainConfigFace.setValid(true);
    mainConfigFace.setUnchanged();

    // // TODO: remove
     if (f.getName().equals("hotrod-1.xml")) {
     System.out.println("MODIFYING...");
//     mainConfigFace.getChildren().get(0).setDeleted();
     mainConfigFace.getChildren()[0].setDeleted();
//     mainConfigFace.getChildren()[1].setModified();
//     mainConfigFace.getChildren()[2].setAdded();
//     mainConfigFace.getChildren()[2].setGenerating(true);
     }

    return mainConfigFace;
  }

  // Utilities

  public static class RelativeProjectPath {

    private IProject project;
    private String relativePath;
    private String fileName;

    public RelativeProjectPath(final IProject project, final String relativePath, final String fileName) {
      this.project = project;
      this.relativePath = relativePath;
      this.fileName = fileName;
      System.out.println("***    relativePath=" + relativePath);
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
          if (parentPath.equals(projectPath)) {
            return new RelativeProjectPath(project, "", fileName);
          }
          if (parentPath.startsWith(head)) {
            String relativePath = parentPath.substring(head.length());
            return new RelativeProjectPath(project, relativePath, fileName);
          }
        }
      }
      return null;
    }

  }

}

package org.hotrod.eclipseplugin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.hotrod.eclipseplugin.FileSystemChangesListener.FileChangeListener;
import org.hotrod.eclipseplugin.domain.loader.FaceProducer;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class LoadedConfigurationFiles implements FileChangeListener {

  private static final String VALID_HOTROD_EXTENSION = ".xml";

  private HotRodViewContentProvider provider;
  private Map<String, MainConfigFace> loadedFiles = new TreeMap<String, MainConfigFace>();

  // Constructor

  public LoadedConfigurationFiles(final HotRodViewContentProvider provider) {
    super();
    this.provider = provider;
  }

  // File changes

  public void addFile(final File f) {
    if (f != null && f.getName().endsWith(VALID_HOTROD_EXTENSION) && f.isFile()) {
      String absolutePath = f.getAbsolutePath();
      if (!this.loadedFiles.containsKey(absolutePath)) {
        // log("adding file: " + absolutePath);
        MainConfigFace face = FaceProducer.load(this.provider, f);
        // log("face '" + absolutePath + "' [" + face.getPath() +
        // "]: valid=" + face.isValid());
        if (face != null) {
          this.loadedFiles.put(absolutePath, face);
          this.provider.refresh();

          // TODO: remove
          // Create a file
          try {
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("project002");
            String name = "f-" + System.currentTimeMillis() + ".txt";
            IFile ifile = project.getFile(name);

            byte[] bytes = "File contents".getBytes();
            InputStream source = new ByteArrayInputStream(bytes);
            ifile.create(source, IResource.NONE, null);
          } catch (CoreException e) {
            e.printStackTrace();
          }

          // TODO: remove
          // update a file
          try {
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("project002");
            String name = "existing-file.txt";
            IFile ifile = project.getFile(name);

            byte[] bytes = ("File contents at " + System.currentTimeMillis()).getBytes();
            InputStream source = new ByteArrayInputStream(bytes);
            ifile.setContents(source, IResource.FORCE, null);
          } catch (CoreException e) {
            e.printStackTrace();
          }

        }
      }
    }
  }

  public void remove(final MainConfigFace face) {
    this.loadedFiles.remove(face.getAbsolutePath());
  }

  public void reload(final MainConfigFace existingFace) {
    String absolutePath = existingFace.getAbsolutePath();
    File f = new File(absolutePath);
    MainConfigFace newFace = FaceProducer.load(this.provider, f);
    existingFace.applyChangesFrom(newFace);
  }

  public void removeAll() {
    this.loadedFiles.clear();
  }

  // Getters

  public List<MainConfigFace> getLoadedFiles() {
    ArrayList<MainConfigFace> list = new ArrayList<MainConfigFace>(loadedFiles.values());
    Collections.sort(list);
    return list;
  }

  // FileChangesListener

  @Override
  public boolean informFileAdded(final File f) {
    log("  --> received file added: " + f.getAbsolutePath());
    // Ignore new file
    return false;
  }

  @Override
  public boolean informFileRemoved(final File f) {
    log("  --> received file removed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    printLoadedFiles();
    if (this.loadedFiles.containsKey(fullPathName)) {
      log("  >> Found to remove");
      this.remove(this.loadedFiles.get(fullPathName));
      return true;
    } else {
      log("  >> NOT Found to remove");
      return false;
    }
  }

  @Override
  public boolean informFileChanged(final File f) {
    log("  --> received file changed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    if (this.loadedFiles.containsKey(fullPathName)) {
      this.reload(this.loadedFiles.get(fullPathName));
      return true;
    }
    return false;
  }

  private void printLoadedFiles() {
    for (String p : this.loadedFiles.keySet()) {
      log("## currently loaded: " + p);
    }
  }

  private void log(final String txt) {
    // System.out.println(txt);
  }

}

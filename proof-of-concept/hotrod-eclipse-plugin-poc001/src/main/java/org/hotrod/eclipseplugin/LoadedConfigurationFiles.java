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
        // System.out.println("adding file: " + absolutePath);
        MainConfigFace face = FaceProducer.load(this.provider, f);
        // System.out.println("face '" + absolutePath + "' [" + face.getPath() +
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
    this.provider.refresh();
  }

  public void reload(final MainConfigFace face) {
    File f = new File(face.getAbsolutePath());
    MainConfigFace nf = FaceProducer.load(this.provider, f);
    if (nf != null) {
      this.remove(face);
      this.loadedFiles.put(nf.getAbsolutePath(), nf);
      this.provider.refresh();
    }
  }

  public void removeAll() {
    this.loadedFiles.clear();
    this.provider.refresh();
  }

  // Getters

  public List<MainConfigFace> getLoadedFiles() {
    ArrayList<MainConfigFace> list = new ArrayList<MainConfigFace>(loadedFiles.values());
    Collections.sort(list);
    return list;
  }

  // FileChangesListener

  @Override
  public void informFileAdded(final File f) {
    System.out.println("  --> received file added: " + f.getAbsolutePath());
    // Ignore
  }

  @Override
  public void informFileRemoved(final File f) {
    System.out.println("  --> received file removed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    printLoadedFiles();
    if (this.loadedFiles.containsKey(fullPathName)) {
      System.out.println("  >> Found to remove");
      this.remove(this.loadedFiles.get(fullPathName));
    } else {
      System.out.println("  >> NOT Found to remove");
    }
  }

  @Override
  public void informFileChanged(final File f) {
    System.out.println("  --> received file changed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    if (this.loadedFiles.containsKey(fullPathName)) {
      this.reload(this.loadedFiles.get(fullPathName));
    }
  }

  private void printLoadedFiles() {
    for (String p : this.loadedFiles.keySet()) {
      System.out.println("## currently loaded: " + p);
    }
  }

}

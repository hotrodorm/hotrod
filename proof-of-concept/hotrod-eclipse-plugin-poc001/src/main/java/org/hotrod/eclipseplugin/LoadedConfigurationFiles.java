package org.hotrod.eclipseplugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hotrod.eclipseplugin.treeview.FaceProducer;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class LoadedConfigurationFiles {

  private static final String VALID_HOTROD_EXTENSION = ".xml";

  private HotRodViewContentProvider provider;
  private Map<String, MainConfigFace> loadedFiles = new TreeMap<String, MainConfigFace>();

  // Constructor

  public LoadedConfigurationFiles(final HotRodViewContentProvider provider) {
    super();
    this.provider = provider;
  }

  // File changes

  public void addFile(final String fullPathName) {
    if (fullPathName != null && fullPathName.endsWith(VALID_HOTROD_EXTENSION)) {
      if (!this.loadedFiles.containsKey(fullPathName)) {
        System.out.println("adding file: " + fullPathName);
        MainConfigFace face = FaceProducer.load(this.provider, fullPathName);
        String key = fullPathName;
        System.out.println("face '" + key + "' [" + face.getPath() + "]: valid=" + face.isValid());
        this.loadedFiles.put(key, face);
        this.provider.refresh();
      }
    }
  }

  public void removeAll() {
    for (MainConfigFace f : this.loadedFiles.values()) {
      // TODO: perform a proper unload to free resources
      // f.unload();
    }
    this.loadedFiles.clear();
    this.provider.refresh();
  }

  // Getters

  public List<MainConfigFace> getLoadedFiles() {
    ArrayList<MainConfigFace> list = new ArrayList<MainConfigFace>(loadedFiles.values());
    Collections.sort(list);
    return list;
  }

}

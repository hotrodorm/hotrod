package org.hotrod.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.hotrod.config.FragmentTag;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class FileRegistry {

  private Set<String> names = new HashSet<String>();

  public FileRegistry(final File firstFile) {
    if (firstFile != null) {
      this.names.add(firstFile.getAbsolutePath());
    }
  }

  public void add(final FragmentTag containerTag, final File f) throws FileAlreadyRegisteredException {
    if (f != null) {
      String absName = f.getAbsolutePath();
      if (this.names.contains(absName)) {
        throw new FileAlreadyRegisteredException(containerTag);
      }
      this.names.add(absName);
    }
  }

  public class FileAlreadyRegisteredException extends Exception {

    private static final long serialVersionUID = 1L;

    private FragmentTag containerTag;

    private FileAlreadyRegisteredException(final FragmentTag containerTag) {
      super();
      this.containerTag = containerTag;
    }

    public FragmentTag getContainerTag() {
      return containerTag;
    }

  }

  public String toString() {
    ListWriter w = new ListWriter(", ");
    for (String n : this.names) {
      int idx = n.lastIndexOf('/');
      w.add(idx == -1 ? n : n.substring(idx + 1));
    }
    return "FileRegistry[" + this.names.size() + "]: " + w.toString();
  }

}

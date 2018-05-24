package org.hotrod.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileRegistry {

  private Set<String> names = new HashSet<String>();

  public FileRegistry(final File firstFile) {
    if (firstFile != null) {
      this.names.add(firstFile.getAbsolutePath());
    }
  }

  public void add(final File f) throws FileAlreadyRegisteredException {
    if (f != null) {
      String absName = f.getAbsolutePath();
      if (this.names.contains(absName)) {
        throw new FileAlreadyRegisteredException();
      }
      this.names.add(absName);
    }
  }

  public class FileAlreadyRegisteredException extends Exception {

    private static final long serialVersionUID = 1L;

    private FileAlreadyRegisteredException() {
      super();
    }

  }

}

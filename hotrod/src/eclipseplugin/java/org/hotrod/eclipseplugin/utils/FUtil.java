package org.hotrod.eclipseplugin.utils;

import java.io.File;

public class FUtil {

  // Returns the relative file if "f" is inside "folder", or null otherwise.
  public static File getRelativeFile(final File f, final File folder) {
    if (f == null) {
      return null;
    }
    if (folder == null) {
      return null;
    }
    String sfolder = folder.getAbsolutePath();
    String sfile = f.getAbsolutePath();
    if (sfile.equals(sfolder)) {
      return new File("");
    } else {
      String head = sfolder + File.separator;
      if (sfile.startsWith(head)) {
        String relName = sfile.substring(head.length());
        return new File(relName);
      } else {
        return null;
      }
    }
  }

  public static boolean isAbsolute(final File f) {
    if (f == null) {
      return false;
    }
    return f.getAbsolutePath().equals(f.getPath());
  }

  public static boolean equals(final File a, final File b) {
    if (a == null) {
      return b == null;
    } else {
      return a.getAbsolutePath().equals(b.getAbsolutePath());
    }
  }

}

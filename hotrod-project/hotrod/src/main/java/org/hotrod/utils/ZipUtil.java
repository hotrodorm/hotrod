package org.hotrod.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

  private static final Logger log = Logger.getLogger(ZipUtil.class.getName());

  public static void pack(File sourceDir, File zipFile) throws IOException {
    log.fine("init");
    if (zipFile.exists()) {
      if (!zipFile.delete()) {
        throw new IOException("Could not delete zip file: " + zipFile);
      }
    }
    Path dest = Files.createFile(zipFile.toPath());
    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(dest))) {
      Path from = sourceDir.toPath();

      // root plus all descendants, excluding directories
      List<Path> paths = null;
      try (Stream<Path> stream = Files.walk(from)) {
        paths = stream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
      }

      for (Path p : paths) {
        String relPath = from.relativize(p).toString().replace(File.separatorChar, '/');
        ZipEntry zipEntry = new ZipEntry(relPath);
        try {
          zs.putNextEntry(zipEntry);
          Files.copy(p, zs);
        } finally {
          zs.closeEntry();
        }
      }

    }
  }

}

package org.hotrodorm.hotrod.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

  public static void pack(File sourceDir, File zipFile) throws IOException {
    if (zipFile.exists()) {
      zipFile.delete();
    }
    Path dest = Files.createFile(zipFile.toPath());
    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(dest))) {
      Path from = sourceDir.toPath();
      Files.walk(from).forEach(p -> {
        if (!Files.isDirectory(p)) {
          String relPath = from.relativize(p).toString();
//          System.out.println("relPath=" + relPath);
          ZipEntry zipEntry = new ZipEntry(relPath);
          try {
            zs.putNextEntry(zipEntry);
            Files.copy(p, zs);
            zs.closeEntry();
          } catch (IOException e) {
            System.err.println("relPath=" + relPath);
            e.printStackTrace();

          }
        }
      });
    }
  }

//  private void zipDirectory(final File fromDir, String zipFileName) {
//    try (ZipOutputStream zs = new ZipOutputStream(new FileOutputStream(new File(zipFileName)))) {
//
//      ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
//      try {
//        zs.putNextEntry(zipEntry);
//        Files.copy(path, zs);
//        zs.closeEntry();
//      } catch (IOException e) {
//        System.err.println(e);
//      }
//
//      // now zip files one by one
//      // create ZipOutputStream to write to the zip file
//      FileOutputStream fos = new FileOutputStream(zipDirName);
//      ZipOutputStream zos = new ZipOutputStream(fos);
//      for (String filePath : filesListInDir) {
//        System.out.println("Zipping " + filePath);
//        // for ZipEntry we need to keep only relative file path, so we used substring on
//        // absolute path
//        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
//        zos.putNextEntry(ze);
//        // read the file and write to ZipOutputStream
//        FileInputStream fis = new FileInputStream(filePath);
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = fis.read(buffer)) > 0) {
//          zos.write(buffer, 0, len);
//        }
//        zos.closeEntry();
//        fis.close();
//      }
//      zos.close();
//      fos.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

}

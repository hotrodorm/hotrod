package org.hotrod.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DiagnosticAndArchiveTest {

  @TempDir
  Path temporaryFolder;

  @Test
  public void stackUtilitiesIncludeThisTestMethod() {
    assertTrue(TUtil.renderStackTrace().contains("DiagnosticAndArchiveTest"));
    assertTrue(TUtil.callStack().contains("DiagnosticAndArchiveTest"));
    String l = LogUtil.getCaller();
    assertTrue(l.contains("DiagnosticAndArchiveTest"));
    assertTrue(LogUtil.renderStack().contains("DiagnosticAndArchiveTest"));
  }

  @Test
  public void packCreatesZipWithRelativeFileEntries() throws Exception {
    File source = temporaryFolder.resolve("source").toFile();
    assertTrue(source.mkdir());
    File nested = new File(source, "nested");
    assertTrue(nested.mkdir());
    Files.write(new File(source, "root.txt").toPath(), "root".getBytes(StandardCharsets.UTF_8));
    Files.write(new File(nested, "child.txt").toPath(), "child".getBytes(StandardCharsets.UTF_8));
    File archive = temporaryFolder.resolve("archive.zip").toFile();

    ZipUtil.pack(source, archive);

    assertTrue(archive.isFile());
    try (ZipFile zip = new ZipFile(archive)) {
      assertTrue(zip.getEntry("root.txt") != null);
      assertTrue(zip.getEntry("nested/child.txt") != null);
      assertFalse(zip.getEntry(source.getName() + "/root.txt") != null);
    }
  }
}

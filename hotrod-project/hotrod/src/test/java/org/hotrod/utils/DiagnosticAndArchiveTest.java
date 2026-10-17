package org.hotrod.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipFile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DiagnosticAndArchiveTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

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
    File source = temporaryFolder.newFolder("source");
    File nested = new File(source, "nested");
    assertTrue(nested.mkdir());
    Files.write(new File(source, "root.txt").toPath(), "root".getBytes(StandardCharsets.UTF_8));
    Files.write(new File(nested, "child.txt").toPath(), "child".getBytes(StandardCharsets.UTF_8));
    File archive = new File(temporaryFolder.getRoot(), "archive.zip");

    ZipUtil.pack(source, archive);

    assertTrue(archive.isFile());
    try (ZipFile zip = new ZipFile(archive)) {
      assertTrue(zip.getEntry("root.txt") != null);
      assertTrue(zip.getEntry("nested/child.txt") != null || zip.getEntry("nested\\child.txt") != null);
      assertFalse(zip.getEntry(source.getName() + "/root.txt") != null);
    }
  }
}

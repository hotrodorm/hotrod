package automatedtests.eclipseplugin;

import java.io.File;

import org.hotrod.eclipseplugin.utils.FUtil;

import junit.framework.TestCase;

public class FileUtilsTests extends TestCase {

  public FileUtilsTests(final String txt) {
    super(txt);
  }

  public void testIncluded() {

    {
      File folder = new File("/home/abc");
      File f = new File("/home/abc/def.txt");
      File relFile = FUtil.getRelativeFile(f, folder);
      display("relFile=" + relFile);
      assertTrue(relFile != null);
      assertEquals("def.txt", relFile.getPath());
    }

    {
      File folder = new File("/home/abc");
      File f = new File("/home/abc");
      File relFile = FUtil.getRelativeFile(f, folder);
      display("relFile=" + relFile);
      assertTrue(relFile != null);
      assertEquals("", relFile.getPath());
    }

  }

  public void testExcluded() {

    {
      File folder = new File("/home/abc1");
      File f = new File("/home/abc/def.txt");
      File relFile = FUtil.getRelativeFile(f, folder);
      display("relFile=" + relFile);
      assertTrue(relFile == null);
    }

    {
      File folder = new File("/home/abc");
      File f = new File("/home/def/ghi.txt");
      File relFile = FUtil.getRelativeFile(f, folder);
      display("relFile=" + relFile);
      assertTrue(relFile == null);
    }

  }

  // Utilities

  public void display(final String txt) {
    System.out.println(txt);
  }

}

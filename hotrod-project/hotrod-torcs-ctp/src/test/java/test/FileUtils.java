package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

  public static void saveToFile(final byte[] data, final File f) throws CouldNotSaveToFileException {
    BufferedOutputStream fos = null;
    try {
      fos = new BufferedOutputStream(new FileOutputStream(f));
      fos.write(data);
      fos.close();
    } catch (Throwable e) {
      throw new CouldNotSaveToFileException(e);
    } finally {
      if (fos != null) {
        closeSilently(fos);
      }
    }
  }

  public static byte[] readFromFile(final File f) throws CouldNotReadFromFileException {
    BufferedInputStream fis = null;
    try {
      fis = new BufferedInputStream(new FileInputStream(f));
      byte[] data = new byte[(int) f.length()];
      fis.read(data);
      return data;

    } catch (Throwable e) {
      throw new CouldNotReadFromFileException(e);
    } finally {
      if (fis != null) {
        closeSilently(fis);
      }
    }
  }

  public static class CouldNotSaveToFileException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotSaveToFileException(final Throwable cause) {
      super(cause);
    }

  }

  public static class CouldNotReadFromFileException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotReadFromFileException(final Throwable cause) {
      super(cause);
    }

  }

  // Utilities

  private static void closeSilently(final OutputStream os) {
    try {
      os.close();
    } catch (Throwable e) {
      // Swallow this exception
    }
  }

  private static void closeSilently(final InputStream is) {
    try {
      is.close();
    } catch (Throwable e) {
      // Swallow this exception
    }
  }

}

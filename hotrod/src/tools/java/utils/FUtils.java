package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FUtils {

  public static String loadFileAsString(final File f)
      throws FileNotFoundException, UnsupportedEncodingException, IOException {
    return loadFileAsString(f, null);
  }

  public static String loadFileAsString(final File f, final String encoding)
      throws FileNotFoundException, UnsupportedEncodingException, IOException {
    BufferedReader r = null;
    try {
      InputStreamReader isr = null;
      if (encoding == null) {
        isr = new InputStreamReader(new FileInputStream(f));
      } else {
        isr = new InputStreamReader(new FileInputStream(f), encoding);
      }
      r = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();

      int c;
      while ((c = r.read()) != -1) {
        sb.append((char) c);
      }

      return sb.toString();
    } finally {
      if (r != null) {
        r.close();
      }
    }
  }

  public static void saveStringToFile(final File f, final String txt) throws IOException {
    BufferedWriter w = null;
    try {

      w = new BufferedWriter(new FileWriter(f));
      w.write(txt);

    } finally {
      if (w != null) {
        w.close();
      }
    }
  }

}

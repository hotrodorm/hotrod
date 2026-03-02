package app.lobs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Verifier {

  private int successes = 0;
  private int failures = 0;

  public void equals(int actual, int expected) {
    if (actual != expected) {
      this.failures++;
      throw new RuntimeException("The actual value " + actual + " does not match the expected value " + expected);
    }
    this.successes++;
  }

  public void equals(ResultSet rs, int ordinal, byte[] expected)
      throws IOException, FileNotFoundException, SQLException {
    try (InputStream is = rs.getBinaryStream(ordinal)) {
      byte[] data = load(is);
      equals(data, expected);
    }
  }

  public void equals(ResultSet rs, int ordinal, String expected) throws IOException, SQLException {
    try (Reader r = rs.getCharacterStream(ordinal)) {
      String text = load(r);
      equals(text, expected);
    }
  }

  public void equals(byte[] was, byte[] expected) {
    if (!Arrays.equals(was, expected)) {
      this.failures++;
      throw new RuntimeException(
          "The BLOB does not match: read=" + HexaUtils.toHexa(was) + " expected=" + HexaUtils.toHexa(expected));
    }
    this.successes++;
  }

  public void equals(String was, String expected) {
    if (was == null) {
      if (expected != null) {
        this.failures++;
        throw new RuntimeException("The CLOB does not match: read=" + was + " expected=" + expected);
      }
    } else if (!was.equals(expected)) {
      this.failures++;
      throw new RuntimeException("The CLOB does not match: read=" + was + " expected=" + expected);
    }
    this.successes++;
  }

  private byte[] load(InputStream source) throws IOException {
    byte[] buf = new byte[8192];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int length;
    while ((length = source.read(buf)) != -1) {
      baos.write(buf, 0, length);
    }
    return baos.toByteArray();
  }

  private String load(Reader r) throws IOException {
    StringBuilder sb = new StringBuilder();
    char[] buffer = new char[8192];
    int length;
    while ((length = r.read(buffer)) >= 0) {
      sb.append(buffer, 0, length);
    }
    return sb.toString();
  }

  public String toString() {
    return "" + (this.successes + this.failures) + " total verifications -- " + this.successes + " successes, "
        + this.failures + " failures";
  }

}

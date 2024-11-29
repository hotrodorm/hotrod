package org.hotrodorm.hotrod.utils;

public class HexaUtils {

  private static final String DIGITS = "0123456789abcdef";

  public static String toHexa(final byte[] byteArray) {
    if (byteArray == null) {
      return null;
    }
    return toHexa(byteArray, 0, byteArray.length);
  }

  public static String toHexa(final byte[] byteArray, final int start, final int end) {
    if (byteArray == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int i = start; i < end; i++) {
      int b = byteArray[i];
      if (b < 0) {
        b = b + 256;
      }
      int hi = ((int) b) / 16;
      int lo = ((int) b) & 0xf;
      sb.append(DIGITS.charAt(hi));
      sb.append(DIGITS.charAt(lo));
    }
    return sb.toString();
  }

  public static String toHexa(final byte b) {
    int i = b;
    if (i < 0) {
      i = i + 256;
    }
    int hi = ((int) i) / 16;
    int lo = ((int) i) & 0xf;
    return "" + DIGITS.charAt(hi) + DIGITS.charAt(lo);
  }

  public static byte[] toByteArray(final String hexa) {
    if (hexa == null) {
      return null;
    }
    byte[] bytes = new byte[hexa.length() / 2];
    for (int i = 0; i + 1 < hexa.length(); i = i + 2) {

      int hi = DIGITS.indexOf(hexa.charAt(i));
      if (hi == -1) {
        throw new IllegalArgumentException(
            "Character '" + DIGITS.indexOf(hexa.charAt(i)) + "' is not an hexa character.");
      }

      int lo = DIGITS.indexOf(hexa.charAt(i + 1));
      if (lo == -1) {
        throw new IllegalArgumentException(
            "Character '" + DIGITS.indexOf(hexa.charAt(i + 1)) + "' is not an hexa character.");
      }

      bytes[i / 2] = (byte) (hi * 16 + lo);
    }
    return bytes;
  }

  public static String charToHexa(final char c) {
    int v = c < 0 ? c + 65536 : c;
    String rendered = HexaUtils.toHexa((byte) (v / 256)) + "/" + HexaUtils.toHexa((byte) (v % 256));
    return rendered;
  }

}

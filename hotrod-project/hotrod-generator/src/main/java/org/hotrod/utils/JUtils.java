package org.hotrod.utils;

public class JUtils {

  // escapes:
  // * \ for \\
  // * double quotes for \"

  public static String escapeJavaString(final String txt) {
    return txt == null ? null : txt.replace("\\", "\\\\").replace("\"", "\\\"");
  }

}

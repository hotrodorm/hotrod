package org.hotrod.runtime.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class SUtils {

  public static boolean isEmpty(final String txt) {
    return txt == null || txt.trim().isEmpty();
  }

  public static boolean equals(final String a, final String b) {
    return a == null ? b == null : a.equals(b);
  }

  public static String capitalize(final String txt) {
    if (txt == null) {
      return null;
    }
    String lower = txt.trim().toLowerCase();
    if (lower.isEmpty()) {
      return "";
    }
    boolean inWord = false;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lower.length(); i++) {
      char c = lower.charAt(i);
      if (!inWord) {
        sb.append(Character.toUpperCase(c));
      } else {
        sb.append(c);
      }
      inWord = c >= 'a' && c <= 'z';
    }
    return sb.toString();
  }

  public static String capitalizeFirst(final String txt) {
    if (txt == null) {
      return null;
    }
    return txt.substring(0, 1).toUpperCase() + txt.substring(1);
  }

  public static boolean startsWithUpperCaseLetter(final String txt) {
    if (txt == null || txt.isEmpty()) {
      return false;
    }
    return isUpperCase(txt.charAt(0));
  }

  public static boolean isUpperCase(final char c) {
    return c >= 'A' && c <= 'Z';
  }

  public static boolean startsWithLowerCaseLetter(final String txt) {
    if (txt == null || txt.isEmpty()) {
      return false;
    }
    return isLowerCase(txt.charAt(0));
  }

  public static boolean isLowerCase(final char c) {
    return c >= 'a' && c <= 'z';
  }

  public static boolean startsWithIgnoreCase(final String txt, final String prefix) {
    if (txt == null || prefix == null) {
      return false;
    }
    if (prefix.length() > txt.length()) {
      return false;
    }
    return prefix.equalsIgnoreCase(txt.substring(0, prefix.length()));
  }

  public static boolean endsWithIgnoreCase(final String txt, final String suffix) {
    if (txt == null || suffix == null) {
      return false;
    }
    if (suffix.length() > txt.length()) {
      return false;
    }
    return suffix.equalsIgnoreCase(txt.substring(txt.length() - suffix.length()));
  }

  public static String getFiller(final char c, final int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append(c);
    }
    return sb.toString();
  }

  public static String indent(final String txt, final int indent) {
    if (txt == null) {
      return null;
    }
    ListWriter w = new ListWriter(getFiller(' ', indent), "", "\n");
    for (String line : txt.split("\n")) {
      w.add(line);
    }
    return w.toString();
  }

  public static String escapeJavaString(final String txt) {
    if (txt == null) {
      return null;
    }
    return txt //
        .replace("\\", "\\\\") //
        .replace("\"", "\\\"") //
        .replace("\t", "\\t") //
        .replace("\b", "\\b") //
        .replace("\n", "\\n") //
        .replace("\r", "\\r") //
        .replace("\f", "\\f") //
    ;
  }

  public static String escapeXmlBody(final String txt) {
    if (txt == null) {
      return null;
    }
    return txt //
        .replace("&", "&amp;") //
        .replace("<", "&lt;") //
    ;
  }

  public static String escapeXmlAttribute(final String txt) {
    if (txt == null) {
      return null;
    }
    return txt //
        .replace("&", "&amp;") //
        .replace("<", "&lt;") //
        .replace("\"", "&quot;") //
        .replace("'", "&apos;") //
    ;
  }

  public static String loadFileAsString(final File f) throws IOException {
    return loadFileAsString(f, null);
  }

  public static String loadFileAsString(final File f, final String readerEncoding) throws IOException {
    String line;
    BufferedReader r = null;
    StringBuilder sb = new StringBuilder();
    try {
      if (readerEncoding != null) {
        r = new BufferedReader(new InputStreamReader(new FileInputStream(f), readerEncoding));
      } else {
        r = new BufferedReader(new FileReader(f));
      }
      while ((line = r.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
      }
      return sb.toString();
    } finally {
      if (r != null) {
        r.close();
      }
    }
  }

  public static void saveStringToFile(final String txt, final File f) throws IOException {
    Writer w = null;
    try {
      w = new BufferedWriter(new FileWriter(f));
      w.write(txt);
    } finally {
      if (w != null) {
        w.close();
      }
    }
  }

  public static int countMatches(final String txt, final String searchText) {
    int pos = 0;
    int count = 0;
    while (pos <= txt.length() && ((pos = txt.indexOf(searchText, pos)) != -1)) {
      count++;
      pos = pos + searchText.length();
    }
    return count;
  }

  public static String alignRight(final String txt, final int width) {
    if (txt == null) {
      return null;
    }
    if (txt.length() >= width) {
      return txt;
    }
    String filler = getFiller(' ', width - txt.length());
    return filler + txt;
  }

  public static String alignLeft(final String txt, final int width) {
    if (txt == null) {
      return null;
    }
    if (txt.length() >= width) {
      return txt;
    }
    String filler = getFiller(' ', width - txt.length());
    return txt + filler;
  }

}

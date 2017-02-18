package org.hotrod.utils;

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

  public static String getFiller(final char c, final int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append(c);
    }
    return sb.toString();
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

}

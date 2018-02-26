package org.hotrod.eclipseplugin.utils;

import java.util.ArrayList;
import java.util.List;

public class ClassPathEncoder {

  private static final char SEPARATOR = ':';
  private static final char ESCAPE = '\\';

  private static final String REPLACEMENT = "" + ESCAPE + ESCAPE + SEPARATOR;

  public static String encode(final List<String> paths) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String p : paths) {
      if (first) {
        first = false;
      } else {
        sb.append(SEPARATOR);
      }
      String escaped = p.replaceAll("" + SEPARATOR, REPLACEMENT);
      sb.append(escaped);
    }
    return sb.toString();
  }

  public static List<String> decode(final String encoded) {
    List<String> paths = new ArrayList<String>();
    int pos = 0;
    while (pos < encoded.length()) {
      int idx = nextSeparatorIndex(encoded, pos);
      String p;
      if (idx == -1) {
        p = encoded.substring(pos);
        pos = encoded.length();
      } else {
        p = encoded.substring(pos, idx);
        pos = idx + 1;
      }
      paths.add(p.replaceAll(REPLACEMENT, "" + SEPARATOR));
    }
    return paths;
  }

  private static int nextSeparatorIndex(final String encoded, final int start) {
    int pos = start;
    while (true) {
      int idx = encoded.indexOf(SEPARATOR, pos);
      if (idx == -1) {
        return idx;
      }
      if (idx == 0 || encoded.charAt(idx - 1) != ESCAPE) {
        return idx;
      }
      pos = idx + 1;
    }
  }

}

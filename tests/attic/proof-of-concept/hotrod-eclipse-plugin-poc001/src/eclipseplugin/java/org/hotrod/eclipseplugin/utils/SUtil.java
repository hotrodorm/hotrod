package org.hotrod.eclipseplugin.utils;

import java.util.ArrayList;
import java.util.List;

public class SUtil {

  public static boolean isEmpty(final String txt) {
    return txt == null || txt.trim().isEmpty();
  }

  public static List<String> slice(final String txt, final int width) {
    List<String> slices = new ArrayList<String>();
    int len = txt.length();
    for (int i = 0; i < len; i += width) {
      slices.add(txt.substring(i, Math.min(len, i + width)));
    }
    return slices;
  }

}

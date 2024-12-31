package org.hotrod.dynamicsql;

public abstract class Utl {

  public static boolean isEmpty(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static String coalesce(String... s) {
    if (s == null) {
      return null;
    }
    for (int i = 0; i < s.length; i++) {
      if (s[i] != null) {
        return s[i];
      }
    }
    return null;
  }

}

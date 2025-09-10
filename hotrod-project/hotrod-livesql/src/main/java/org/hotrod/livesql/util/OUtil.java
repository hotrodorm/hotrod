package org.hotrod.livesql.util;

public class OUtil {

  public static String hc(Object obj) {
    return obj == null ? "" : obj.getClass().getName() + "@" + String.format("%08x", System.identityHashCode(obj));
  }

}

package org.hotrod.utils;

import org.hotrod.runtime.util.ListWriter;

public class EUtils {

  public static String renderMessages(final Throwable t) {
    return renderMessages(t, "", "", ": ");
  }

  public static String renderMessages(final Throwable t, final String prefix, final String suffix,
      final String separator) {
    ListWriter w = new ListWriter(prefix, suffix, separator);
    Throwable current = t;
    while (current != null) {
      if (current.getMessage() != null) {
        w.add(current.getMessage());
      }
      current = current.getCause();
    }
    return w.toString();
  }

}

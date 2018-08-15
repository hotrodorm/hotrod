package org.hotrod.utils;

import org.apache.log4j.Logger;
import org.hotrod.runtime.util.ListWriter;

public class EUtils {

  private static transient final Logger log = Logger.getLogger(EUtils.class);

  public static String renderMessages(final Throwable t) {
    return renderMessages(t, "", "", ": ");
  }

  public static String renderMessages(final Throwable t, final String prefix, final String suffix,
      final String separator) {
    ListWriter w = new ListWriter(prefix, suffix, separator);
    Throwable current = t;
    while (current != null) {
      String msg = current.getMessage();
      log.debug("Exception Stack [" + current.getClass().getName() + "] " + msg);
      if (msg != null) {
        w.add(msg);
      }
      current = current.getCause();
    }
    return w.toString();
  }

}

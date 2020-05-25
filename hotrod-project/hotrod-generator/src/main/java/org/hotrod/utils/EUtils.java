package org.hotrod.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class EUtils {

  private static transient final Logger log = LogManager.getLogger(EUtils.class);

  @Deprecated
  public static String renderMessages(final Throwable t) {
    return renderMessages(t, "", "", ": ");
  }

  @Deprecated
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

package org.hotrod.livesql.util;

import java.util.logging.Logger;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public abstract class LoggingUtil {

  private static final Logger log = Logger.getLogger(LoggingUtil.class.getName());

  private LoggingUtil() {
    log.fine("init");
  }

  public static void logQuery(final LiveSQLPreparedQuery q, final LiveSQLLogging loggingAdapter) {
    if (loggingAdapter != null && loggingAdapter.fullEnabled()) {
      loggingAdapter.fullLog(q.getPreview(true));
    } else if (loggingAdapter != null && loggingAdapter.basicEnabled()) {
      loggingAdapter.basicLog(q.getPreview(false));
    }
  }

}

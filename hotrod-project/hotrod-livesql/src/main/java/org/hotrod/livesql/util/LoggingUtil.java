package org.hotrod.livesql.util;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public abstract class LoggingUtil {

  private LoggingUtil() {
  }

  public static void logQuery(final LiveSQLPreparedQuery q, final LiveSQLLogging loggingAdapter) {
    if (loggingAdapter != null && loggingAdapter.fullEnabled()) {
      loggingAdapter.fullLog(q.getPreview(true));
    } else if (loggingAdapter != null && loggingAdapter.basicEnabled()) {
      loggingAdapter.basicLog(q.getPreview(false));
    }
  }

}

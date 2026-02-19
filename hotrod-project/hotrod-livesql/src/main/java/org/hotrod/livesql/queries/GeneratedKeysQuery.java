package org.hotrod.livesql.queries;

import org.hotrod.livesql.LiveSQLLogging;

public interface GeneratedKeysQuery<T> extends Query {

  public T execute();

  public T execute(LiveSQLLogging loggingAdapter);

}

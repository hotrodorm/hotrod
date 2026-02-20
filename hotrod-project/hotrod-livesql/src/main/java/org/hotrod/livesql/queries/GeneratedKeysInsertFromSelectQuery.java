package org.hotrod.livesql.queries;

import org.hotrod.livesql.LiveSQLLogging;

public interface GeneratedKeysInsertFromSelectQuery<T> extends Query {

  public InsertResult<T> execute();

  public InsertResult<T> execute(LiveSQLLogging loggingAdapter);

}

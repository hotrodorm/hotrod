package org.hotrod.livesql.queries;

import java.util.List;

import org.hotrod.livesql.LiveSQLLogging;

public interface GeneratedKeysInsertFromSelectQuery<T> extends Query {

  public List<T> execute();

  public List<T> execute(LiveSQLLogging loggingAdapter);

}

package org.hotrod.livesql.queries;

import org.hotrod.livesql.LiveSQLLogging;

public interface DMLQuery extends Query {

  public int execute();

  public int execute(LiveSQLLogging loggingAdapter);

}

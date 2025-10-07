package org.hotrod.livesql.queries.select;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.queries.Query;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;

public abstract class Select<R> implements Query {

  public abstract List<R> execute();

  public abstract Cursor<R> executeCursor() throws SQLException;

  public abstract Cursor<R> executeCursor(int fetchSize) throws SQLException;

  public abstract R executeOne();

  public abstract List<R> execute(LiveSQLLogging loggingAdapter);

  public abstract Cursor<R> executeCursor(LiveSQLLogging loggingAdapter) throws SQLException;

  public abstract Cursor<R> executeCursor(int fetchSize, LiveSQLLogging loggingAdapter) throws SQLException;

  public abstract R executeOne(LiveSQLLogging loggingAdapter);

  protected abstract CombinedSelectObject<R> getCombinedSelect();

}

package org.hotrod.runtime.livesql.queries.select;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.Query;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public abstract class Select<R> implements Query {

  public abstract List<R> execute();

  public abstract Cursor<R> executeCursor() throws SQLException;

  public abstract Cursor<R> executeCursor(int fetchSize) throws SQLException;

  public abstract R executeOne();

  protected abstract CombinedSelectObject<R> getCombinedSelect();

}

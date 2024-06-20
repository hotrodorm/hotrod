package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.Query;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public abstract class Select<R> implements Query {

  public abstract List<R> execute();

  public abstract Cursor<R> executeCursor();

  public abstract R executeOne();

  protected abstract CombinedSelectObject<R> getCombinedSelect();

}

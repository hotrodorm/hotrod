package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectLimitPhase<R> implements Select<R> {

  // Properties

  private LiveSQLContext context;
  private SelectObject<R> select;

  // Constructor

  CombinedSelectLimitPhase(final LiveSQLContext context, final SelectObject<R> select, final int limit) {
    this.context = context;
    this.select = select;
    this.select.setLimit(limit);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute(this.context);
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor(this.context);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

  // Executable Select

  @Override
  public SelectObject<R> getSelect() {
    return this.select;
  }

}

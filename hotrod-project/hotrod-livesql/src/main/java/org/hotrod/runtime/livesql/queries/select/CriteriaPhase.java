package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class CriteriaPhase<T> implements EntitySelect<T> {

  protected LiveSQLContext context;
  protected AbstractSelectObject<T> select;

  protected CriteriaPhase(final LiveSQLContext context, final AbstractSelectObject<T> select) {
    this.context = context;
    this.select = select;
  }

  // next phases

  // execute

  public final List<T> execute() {
    return this.select.execute(this.context);
  }

  public final Cursor<T> executeCursor() {
    return this.select.executeCursor(this.context);
  }

  public final T executeOne() {
    return this.select.executeOne(this.context);
  }

  // rendering

  protected void renderTo(QueryWriter w) {
    this.select.renderTo(w);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

}

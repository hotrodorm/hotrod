package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public abstract class CriteriaPhase<T> implements EntitySelect<T> {

  protected LiveSQLContext context;
  protected AbstractSelectObject<T> select;
  protected String mapperStatement;

  protected CriteriaPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement) {
    this.context = context;
    this.select = select;
    this.mapperStatement = mapperStatement;
  }

  // next phases

  // execute

  public List<T> execute() {
    return this.select.execute(this.context, this.mapperStatement);
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor(this.context, this.mapperStatement);
  }

  public T executeOne() {
    return this.select.executeOne(this.context, this.mapperStatement);
  }

  // rendering

  @Override
  public void renderTo(QueryWriter w) {
    this.select.renderTo(w);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

}
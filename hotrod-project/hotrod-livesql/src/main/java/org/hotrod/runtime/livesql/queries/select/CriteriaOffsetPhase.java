package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaOffsetPhase<T> implements ExecutableCriteriaSelect<T> {

  private LiveSQLContext context;
  private AbstractSelectObject<T> select;
  private String mapperStatement;

  CriteriaOffsetPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement) {
    this.context = context;
    this.select = select;
    this.mapperStatement = mapperStatement;
  }

  // next phases

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.limit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select, this.mapperStatement);
  }

  // execute

  public List<T> execute() {
    return this.select.execute(this.context, this.mapperStatement);
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor(this.context, this.mapperStatement);
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

package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;

public class CriteriaOrderByPhase<T> implements ExecutableCriteriaSelect<T> {

  private AbstractSelect<T> select;
  private String mapperStatement;

  CriteriaOrderByPhase(final AbstractSelect<T> select, final String mapperStatement) {
    this.select = select;
    this.mapperStatement = mapperStatement;
  }

  // next phases

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.offset(offset);
    return new CriteriaOffsetPhase<T>(this.select, this.mapperStatement);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.limit(limit);
    return new CriteriaLimitPhase<T>(this.select, this.mapperStatement);
  }

  // execute

  public List<T> execute() {
    return this.select.execute(this.mapperStatement);
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor(this.mapperStatement);
  }

  // rendering

  @Override
  public void renderTo(QueryWriter w) {
    this.select.renderTo(w);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

}

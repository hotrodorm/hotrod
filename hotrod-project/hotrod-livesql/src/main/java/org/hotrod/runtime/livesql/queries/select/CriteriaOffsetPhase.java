package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;

public class CriteriaOffsetPhase<T> implements ExecutableCriteriaSelect<T> {

  private AbstractSelect<T> select;

  CriteriaOffsetPhase(final AbstractSelect<T> select) {
    this.select = select;
  }

  // next phases

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.limit(limit);
    return new CriteriaLimitPhase<T>(this.select);
  }

  // execute

  public List<T> execute() {
    return this.select.execute();
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor();
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

package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public class SelectOffsetPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  SelectOffsetPhase(final Select<R> select, final int offset) {
    this.select = select;
    this.select.setOffset(offset);
  }

  // Next stages

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.select, limit);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute();
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor();
  }

  // Validation

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

  @Override
  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
    return this.select.listColumns();
  }

  // Executable Select

  @Override
  public Select<R> getSelect() {
    return this.select;
  }

}

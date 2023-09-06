package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.Select;

public class CombinedSelectLimitPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  CombinedSelectLimitPhase(final Select<R> select, final int limit) {
    this.select = select;
    this.select.setLimit(limit);
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

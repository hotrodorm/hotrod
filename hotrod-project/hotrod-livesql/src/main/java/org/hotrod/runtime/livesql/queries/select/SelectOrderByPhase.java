package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

@SuppressWarnings("deprecation")
public class SelectOrderByPhase<R> implements ExecutableSelect<R> {

  // Properties

  private LiveSQLContext context;
  private SelectObject<R> select;

  // Constructor

  SelectOrderByPhase(final LiveSQLContext context, final SelectObject<R> select, final OrderingTerm... orderingTerms) {
    this.context = context;
    this.select = select;
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.select, limit);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute(this.context);
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor(this.context);
  }

  // Validation

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

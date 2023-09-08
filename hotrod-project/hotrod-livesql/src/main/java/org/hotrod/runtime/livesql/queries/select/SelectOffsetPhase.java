package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;

public class SelectOffsetPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  SelectOffsetPhase(final LiveSQLContext context, final SelectObject<R> select, final int offset) {
    super(context, select);
    this.select.setOffset(offset);
  }

  // Next stages

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.select, limit);
  }

}

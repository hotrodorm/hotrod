package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectOffsetPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectOffsetPhase(final LiveSQLContext context, final SelectObject<R> select, final int offset) {
    super(context, select);
    this.select.setOffset(offset);
  }

  // Next phases

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.select, limit);
  }

}

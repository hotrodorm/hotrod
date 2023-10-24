package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectOffsetPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectOffsetPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int offset) {
    super(context, combined);
    combined.setOffset(offset);
  }

  // Next phases

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}

package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.LockableSelectPhase;

public class LockableSelectOffsetPhase<R> extends LockableSelectPhase<R> {

  // Constructor

  LockableSelectOffsetPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int offset) {
    super(context, combined);
    this.getLastSelect().setOffset(offset);
  }

  // Next phases

  public LockableSelectLimitPhase<R> limit(final int limit) {
    return new LockableSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}

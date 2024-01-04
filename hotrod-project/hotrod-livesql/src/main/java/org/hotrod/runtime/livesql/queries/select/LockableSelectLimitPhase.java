package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.LockableSelectPhase;

public class LockableSelectLimitPhase<R> extends LockableSelectPhase<R> {

  // Constructor

  LockableSelectLimitPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int limit) {
    super(context, combined);
    this.getLastSelect().setLimit(limit);
  }

  // Next phases

}

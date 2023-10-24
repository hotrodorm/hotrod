package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectLimitPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectLimitPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int limit) {
    super(context, combined);
    combined.setLimit(limit);
  }

}

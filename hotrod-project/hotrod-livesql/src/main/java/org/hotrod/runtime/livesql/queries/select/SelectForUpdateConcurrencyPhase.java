package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectForUpdateConcurrencyPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public SelectForUpdateConcurrencyPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Number waitTime, final boolean skipLocked) {
    super(context, combined);
    this.getLastSelect().setLockingConcurrency(waitTime, skipLocked);
  }

}

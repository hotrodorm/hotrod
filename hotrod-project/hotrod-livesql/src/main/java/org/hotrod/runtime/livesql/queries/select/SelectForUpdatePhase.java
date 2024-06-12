package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectForUpdatePhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public SelectForUpdatePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final boolean forUpdate) {
    super(context, combined);
    if (forUpdate) {
      this.getLastSelect().setForUpdate();
    } else {
      this.getLastSelect().setForShare();
    }
  }

  public SelectForUpdateConcurrencyPhase<R> noWait() {
    return new SelectForUpdateConcurrencyPhase<>(super.context, super.combined, null, false);
  }

  public SelectForUpdateConcurrencyPhase<R> wait(final int time) {
    return new SelectForUpdateConcurrencyPhase<>(super.context, super.combined, time, false);
  }

  public SelectForUpdateConcurrencyPhase<R> wait(final double time) {
    return new SelectForUpdateConcurrencyPhase<>(super.context, super.combined, time, false);
  }

  public SelectForUpdateConcurrencyPhase<R> skipLocked() {
    return new SelectForUpdateConcurrencyPhase<>(super.context, super.combined, null, true);
  }

}

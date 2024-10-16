package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectOffsetPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectOffsetPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int offset) {
    super(context, combined);
    this.getLastSelect().setOffset(offset);
  }

  // Next phases

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.combined, limit);
  }

}

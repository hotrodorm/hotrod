package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectLimitPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectLimitPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final int limit) {
    super(context, combined);
    this.getLastSelect().setLimit(limit);
  }

  // Next phases

}

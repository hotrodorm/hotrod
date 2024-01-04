package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectForUpdatePhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  public SelectForUpdatePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined) {
    super(context, combined);
    this.getLastSelect().setForUpdate();
  }

}

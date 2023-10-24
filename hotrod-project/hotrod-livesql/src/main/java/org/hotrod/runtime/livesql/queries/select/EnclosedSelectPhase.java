package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectPhase;

public class EnclosedSelectPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  public EnclosedSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined) {
    super(context, new CombinedSelectObject<>(combined, true));
  }

}

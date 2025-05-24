package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.CombinedSelectPhase;

public class EnclosedSelectPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  public EnclosedSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined) {
    super(context, new CombinedSelectObject<>(combined, true));
  }

}

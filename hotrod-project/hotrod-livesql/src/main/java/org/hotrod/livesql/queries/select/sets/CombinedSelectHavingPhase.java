package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CombinedSelectHavingPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectHavingPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Predicate predicate) {
    super(context, combined);
    if (predicate != null) {
      this.getLastSelect().setHavingCondition(predicate);
    }
  }

  // This phase

  // Next phases

}
